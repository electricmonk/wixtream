package com.wixpress.streaming.paypal;

import com.wixpress.streaming.chat.ChatCoordinationException;
import com.wixpress.streaming.chat.ChatCoordinator;
import com.wixpress.streaming.wix.AppInstance;
import com.wixpress.streaming.wix.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    ChatCoordinator chatCoordinator;

    @RequestMapping(value = "/prepare-payment", method = RequestMethod.POST)
    public String preparePayment(@RequestParam String instance, @RequestParam String clientId) throws PaypalException {
        AppInstance appInstance = getOrCreateApplication(instance);

        return "redirect:" + payPalManager.startPurchase(
                clientId,
                "http://wixstreamingapp.appspot.com/api/v1/pay/complete-payment?instance=" + instance,
                appInstance.getSettings().getPaypalMerchantEmail(),
                appInstance.getSettings().getPricePerSessionInUSD(),
                "USD"
                );
    }

    @RequestMapping(value = "/complete-payment", method = RequestMethod.GET)
    public String completePayment(@RequestParam String instance, @RequestParam String token, Model model) throws PaypalException, ChatCoordinationException {
        String clientId = payPalManager.finishPurchase(token);
        chatCoordinator.createSession(getInstance(instance).getInstanceId(), clientId);
        model.addAttribute("clientId", clientId);

        return "paypal-complete";
    }


}
