package cm.pak.canon.services;

import cm.pak.canon.beans.ImportBean;
import cm.pak.canon.models.PrintUsage;

import java.io.IOException;
import java.util.List;

public interface CSVService {

    public void processCsv(final ImportBean data) throws IOException;
}
