package cm.pak.canon.facades;

import cm.pak.canon.beans.StructureData;
import cm.pak.canon.beans.UsersBean;

import java.io.IOException;
import java.util.List;

public interface StructureFacade {
    List<StructureData> getStructures(int start, int max);

    void importStructures(final UsersBean criteria) throws IOException;
}
