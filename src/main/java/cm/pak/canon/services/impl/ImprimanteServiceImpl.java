package cm.pak.canon.services.impl;

import cm.pak.canon.models.Imprimante;
import cm.pak.canon.repertories.ImprimanteRepository;
import cm.pak.canon.services.ImprimanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImprimanteServiceImpl implements ImprimanteService {

    @Autowired
    private ImprimanteRepository repository ;

    @Override
    public void save(Imprimante printer) {
        repository.save(printer);
    }

    @Override
    public void delete(Long id) {
        final Optional<Imprimante> prt = repository.findById(id);
        prt.ifPresent(printer -> {
            repository.delete(printer);
        });
    }

    @Override
    public Imprimante getPrinterById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Imprimante> getAllPrinters() {
        List<Imprimante> results = new ArrayList<>();
        repository.findAll()
                .forEach(prt -> results.add(prt));

        return results ;
    }
}
