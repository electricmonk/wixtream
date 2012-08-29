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
        AppInstance appAppInstance = getOrCreateApplication(instance);

        appAppInstance.setWidth(width);
        model.addAttribute("appInstance", appAppInstance);

        return "settings";
    }

}
