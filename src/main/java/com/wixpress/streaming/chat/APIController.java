package com.wixpress.streaming.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping(value = "/request/{clientId}")
    public void requestChat(@PathVariable String clientId) {
        chatCoordinator.createSession(clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/fulfill/{clientId}")
    public ChatSession fulfillChat(@PathVariable String clientId) throws ChatCoordinationException {
        return chatCoordinator.fulfillSession(clientId);
    }

    @ResponseBody
    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET)
    public ChatSession getSession(@PathVariable String clientId) {
        return chatCoordinator.getSession(clientId);
    }
}
