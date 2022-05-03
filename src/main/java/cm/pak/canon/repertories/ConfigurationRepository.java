package cm.pak.canon.repertories;

import cm.pak.canon.models.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {
}
