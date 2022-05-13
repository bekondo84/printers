package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.populator.Populator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Objects;

@Component
public class PrintUsagePopulator implements Populator<PrintUsage, PrintUsageData> {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    @Autowired
    private UserPopulator userPopulator;
    @Autowired
    private ImprimantePopulator imprimantePopulator;


    @Override
    public PrintUsageData populate(PrintUsage data) {
        final PrintUsageData printUsage = new PrintUsageData();
        printUsage.setUser(userPopulator.populate(data.getUser()));
        printUsage.setPrinter(imprimantePopulator.populate(data.getPrinter()));
        printUsage.setFilename(data.getFileName());
        printUsage.setOriginal(data.getOriginalPages());
        printUsage.setOutput(data.getOutputPages());
        if (Objects.nonNull(data.getStartTime())) {
            printUsage.setDate(SDF.format(data.getEndTime()));
        }
        return printUsage;
    }
}
