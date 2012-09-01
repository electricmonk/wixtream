package com.wixpress.streaming.chat;

import com.wixpress.streaming.wix.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

}
