package com.wixpress.streaming.wix;

import org.codehaus.jackson.JsonGenerationException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
                           @RequestParam String instance) throws IOException {

        AppInstance appInstance = getOrCreateApplication(instance);

        SettingsModel settingsModel = new SettingsModel(appInstance.getSettings(), instance);
        model.addAttribute("model", objectMapper.writeValueAsString(settingsModel));

        return "settings";
    }

    @RequestMapping("/get")
    @ResponseBody
    public Settings getSettings(@RequestParam String instance) {
        return getOrCreateApplication(instance).getSettings();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public void getSettings(@RequestParam String instance, @RequestBody Settings settings) {
        AppInstance appInstance = getOrCreateApplication(instance);
        appInstance.setSettings(settings);
        appInstanceDao.update(appInstance);
    }

}
