package cm.pak.canon.populator.impl;

import cm.pak.canon.beans.ConfigurationData;
import cm.pak.canon.models.Configuration;
import cm.pak.canon.populator.Populator;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationPopulator implements Populator<Configuration, ConfigurationData> {
    @Override
    public ConfigurationData populate(Configuration data) {
        final ConfigurationData config = new ConfigurationData();
        config.setId(data.getId());
        config.setPageSize(data.getPageSize());
        return config;
    }

    @Override
    public Configuration reversePopulate(ConfigurationData data) {
        final Configuration config = new Configuration();
        config.setId(data.getId());
        config.setPageSize(data.getPageSize());
        return config;
    }
}
