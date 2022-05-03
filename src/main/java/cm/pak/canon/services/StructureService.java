package cm.pak.canon.services;

import cm.pak.canon.models.Structure;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StructureService {
    List<Structure> getStructures(int start, int max) ;

    Optional<Structure> getStructure(final String code) ;

    void importStructures(final MultipartFile mpf) throws IOException;
}
