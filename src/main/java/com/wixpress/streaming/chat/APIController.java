package com.wixpress.streaming.chat;

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

    @ResponseBody
    @RequestMapping(value = "/subscribe/{clientId}")
    public void requestChat(@PathVariable String clientId, @RequestParam String instance) throws ChatCoordinationException {
        chatCoordinator.createSession(getInstanceId(instance), clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/subscriber-status/{clientId}", method = RequestMethod.GET)
    public ChatSession getSession(@PathVariable String clientId, @RequestParam String instance) {
        return chatCoordinator.getSession(getInstanceId(instance), clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/accept-subscriber/{clientId}")
    public ChatSession fulfillChat(@PathVariable String clientId, @RequestParam String instance) throws ChatCoordinationException {
        return chatCoordinator.fulfillSession(getInstanceId(instance), clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/subscriber-list", method = RequestMethod.GET)
    public List<ChatSession> waitingSessions(@RequestParam String instance) {
        return chatCoordinator.getSessions(getInstanceId(instance));
    }
}
