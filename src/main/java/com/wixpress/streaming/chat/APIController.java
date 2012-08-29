package com.wixpress.streaming.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
@Controller
@RequestMapping("/api/v1/chat")
public class APIController {

    @Resource
    ChatCoordinator chatCoordinator;

    @RequestMapping(value = "/request/{clientId}", method = RequestMethod.POST)
    public void requestChat(@PathVariable String clientId) {
        chatCoordinator.createSession(clientId);
    }

    @RequestMapping(value = "/fulfill/{clientId}", method = RequestMethod.POST)
    public ChatSession fulfillChat(@PathVariable String clientId) throws ChatCoordinationException {
        return chatCoordinator.fulfillSession(clientId);
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET)
    public ChatSession getSession(@PathVariable String clientId) {
        return chatCoordinator.getSession(clientId);
    }
}
