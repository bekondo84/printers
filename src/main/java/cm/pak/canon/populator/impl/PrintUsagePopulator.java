package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.PrintUsageData;
import cm.pak.canon.models.PrintUsage;
import cm.pak.canon.populator.Populator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrintUsagePopulator implements Populator<PrintUsage, PrintUsageData> {

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private ImprimantePopulator imprimantePopulator;

    @Override
    public PrintUsageData populate(PrintUsage data) {
        final PrintUsageData printUsage = new PrintUsageData();
        printUsage.setUser(userPopulator.populate(data.getUser()));
        printUsage.setPrinter(imprimantePopulator.populate(data.getPrinter()));
        printUsage.setOriginal(data.getOriginalPages());
        printUsage.setOutput(data.getOutputPages());
        return printUsage;
    }
}
