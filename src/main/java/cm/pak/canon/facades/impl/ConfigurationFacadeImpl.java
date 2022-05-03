package cm.pak.canon.facades.impl;

import cm.pak.canon.beans.ConfigurationData;
import cm.pak.canon.facades.ConfigurationFacade;
import cm.pak.canon.models.Configuration;
import cm.pak.canon.populator.impl.ConfigurationPopulator;
import cm.pak.canon.repertories.ConfigurationRepository;
import cm.pak.canon.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigurationFacadeImpl implements ConfigurationFacade {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ConfigurationPopulator populator;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Override
    public ConfigurationData getConfiguration() {
        final Configuration config = configurationService.getConfiguration()
                .orElse(null);
        return populator.populate(Objects.nonNull(config) ? config : new Configuration());
    }

    @Override
    public void save(ConfigurationData config) {
         final Configuration data = populator.reversePopulate(config);
         configurationRepository.save(data);
    }
}
