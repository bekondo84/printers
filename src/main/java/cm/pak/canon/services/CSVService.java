package cm.pak.canon.services;

import cm.pak.canon.beans.ImportBean;
import cm.pak.canon.models.PrintUsage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CSVService {

    void processCsv(final ImportBean data) throws IOException;

    <T>List<T>  parseCsv(final InputStream is, final String[] headers, T type) throws UnsupportedEncodingException;
}
