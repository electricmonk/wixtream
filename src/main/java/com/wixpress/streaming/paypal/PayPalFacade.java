package com.wixpress.streaming.paypal;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.paypal.exception.*;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import org.xml.sax.SAXException;
import urn.ebay.api.PayPalAPI.*;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class PayPalFacade {

    private final AdaptivePaymentsService paymentsService;
    private final PayPalAPIInterfaceServiceService expressCheckoutService;

    public PayPalFacade() {
        try {
            paymentsService = new AdaptivePaymentsService(getClass().getClassLoader().getResourceAsStream("paypal.properties"));
            expressCheckoutService = new PayPalAPIInterfaceServiceService(getClass().getClassLoader().getResourceAsStream("paypal.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read paypal.properties", e);
        }
    }

    public PaymentModel preparePayment(PaymentRequest paymentRequest) throws PaypalException {

        SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();
        SetExpressCheckoutReq request = new SetExpressCheckoutReq();
        SetExpressCheckoutRequestType requestType = new SetExpressCheckoutRequestType(details);
        request.setSetExpressCheckoutRequest(requestType);

        PaymentDetailsItemType item = new PaymentDetailsItemType();
        BasicAmountType amountType = new BasicAmountType();
        amountType.setCurrencyID(CurrencyCodeType.USD);
        amountType.setValue(paymentRequest.getAmount().toString());
        item.setQuantity(1);
        item.setAmount(amountType);
        item.setName("Video Chat");
        item.setItemCategory(ItemCategoryType.DIGITAL);

        SellerDetailsType sellerDetailsType = new SellerDetailsType();
        sellerDetailsType.setPayPalAccountID(paymentRequest.getReceiverEmail());

        PaymentDetailsType pdt = new PaymentDetailsType();
        pdt.setPaymentDetailsItem(ImmutableList.of(item));
        pdt.setSellerDetails(sellerDetailsType);

        details.setReturnURL(paymentRequest.getReturnUrl());
        details.setCancelURL(paymentRequest.getReturnUrl());
        details.setPaymentDetails(ImmutableList.of(pdt));
        details.setReqConfirmShipping("0");
        details.setNoShipping("1");
        details.setOrderTotal(amountType);

        try {
            SetExpressCheckoutResponseType response = expressCheckoutService.setExpressCheckout(request);

            return new PaymentModel(response.getToken());

        } catch (Exception e) {
            throw new PaypalException("Failed calling motherfucking PayPal", e);
        }
    }

    public void completePayment(CompletePaymentRequest request) throws PaypalException {

        GetExpressCheckoutDetailsResponseDetailsType response = getExpressCheckout(request);

        DoExpressCheckoutPaymentRequestType pprequest = new DoExpressCheckoutPaymentRequestType();
//        pprequest.setVersion("63.0");

        DoExpressCheckoutPaymentRequestDetailsType paymentDetailsRequestType = new DoExpressCheckoutPaymentRequestDetailsType();
        paymentDetailsRequestType.setToken(response.getToken());

        PayerInfoType payerInfo = response.getPayerInfo();
        paymentDetailsRequestType.setPayerID(payerInfo.getPayerID());

        PaymentDetailsType paymentDetails = response.getPaymentDetails().get(0);
        paymentDetailsRequestType.setPaymentAction(paymentDetails.getPaymentAction());

        paymentDetailsRequestType.setPaymentDetails(response.getPaymentDetails());
        pprequest.setDoExpressCheckoutPaymentRequestDetails(paymentDetailsRequestType);

        DoExpressCheckoutPaymentReq req = new DoExpressCheckoutPaymentReq();
        req.setDoExpressCheckoutPaymentRequest(pprequest);
        try {
            expressCheckoutService.doExpressCheckoutPayment(req);
        } catch (Exception e) {
            throw new PaypalException("Failed completing payment", e);
        }

    }

    private GetExpressCheckoutDetailsResponseDetailsType getExpressCheckout(CompletePaymentRequest request) throws PaypalException {
        GetExpressCheckoutDetailsRequestType pprequest = new GetExpressCheckoutDetailsRequestType();
        pprequest.setToken(request.getToken());
        GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
        getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(pprequest);
        try {
            return expressCheckoutService.getExpressCheckoutDetails(getExpressCheckoutDetailsReq).getGetExpressCheckoutDetailsResponseDetails();
        } catch (Exception e) {
            throw new PaypalException("Failed calling setGetExpressCheckoutDetailsRequest", e);
        }
    }

    public PaymentModel prepareAdaptivePayment(PaymentRequest paymentRequest) throws PaypalException {

        PayRequest request = new PayRequest();

        Receiver receiver = new Receiver(paymentRequest.getAmount());
        receiver.setEmail(paymentRequest.getReceiverEmail());
        receiver.setPaymentType("DIGITALGOODS");

        RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");

        //TODO IPN?
        request.setReceiverList(new ReceiverList(ImmutableList.of(receiver)));
        request.setActionType("PAY");
        request.setCurrencyCode(paymentRequest.getCurrenyCode());
        request.setReturnUrl(paymentRequest.getReturnUrl());
        request.setCancelUrl(paymentRequest.getReturnUrl());
        request.setRequestEnvelope(requestEnvelope);

        try {
            PayResponse response = paymentsService.pay(request);
            //TODO do something with correlation ID?
            return new PaymentModel(response.getPayKey());
        } catch (Exception e) {
            //TODO find out which exceptions really need a special treatment
            throw new PaypalException("Failed preparing payment", e);
        }
    }
}
