package cm.pak.canon.facades;

import cm.pak.canon.beans.ConfigurationData;

public interface ConfigurationFacade {
    ConfigurationData getConfiguration();
    void save(final ConfigurationData config);
}
