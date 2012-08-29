package com.wixpress.streaming.controller;

import com.wixpress.streaming.domain.*;
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
    @RequestMapping(value = "/widget", method = RequestMethod.GET)
    public String widget(Model model,
                         @RequestParam String instance,
                         @RequestParam(required = false) String target,
                         @RequestParam Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(SECRET, instance);
        Instance appInstance = instanceDao.getAppInstance(wixSignedInstance);

        if(appInstance == null) //new Instance created
        {
            appInstance = instanceDao.addAppInstance(wixSignedInstance);
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

        Instance appInstance = instanceDao.getAppInstance(uuid);

        if(appInstance == null) //new Instance created
        {
            appInstance = instanceDao.addAppInstance(new WixSignedInstance(uuid, new DateTime(), null, ""));
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "widget";
    }


}
