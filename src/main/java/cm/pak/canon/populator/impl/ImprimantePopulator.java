package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.ImprimanteData;
import cm.pak.canon.models.Imprimante;
import cm.pak.canon.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class ImprimantePopulator implements Populator<Imprimante, ImprimanteData> {
    @Override
    public ImprimanteData populate(Imprimante data) {
        final ImprimanteData printer = new ImprimanteData();
        printer.setId(data.getId());
        printer.setIpAdress(data.getIpAdress());
        printer.setLocalisation(data.getLocalisation());
        printer.setModel(data.getModel());
        printer.setName(data.getName());
        return printer;
    }
}
