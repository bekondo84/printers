package cm.pak.canon.services.impl;

import cm.pak.canon.beans.ConfigurationData;
import cm.pak.canon.models.Configuration;
import cm.pak.canon.repertories.ConfigurationRepository;
import cm.pak.canon.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Override
    public Optional<Configuration> getConfiguration() {
        return configurationRepository.findById(ConfigurationService.ID);
    }
}
