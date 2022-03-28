package cm.pak.canon.controllers;

import cm.pak.canon.models.Imprimante;
import cm.pak.canon.services.ImprimanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/imprimante")
public class ImprimanteCtr {

    @Autowired
    private ImprimanteService printerService ;

    @GetMapping
    public String imprimantes(final Model model, @RequestParam(value = "id", required = false) Long id ) {
        Imprimante prt = new Imprimante();

        if (Objects.nonNull(id)) {
            prt = printerService.getPrinterById(id);
        }
        model.addAttribute("prt", prt);
        return "imprimante";
    }

    @PostMapping
    public String imprimant(final Model model, @ModelAttribute("prt") final Imprimante prt) {
        printerService.save(prt);
        return "redirect:/imprimantes";
    }

    @GetMapping("/{id}")
    public String imprimante2(final Model model, @PathVariable("id") Long id) {
        printerService.delete(id);
        return "redirect:/imprimantes";
    }

}
