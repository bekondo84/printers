package cm.pak.canon.repertories;

import cm.pak.canon.models.Structure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureRepository extends CrudRepository<Structure, String> {
}
