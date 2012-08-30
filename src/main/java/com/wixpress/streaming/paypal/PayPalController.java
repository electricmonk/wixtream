package com.wixpress.streaming.paypal;

import com.wixpress.streaming.wix.AppInstance;
import com.wixpress.streaming.wix.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author shaiyallin
 * @since 8/30/12
 */
@Controller
@RequestMapping("/api/v1/pay")
public class PayPalController extends BaseController {

    @Resource
    PayPalFacade payPalFacade;

    @ResponseBody
    @RequestMapping(value = "/prepare-payment", method = RequestMethod.POST)
    public PaymentModel preparePayment(@RequestParam String instance) throws PaypalException {
        AppInstance appInstance = getOrCreateApplication(instance);
        return payPalFacade.preparePayment(new PaymentRequest(
                appInstance.getSettings().getPricePerSessionInUSD(),
                "USD",
                appInstance.getSettings().getPaypalMerchantEmail(),
                "http://localhost:8080/api/v1/pay/complete-payment?instance=" + instance
        ));
    }

    @ResponseBody
    @RequestMapping(value = "/complete-payment", method = RequestMethod.GET)
    public String completePayment(@RequestParam String instance, @RequestParam String token) throws PaypalException {
        payPalFacade.completePayment(new CompletePaymentRequest(token));
        return "redirect:/widget/paypal?instance=" + instance;
    }


}
