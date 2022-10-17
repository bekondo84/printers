package cm.pak.canon.services.impl;

import cm.pak.canon.beans.HttpResponseData;
import cm.pak.canon.services.HttpUrlConnectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DefaultHttpUrlConnectionServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpUrlConnectionServiceTest.class);
    public static final String HOST = "172.16.0.190:8000";
    private HttpUrlConnectionService service ;
    private static final String url = "http://172.16.0.190:8000/rps/";
    private static final String durl = "http://172.16.0.190:8000/rps/pprint.csv?LogType=0&Flag=Csv_Data";
    private List<String> cookies ;
    @Before
    public void setUp() throws Exception {
        service = new DefaultHttpUrlConnectionService();
        cookies = new ArrayList<>();
    }

    @Test
    public void shouldConnect() throws IOException {
        HttpResponseData data = service.downloadCsv(url, durl, "DSID", "1234");
        assertNotNull(data);
        LOG.info(String.format("--------------------- %s", data.getPage()));
    }
}