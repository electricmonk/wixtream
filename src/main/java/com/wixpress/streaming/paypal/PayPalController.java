package com.wixpress.streaming.paypal;

import com.wixpress.streaming.wix.AppInstance;
import com.wixpress.streaming.wix.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
@Controller
@RequestMapping("/api/v1/pay")
public class PayPalController extends BaseController {

    @Resource
    PayPalManager payPalManager;

    @Resource
    PayPalFacade payPalFacade;

    @ResponseBody
    @RequestMapping(value = "/prepare-payment", method = RequestMethod.POST)
    public PaymentModel preparePayment(@RequestParam String instance) throws PaypalException {
        AppInstance appInstance = getOrCreateApplication(instance);
        Double pricePerSessionInUSD = appInstance.getSettings().getPricePerSessionInUSD();

//        payPalManager.startPurchase(
//                "http://wixstreamingapp.appspot.com/api/v1/pay/complete-payment?instance=" + instance + "&price=" + pricePerSessionInUSD)

        return payPalFacade.preparePayment(new PaymentRequest(
                pricePerSessionInUSD,
                "USD",
                appInstance.getSettings().getPaypalMerchantEmail(),
                "http://wixstreamingapp.appspot.com/api/v1/pay/complete-payment/" + instance
        ));
    }

    @RequestMapping(value = "/complete-payment/{instance}", method = RequestMethod.GET)
    public String completePayment(@PathVariable String instance, @RequestParam String token) throws PaypalException {
        payPalFacade.completePayment(new CompletePaymentRequest(token));
        return "redirect:/widget/paypal?instance=" + instance;
    }


}
