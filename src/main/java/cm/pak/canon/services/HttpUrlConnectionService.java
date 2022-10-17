package cm.pak.canon.services;

import cm.pak.canon.beans.HttpResponseData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

public interface HttpUrlConnectionService {

      HttpResponseData downloadCsv(final String url, final String durl, final String username, final String password) throws IOException;
}
