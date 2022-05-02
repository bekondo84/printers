package cm.pak.canon.services.impl;

import cm.pak.canon.models.Structure;
import cm.pak.canon.repertories.StructureRepository;
import cm.pak.canon.services.StructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StructureServiceImpl implements StructureService {

    @Autowired
    private StructureRepository structureRepository;

    @Override
    public List<Structure> getStructures(int start, int max) {
        final List<Structure> datas = new ArrayList<>();
        structureRepository.findAll()
                .forEach(data -> datas.add(data));
        return datas ;
    }

    @Override
    public Optional<Structure> getStructure(String code) {
        return structureRepository.findById(code);
    }


}
