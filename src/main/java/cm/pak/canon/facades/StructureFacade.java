package cm.pak.canon.facades;

import cm.pak.canon.beans.StructureData;

import java.util.List;

public interface StructureFacade {
    List<StructureData> getStructures(int start, int max);
}
