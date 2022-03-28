package cm.pak.canon.controllers;

import cm.pak.canon.beans.ImportBean;
import cm.pak.canon.beans.ResultBean;
import cm.pak.canon.beans.SearchBean;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.services.CSVService;
import cm.pak.canon.services.ImprimanteService;
import cm.pak.canon.services.PrintUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CanonHomeCtr {

    @Autowired
    private ImprimanteService printerService ;

    @Autowired
    private CSVService csvService ;

    @Autowired
    private PrintUsageService printUsageService;

    @GetMapping
    public String loginPage(Model model) {
        return "canonHomePage" ;
    }

    @GetMapping("/imprimantes")
    public String getPrinters(final Model model) {
        List<Imprimante> printers = printerService.getAllPrinters();
        model.addAttribute("printers", printers);
        return "imprimantes";
    }

    @GetMapping("/importer")
    public String importer(final Model model) {
        final List<Imprimante> printers = printerService.getAllPrinters();
        model.addAttribute("printers", printers);
        final ImportBean data = new ImportBean();
        model.addAttribute("data", data);
        return "importer";
    }

    @PostMapping("/importer")
    public String importer(final Model model, @ModelAttribute ImportBean data) throws IOException {
        csvService.processCsv(data);
        final ImportBean data2 = new ImportBean();
        model.addAttribute("data", data2);
        return "redirect:/importer";
    }

    @GetMapping("/reporting")
    public String reporting(final Model model) {
        final SearchBean search = new SearchBean();
        final List<Imprimante> printers = printerService.getAllPrinters();
        model.addAttribute("printers", printers);
        model.addAttribute("search", search);
        return  "/reporting";
    }

    @PostMapping("/reporting")
    public String reporting(final Model model, @ModelAttribute("searh") final SearchBean search) throws ParseException {
        if (search.getGroupBy() == 1) {
            final List<ResultBean> datas =printUsageService.getPrinterForUsers(search.getFrom(), search.getTo());
            model.addAttribute("search", search) ;
            model.addAttribute("datas", datas);
            return "/reportingUsers";
        }
        final List<ResultBean> datas =printUsageService.getPrinterForPrinters(search.getFrom(), search.getTo());
        model.addAttribute("search", search) ;
        model.addAttribute("datas", datas);
        return "/reportingPrinters";
    }
}
