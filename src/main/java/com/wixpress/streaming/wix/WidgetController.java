package com.wixpress.streaming.wix;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 7/1/12
 */

@Controller
@RequestMapping("/widget")
public class WidgetController extends BaseController
{
    @RequestMapping(method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam(required = false) String target,
                         @RequestParam Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(WIX_SECRET, instance);
        AppInstance appInstance = appInstanceDao.getAppInstance(wixSignedInstance);

        if(appInstance == null) //new Instance created
        {
            appInstance = appInstanceDao.addAppInstance(wixSignedInstance);
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "widget";

    }

    ///// For Testing purposes only !! /////

    @RequestMapping(value = "/widgetstandalone", method = RequestMethod.GET)
    public String widgetStandAlone(Model model,
                                   String instanceId,
                                   Integer width)
    {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(instanceId);
        } catch (Exception ignored) {}

        AppInstance appAppInstance = appInstanceDao.getAppInstance(uuid);

        if(appAppInstance == null) //new Instance created
        {
            appAppInstance = appInstanceDao.addAppInstance(new WixSignedInstance(UUID.fromString(instanceId), new DateTime(), uuid, ""));
        }

        appAppInstance.setWidth(width);
        model.addAttribute("appInstance", appAppInstance);

        return "widget";
    }


}
