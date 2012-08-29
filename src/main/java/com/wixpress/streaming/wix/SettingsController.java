package com.wixpress.streaming.wix;

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
        WixSignedInstance wixSignedInstance = authenticationResolver.unsignInstance(WIX_SECRET, instance);
        AppInstance appAppInstance = appInstanceDao.getAppInstance(wixSignedInstance);

        if(appAppInstance == null) //new Instance created
        {
            appAppInstance = appInstanceDao.addAppInstance(wixSignedInstance);
        }

        appAppInstance.setWidth(width);
        model.addAttribute("appInstance", appAppInstance);

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

        AppInstance appAppInstance = appInstanceDao.getAppInstance(uuid);

        if(appAppInstance == null) //new Instance created
        {
            appAppInstance = appInstanceDao.addAppInstance(new WixSignedInstance(uuid, new DateTime(), null, ""));
        }

        appAppInstance.setWidth(width);
        model.addAttribute("appInstance", appAppInstance);

        return "settings";
    }
}
