package cm.pak.canon.controllers;

import cm.pak.canon.beans.ConfigurationData;
import cm.pak.canon.facades.ConfigurationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/configuration")
public class ConfigurationCtr {

    @Autowired
    private ConfigurationFacade configurationFacade;

    @GetMapping
    public String configuration(final Model model)
    {
       final ConfigurationData data = configurationFacade.getConfiguration();
       model.addAttribute("data", data);
        return "configuration";
    }

    @PostMapping
    public String configuration(final Model model, @ModelAttribute("data") ConfigurationData data){
        model.addAttribute("data", data);
        return "configuration";
    }
}
