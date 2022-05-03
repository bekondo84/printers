package cm.pak.canon.services;

import cm.pak.canon.beans.ConfigurationData;
import cm.pak.canon.models.Configuration;

import java.util.Optional;

public interface ConfigurationService {

    static final long ID = 1;

    Optional<Configuration> getConfiguration() ;
}
