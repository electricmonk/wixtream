package com.wixpress.streaming.wix;

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
public class WidgetController extends BaseController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam(defaultValue = "1024") Integer width) throws IOException {

        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(WIX_SECRET, instance);
        AppInstance appInstance = appInstanceDao.getAppInstance(wixSignedInstance);

        if(appInstance == null)
            appInstance = appInstanceDao.addAppInstance(wixSignedInstance);

        appInstance.setWidth(width);

        WidgetModel widgetModel = new WidgetModel(instance, appInstance, wixSignedInstance.getPermissions().contains("owner"));
        model.addAttribute("model", widgetModel);
        model.addAttribute("widgetModelJson", objectMapper.writeValueAsString(widgetModel));

        return "video-chat";

    }

}
