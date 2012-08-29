package com.wixpress.streaming.controller;

import com.wixpress.streaming.domain.Instance;
import com.wixpress.streaming.domain.WixSignedInstance;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController {


    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings(Model model,
                           @RequestParam String instance,
                           @RequestParam(required = false) Integer width)
    {
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(SECRET, instance);
        Instance appInstance = instanceDao.getAppInstance(wixSignedInstance);

        if(appInstance == null) //new Instance created
        {
            appInstance = instanceDao.addAppInstance(wixSignedInstance);
        }

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "settings";
    }

    @RequestMapping(value = "/settingsstandalone", method = RequestMethod.GET)
    public String settingsStandAlone(Model model,
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

        return "settings";
    }
}
