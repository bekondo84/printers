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
import cm.pak.canon.services.ExcelRowService;
import cm.pak.canon.services.ExcelService;
import cm.pak.canon.services.ImprimanteService;
import cm.pak.canon.services.impl.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
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

    @Autowired
    private UsersRowService usersRowService;

    @Autowired
    private UserWithDateRowService userWithDateRowService;

    @Autowired
    private ImprimanteRowService imprimanteRowService;

    @Autowired
    private StructureRowService structureRowService;

    @Autowired
    private UserComparativeanalyseRowService userComparativeanalyseRowService;

    @Autowired
    private  StructureComparativeanalyseRowService structureComparativeanalyseRowService;

    @Autowired
    private ImprimanteComparativeanalyseRowService imprimanteComparativeanalyseRowService;

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
            processGroupingByAfectation(printUsageFacade.getPrinterForUsers(search.getFrom(), search.getTo()), model, search);
            return "/reportingUsers";
        }

        if (search.getGroupBy() == 2) {
            processGroupingByAfectation(printUsageFacade.getPrinterForPrinters(search.getFrom(), search.getTo()), model, search);
            return "/reportingPrinters";
        }

        if (search.getGroupBy() == 3) {
            processGroupingByAfectation(printUsageFacade.printGroupbyAffectation(search.getFrom(), search.getTo()), model, search);
            return "/reportingAffectations";
        }
        processGroupingByAfectation(printUsageFacade.printGroupbyStructure(search.getFrom(), search.getTo()), model, search);
        return "/reportingStructures";
    }

    @PostMapping(value = "/reporting", params = "action=extract")
    public void extractSynthese(final HttpServletResponse response, final Model model, @ModelAttribute("search") SearchBean search) throws ParseException, NoSuchFieldException, IllegalAccessException, IOException {
        if (search.getGroupBy() == 1) {
            final List<PrintUsageData> datas = printUsageFacade.getPrinterForUsers(search.getFrom(), search.getTo());
            processGroupingByAfectation(datas, model, search);
            ByteArrayInputStream stream = excelService.excelExpoter(new String[]{"ID", "NOM", "SERVICE", "STRUCTURE", "TOTAL IMPRESSIONS"}, datas, userRowService);
            exporter(response, stream, "printSynthese.xlsx");
        }
        if (search.getGroupBy() == 2) {
            final List<PrintUsageData> datas =printUsageFacade.getPrinterForPrinters(search.getFrom(), search.getTo());
            processGroupingByAfectation(datas, model, search);
            ByteArrayInputStream stream = excelService.excelExpoter(new String[]{"IMPRIMANTE", "NOM", "TOTAL IMPRESSIONS"}, datas, imprimanteRowService);
            exporter(response, stream, "printSynthese.xlsx");
        }

        if (search.getGroupBy() == 3) {
            final List<PrintUsageData> datas = printUsageFacade.printGroupbyAffectation(search.getFrom(), search.getTo());
            processGroupingByAfectation(datas, model, search);
            final ByteArrayInputStream stream = excelService.excelExpoter(new String[]{"CODE", "INTITULE", "TOTAL IMPRESSIONS"}, datas, structureRowService);
            exporter(response, stream, "printSynthese.xlsx");
        }
        final List<PrintUsageData> datas = printUsageFacade.printGroupbyStructure(search.getFrom(), search.getTo());
        processGroupingByAfectation(datas, model, search);
        ByteArrayInputStream stream = excelService.excelExpoter(new String[]{"CODE", "INTITULE", "TOTAL IMPRESSIONS"}, datas, structureRowService);
        exporter(response, stream, "printSynthese.xlsx");
    }

    private void exporter(HttpServletResponse response, ByteArrayInputStream stream, String filename) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s", filename));
        IOUtils.copy(stream, response.getOutputStream());
    }

    private void processGroupingByAfectation(List<PrintUsageData> printUsageFacade, Model model, SearchBean search) throws ParseException {
        final List<PrintUsageData> datas = printUsageFacade;
        model.addAttribute("search", search);
        model.addAttribute("datas", datas);
    }

    @GetMapping("/reporting-str")
    public String reportingStructure(final Model model) throws ParseException {
        final List<PrintUsageData> datas = new ArrayList<>();
        model.addAttribute("datas", datas);
        final SearchBean search = new SearchBean();
        model.addAttribute("search", search);
        return "/reportingPrintusageForStructure";
    }
    @PostMapping(value = "/reporting-str", params = "action=find")
    public String reportingStructure(final Model model, @ModelAttribute("search") final SearchBean search) throws ParseException {
        reportingPerStructure(search.getCodeStructure(), printUsageFacade.getPrintUsageForStructureForDates(search.getFrom(), search.getTo(), search.getCodeStructure()), model, search);
        return "/reportingPrintusageForStructure";
    }

    @PostMapping(value = "/reporting-str", params = "action=extract")
    public void extractStructure(final HttpServletResponse response, final Model model, @ModelAttribute("search") final SearchBean search) throws ParseException, IOException, NoSuchFieldException, IllegalAccessException {
        List<PrintUsageData> datas = printUsageFacade.getPrintUsageForStructureForDates(search.getFrom(), search.getTo(), search.getCodeStructure());
        reportingPerStructure(search.getCodeStructure(), datas, model, search);
        final ByteArrayInputStream stream = excelService.excelExpoter(new String[]{"ID", "NOM", "TOTAL IMPRESSIONS"}, datas, usersRowService);
        exporter(response, stream, "ImpressionsParStructure.xlsx");
    }

    private void reportingPerStructure(String search, List<PrintUsageData> printUsageFacade, Model model, SearchBean search1) throws ParseException {
        if (StringUtils.hasLength(search)) {
            final List<PrintUsageData> datas = printUsageFacade;
            model.addAttribute("datas", datas);
            //final SearchBean search = new SearchBean();
        }
        model.addAttribute("search", search1);
    }

    @GetMapping("/reporting-indiv")
    public String reportingIndividuel(final Model model) {
        final List<PrintUsageData> datas = new ArrayList<>();
        model.addAttribute("datas", datas);
        final SearchBean search = new SearchBean();
        model.addAttribute("search", search);
        return "/reportingPrintusageIndividual";
    }

    @PostMapping(value = "/reporting-indiv", params = "action=find")
    public String reportingIndividuel(final Model model, @ModelAttribute("search") final SearchBean search) throws ParseException {
        reportingPerStructure(search.getUserId(), printUsageFacade.getUserPrintUsageResume(search.getFrom(), search.getTo(), search.getUserId()), model, search);
        return "/reportingPrintusageIndividual";
    }

    @PostMapping(value = "/reporting-indiv", params = "action=extract")
    public void extractIndividuel(final HttpServletResponse response, final Model model, @ModelAttribute("search") final SearchBean search) throws ParseException, IOException, NoSuchFieldException, IllegalAccessException {
        final List<PrintUsageData> datas = printUsageFacade.getUserPrintUsageResume(search.getFrom(), search.getTo(), search.getUserId());
        reportingPerStructure(search.getUserId(),datas , model, search);
        ByteArrayInputStream stream = excelService.excelExpoter(new String[]{"ID", "NOM", "DATE", "TOTAL IMPRESSIONS"}, datas, userWithDateRowService);
        exporter(response, stream, "printReport.xlsx");
    }

    @GetMapping("/reporting-anal-comp")
    public String analyseComparative(final Model model) {
         final SearchBean search = new SearchBean();
         model.addAttribute("search", search);
        model.addAttribute("headers", new ArrayList<>());
        model.addAttribute("datas", new ArrayList<AnalyseComparativeData>());
        return "/comparativeAnalyzeUsers";
    }

    @PostMapping(value = "/reporting-anal-comp", params = "action=find")
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

    @PostMapping(value = "/reporting-anal-comp", params = "action=extract")
    public void analyseComparativeExtract(final HttpServletResponse response, final Model model, @ModelAttribute("search") SearchBean search) throws ParseException, IOException, NoSuchFieldException, IllegalAccessException {
        final List<String> headers = analyseComparativeFacade.headers(search.getCycle(), search.getFrom(), search.getTo());
        final List<AnalyseComparativeData> datas = analyseComparativeFacade.analyseComparative(search.getFrom(), search.getTo(), Integer.toString(search.getGroupBy()), search.getCycle());
        model.addAttribute("headers", headers);
        model.addAttribute("datas", datas);
        ExcelRowService service =  null ;
        if (search.getGroupBy() == 2) {
            service = imprimanteComparativeanalyseRowService ;
        } else if(search.getGroupBy() == 3 || search.getGroupBy() == 4){
            service = structureComparativeanalyseRowService;
        } else {
            service = userComparativeanalyseRowService ;
        }
        headers.addAll(0, Arrays.asList(" ", " "));
        final ByteArrayInputStream stream = excelService.excelExpoter(headers.toArray(new String[headers.size()]), datas, service);
        exporter(response, stream, "comparativeAnalyze.xlsx");
    }
}
