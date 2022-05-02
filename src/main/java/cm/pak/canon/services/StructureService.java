package cm.pak.canon.services;

import cm.pak.canon.models.Structure;

import java.util.List;
import java.util.Optional;

public interface StructureService {
    List<Structure> getStructures(int start, int max) ;

    Optional<Structure> getStructure(final String code) ;
}
