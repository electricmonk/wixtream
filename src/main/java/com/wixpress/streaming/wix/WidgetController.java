package com.wixpress.streaming.wix;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import org.codehaus.jackson.JsonGenerationException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Controller
@RequestMapping("/widget")
public class WidgetController extends BaseController {
    @RequestMapping(method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam(defaultValue = "1024") Integer width) throws IOException {

        WixSignedInstance wixSignedInstance = getInstance(instance);
        AppInstance appInstance = getOrCreateApplication(wixSignedInstance);

        String displayName = appInstance.getSettings().getDisplayName();
        boolean owner = Strings.nullToEmpty(wixSignedInstance.getPermissions()).toLowerCase().contains("owner");
        WidgetModel widgetModel = new WidgetModel(instance, appInstance, owner);
        model.addAttribute("model", widgetModel);
        model.addAttribute("widgetModelJson", objectMapper.writeValueAsString(widgetModel));

        return "video-chat";

    }

    @RequestMapping(value = "/paypal", method = RequestMethod.GET)
    public String paypal(Model model,
                         @RequestParam String instance,
                         @RequestParam(defaultValue = "1024") Integer width) throws IOException {
        model.addAttribute("instanceToken", instance);

        return "paypal";

    }

}
