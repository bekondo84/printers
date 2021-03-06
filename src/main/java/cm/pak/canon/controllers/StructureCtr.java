package cm.pak.canon.controllers;

import cm.pak.canon.beans.UsersBean;
import cm.pak.canon.facades.StructureFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/structures")
public class StructureCtr {

    @Autowired
    private StructureFacade structureFacade ;

    @GetMapping
    public String structure(final Model model) {
        final UsersBean crieria = new UsersBean();
        model.addAttribute("criteria", crieria);
        model.addAttribute("datas", structureFacade.getStructures(0, -1));
        return "structures";
    }

    @PostMapping
    public String structures(final Model model, @ModelAttribute("criteria") UsersBean criteria) throws IOException {
         structureFacade.importStructures(criteria);
        model.addAttribute("criteria", criteria);
        model.addAttribute("datas", structureFacade.getStructures(0, -1));
        return "structures";
    }
}
