package com.wixpress.streaming.wix;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String settings(Model model,
                           @RequestParam String instance,
                           @RequestParam(required = false) Integer width)
    {
        AppInstance appInstance = getOrCreateApplication(instance);

        appInstance.setWidth(width);
        model.addAttribute("appInstance", appInstance);

        return "settings";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Settings getSettings(@RequestParam String instance) {
        return getOrCreateApplication(instance).getSettings();
    }

    @RequestMapping("/save")
    public void getSettings(@RequestParam String instance, @RequestBody Settings settings) {
        AppInstance appInstance = getOrCreateApplication(instance);
        appInstance.setSettings(settings);
        appInstanceDao.update(appInstance);
    }

}
