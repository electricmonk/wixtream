package com.wixpress.streaming.paypal;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.paypal.exception.*;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;

import java.io.IOException;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
public class PayPalFacade {

    private final AdaptivePaymentsService paymentsService;

    public PayPalFacade() {
        try {
            paymentsService = new AdaptivePaymentsService(getClass().getClassLoader().getResourceAsStream("paypal.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read paypal.properties", e);
        }
    }

    public PaymentModel preparePayment(PaymentRequest paymentRequest) throws PaypalException {

        PayRequest request = new PayRequest();

        Receiver receiver = new Receiver(paymentRequest.getAmount());
        receiver.setEmail(paymentRequest.getReceiverEmail());
        receiver.setPaymentType("DIGITALGOODS");
        receiver.setPaymentSubType("Wix Video Chat");

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
