package cm.pak.canon.services.impl;

import cm.pak.canon.beans.ImportBean;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.repertories.PrintUsageRepository;
import cm.pak.canon.services.CSVService;
import cm.pak.canon.services.ImprimanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

    @Autowired
   private ImprimanteService printerService ;

    @Autowired
    private PrintUsageRepository repository ;

    @Override
    public void processCsv(ImportBean data) throws IOException {
       final Imprimante printer = printerService.getPrinterById(data.getPrinter());
       final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM yyyy hh:mm:ss");
        if (CollectionUtils.isEmpty(data.getFiles())) {
            return;
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(data.getFiles().get(0).getInputStream(), "UTF-8"));

        reader.lines().skip(1).forEach(line -> {
            String[] cols= line.split(",");

            if (cols.length == 14) {

                final PrintUsage usage = new PrintUsage();
                usage.setPrinter(printer);
                usage.setPrinterId(printer.getId());
                usage.setJobId(cols[0]);
                usage.setResult(clean(cols[1]));
                try {
                    usage.setStartTime(formatter.parse(clean(cols[2])));
                    usage.setEndTime(formatter.parse(clean(cols[3])));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                usage.setDepartmentId(clean(cols[4]));
                usage.setJobType(clean(cols[5]));
                usage.setFileName(clean(cols[6]));
                usage.setUserName(clean(cols[7]));
                usage.setOriginalPages(Integer.valueOf(clean(cols[8])));
                usage.setOutputPages(Integer.valueOf(clean(cols[9])));
                usage.setSheetCopies(clean(cols[10]));
                usage.setEndCode(clean(cols[11]));
                usage.setJobType(clean(cols[12]));
                usage.setDetails(clean(cols[13]));
                //System.out.println("Usage before save : "+usage+" output : "+clean(cols[9])+" value ::: "+Integer.valueOf(clean(cols[9])));
                repository.save(usage);
            }
        });
    }

    private String clean(final String value) {
        return  value. replaceAll("^\"|\"$", "");
    }
}
