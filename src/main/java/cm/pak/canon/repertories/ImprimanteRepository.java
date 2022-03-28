package cm.pak.canon.repertories;

import cm.pak.canon.models.Imprimante;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImprimanteRepository extends CrudRepository<Imprimante, Long> {

    public Imprimante findByIpAdress(final String ipAdress);
}
