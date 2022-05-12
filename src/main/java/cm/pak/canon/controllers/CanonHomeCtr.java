package cm.pak.canon.controllers;

import cm.pak.canon.beans.AnalyseComparativeData;
import cm.pak.canon.beans.ImportBean;
import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.beans.SearchBean;
import cm.pak.canon.facades.AnalyseComparativeFacade;
import cm.pak.canon.facades.PrintUsageFacade;
import cm.pak.canon.facades.StructureFacade;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.services.CSVService;
import cm.pak.canon.services.ExcelService;
import cm.pak.canon.services.ImprimanteService;
import cm.pak.canon.services.impl.UserRowService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CanonHomeCtr {

    private static final Logger LOG = LoggerFactory.getLogger(CanonHomeCtr.class);

    @Autowired
    private ImprimanteService printerService ;

    @Autowired
    private CSVService csvService ;

    @Autowired
    private PrintUsageFacade printUsageFacade;

    @Autowired
    private StructureFacade structureFacade;

    @Autowired
    private AnalyseComparativeFacade analyseComparativeFacade;

    @Autowired
    private ExcelService excelService ;

    @Autowired
    private UserRowService userRowService;

    @GetMapping
    public String homePage(final Model model) {
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
        //LOG.info(String.format("ImportData Recieve : %s", data));
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

    @PostMapping(value = "/reporting", params = "action=find")
    public String reporting(final Model model, @ModelAttribute("searh") final SearchBean search) throws ParseException {

        if (search.getGroupBy() == 1) {
            final List<PrintUsageData> datas =printUsageFacade.getPrinterForUsers(search.getFrom(), search.getTo());
            model.addAttribute("search", search) ;
            model.addAttribute("datas", datas);
            return "/reportingUsers";
        }

        if (search.getGroupBy() == 2) {
            final List<PrintUsageData> datas = printUsageFacade.getPrinterForPrinters(search.getFrom(), search.getTo());
            model.addAttribute("search", search);
            model.addAttribute("datas", datas);
            return "/reportingPrinters";
        }

        if (search.getGroupBy() == 3) {
           final List<PrintUsageData> datas = printUsageFacade.printGroupbyAffectation(search.getFrom(), search.getTo());
            model.addAttribute("search", search);
            model.addAttribute("datas", datas);
            return "/reportingAffectations";
        }
        final List<PrintUsageData> datas = printUsageFacade.printGroupbyStructure(search.getFrom(), search.getTo());
        model.addAttribute("search", search);
        model.addAttribute("datas", datas);
        return "/reportingStructures";
    }

    @PostMapping(value = "/reporting", params = "action=extract")
    public String extractSynthese(final Model model, @ModelAttribute("search") SearchBean search) throws ParseException, NoSuchFieldException, IllegalAccessException, IOException {
        LOG.info("INSIDE EXTRACT CONTROLLER");
        if (search.getGroupBy() == 1) {
            final List<PrintUsageData> datas =printUsageFacade.getPrinterForUsers(search.getFrom(), search.getTo());
            model.addAttribute("search", search) ;
            model.addAttribute("datas", datas);
            final Workbook workbook = excelService.createExcel(new String[]{"ID", "Nom", "Service", "Structure", "Total impression"}, datas, userRowService);
            File currDir = new File(".");
            String path = currDir.getAbsolutePath();
            String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

            FileOutputStream outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
            return "/reportingUsers";
        }

        if (search.getGroupBy() == 2) {
            final List<PrintUsageData> datas = printUsageFacade.getPrinterForPrinters(search.getFrom(), search.getTo());
            model.addAttribute("search", search);
            model.addAttribute("datas", datas);
            return "/reportingPrinters";
        }

        if (search.getGroupBy() == 3) {
            final List<PrintUsageData> datas = printUsageFacade.printGroupbyAffectation(search.getFrom(), search.getTo());
            model.addAttribute("search", search);
            model.addAttribute("datas", datas);
            return "/reportingAffectations";
        }
        final List<PrintUsageData> datas = printUsageFacade.printGroupbyStructure(search.getFrom(), search.getTo());
        model.addAttribute("search", search);
        model.addAttribute("datas", datas);
        return "/reportingStructures";
    }

    @GetMapping("/reporting-str")
    public String reportingStructure(final Model model) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        model.addAttribute("datas", datas);
        final SearchBean search = new SearchBean();
        model.addAttribute("search", search);
        return "/reportingPrintusageForStructure";
    }
    @PostMapping("/reporting-str")
    public String reportingStructure(final Model model, @ModelAttribute("searh") final SearchBean search) throws ParseException {
        if (StringUtils.hasLength(search.getCodeStructure())) {
            final List<PrintUsageData> datas = printUsageFacade.getPrintUsageForStructureForDates(search.getFrom(), search.getTo(), search.getCodeStructure());
            model.addAttribute("datas", datas);
            //final SearchBean search = new SearchBean();
        }
        model.addAttribute("search", search);
        return "/reportingPrintusageForStructure";
    }

    @GetMapping("/reporting-indiv")
    public String reportingIndividuel(final Model model) {
        final List<PrintUsageData> datas = new ArrayList<>();
        model.addAttribute("datas", datas);
        final SearchBean search = new SearchBean();
        model.addAttribute("search", search);
        return "/reportingPrintusageIndividual";
    }

    @PostMapping("/reporting-indiv")
    public String reportingIndividuel(final Model model, @ModelAttribute("search") final SearchBean search) throws ParseException {
        if (StringUtils.hasLength(search.getUserId())) {
            final List<PrintUsageData> datas = printUsageFacade.getUserPrintUsageResume(search.getFrom(), search.getTo(), search.getUserId());
            model.addAttribute("datas", datas);
        }
        model.addAttribute("search", search);
        return "/reportingPrintusageIndividual";
    }

    @GetMapping("/reporting-anal-comp")
    public String analyseComparative(final Model model) {
         final SearchBean search = new SearchBean();
         model.addAttribute("search", search);
        model.addAttribute("headers", new ArrayList<>());
        model.addAttribute("datas", new ArrayList<AnalyseComparativeData>());
        return "/comparativeAnalyzeUsers";
    }

    @PostMapping("/reporting-anal-comp")
    public String analyseComparative(final Model model, @ModelAttribute("search") SearchBean search) throws ParseException {
        //LOG.info(String.format("Headers : %s", analyseComparativeFacade.headers(search.getCycle(), search.getFrom(), search.getTo())));
        model.addAttribute("headers", analyseComparativeFacade.headers(search.getCycle(), search.getFrom(), search.getTo()));
        model.addAttribute("datas", analyseComparativeFacade.analyseComparative(search.getFrom(), search.getTo(), Integer.toString(search.getGroupBy()), search.getCycle()));

        if (search.getGroupBy() == 2) {
            return "/comparativeAnalyzePrinters";
        }
        if(search.getGroupBy() == 3 || search.getGroupBy() == 4){
            return  "comparativeAnalyzeStructures";
        }
        return "/comparativeAnalyzeUsers";
    }

}
