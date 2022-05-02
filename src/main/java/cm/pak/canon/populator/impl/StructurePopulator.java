package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.StructureData;
import cm.pak.canon.models.Structure;
import cm.pak.canon.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class StructurePopulator implements Populator<Structure, StructureData> {
    @Override
    public StructureData populate(Structure data) {
        final StructureData result = new StructureData();
        result.setIntitule(data.getIntitule());
        result.setCode(data.getCode());
        return result;
    }
}
