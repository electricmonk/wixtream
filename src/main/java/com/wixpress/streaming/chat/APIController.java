package com.wixpress.streaming.chat;

import com.wixpress.streaming.paypal.PayPalFacade;
import com.wixpress.streaming.paypal.PaymentModel;
import com.wixpress.streaming.paypal.PaymentRequest;
import com.wixpress.streaming.paypal.PaypalException;
import com.wixpress.streaming.wix.AppInstance;
import com.wixpress.streaming.wix.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
@Controller
@RequestMapping("/api/v1/chat")
public class APIController extends BaseController {

    @Resource
    ChatCoordinator chatCoordinator;

    @Resource
    PayPalFacade payPalFacade;

    @ResponseBody
    @RequestMapping(value = "/subscribe")
    public void requestChat(@RequestParam String clientId, @RequestParam String instance) throws ChatCoordinationException {
        chatCoordinator.createSession(getInstanceId(instance), clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/subscriber-status", method = RequestMethod.GET)
    public ChatSession getSession(@RequestParam String clientId, @RequestParam String instance) {
        return chatCoordinator.getSession(getInstanceId(instance), clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/accept-subscriber")
    public ChatSession fulfillChat(@RequestParam String subscriberClientId,
                                   @RequestParam(required = false) String removePreviousSessionWithClientId,
                                   @RequestParam String instance) throws ChatCoordinationException {

        if (removePreviousSessionWithClientId != null) {
            chatCoordinator.endSession(getInstanceId(instance), removePreviousSessionWithClientId);
        }
        return chatCoordinator.fulfillSession(getInstanceId(instance), subscriberClientId);
    }

    @ResponseBody
    @RequestMapping(value = "/end-session")
    public void endSession(@RequestParam String subscriberClientId, @RequestParam String instance) throws ChatCoordinationException {
        chatCoordinator.endSession(getInstanceId(instance), subscriberClientId);
    }

    @ResponseBody
    @RequestMapping(value = "/subscriber-list", method = RequestMethod.GET)
    public List<ChatSession> waitingSessions(@RequestParam String instance) {
        return chatCoordinator.getSessions(getInstanceId(instance));
    }

    @ResponseBody
    @RequestMapping(value = "/prepare-payment", method = RequestMethod.POST)
    public PaymentModel preparePayment(@RequestParam String instance, @RequestParam String returnUrl) throws PaypalException {
        AppInstance appInstance = getOrCreateApplication(instance);
        //TODO error if no merchant account or amount set
        return payPalFacade.preparePayment(new PaymentRequest(
                appInstance.getSettings().getPricePerSessionInUSD(),
                "USD",
                appInstance.getSettings().getPaypalMerchantEmail(),
                returnUrl
        ));
    }
}
