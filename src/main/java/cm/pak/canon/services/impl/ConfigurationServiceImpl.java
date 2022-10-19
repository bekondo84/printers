package cm.pak.canon.services.impl;

import cm.pak.canon.beans.ConfigurationData;
import cm.pak.canon.models.Configuration;
import cm.pak.canon.repertories.ConfigurationRepository;
import cm.pak.canon.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Value("${printer.username}")
    private String username ;
    @Value("${printer.password}")
    private String password ;
    @Value("${collector.task.delai}")
    private String cronExp ;

    @Override
    public Optional<Configuration> getConfiguration() {
        final Configuration config =  configurationRepository
                .findById(ConfigurationService.ID)
                .orElse(new Configuration());
        config.setCronExp(cronExp);
        config.setPassword(password);
        config.setUsername(username);
        return Optional.of(config);
    }
}
