package cm.pak.canon.facades.impl;

import cm.pak.canon.beans.StructureData;
import cm.pak.canon.beans.UsersBean;
import cm.pak.canon.facades.StructureFacade;
import cm.pak.canon.populator.impl.StructurePopulator;
import cm.pak.canon.services.StructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StructureFacadeImpl implements StructureFacade {

    @Autowired
    private StructureService structureService;

    @Autowired
    private StructurePopulator populator;

    @Override
    public List<StructureData> getStructures(int start, int max) {
        return structureService.getStructures(start, max)
                .stream()
                .map(structure -> populator.populate(structure))
                .collect(Collectors.toList());
    }

    @Override
    public void importStructures(UsersBean criteria) throws IOException {

        if (!CollectionUtils.isEmpty(criteria.getFiles())) {
            structureService.importStructures(criteria.getFiles().get(0));
        }
    }
}
