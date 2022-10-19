package cm.pak.canon.services.impl;

import cm.pak.canon.services.HttpUrlConnectionService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultHttpUrlConnectionServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpUrlConnectionServiceTest.class);
    public static final String HOST = "172.16.0.190:8000";
    private HttpUrlConnectionService service ;
    private static final String url = "http://172.16.0.191:8000/rps/";

    @Before
    public void setUp() throws Exception {
        service = new DefaultHttpUrlConnectionService();
        List<String> cookies = new ArrayList<>();
    }

    @Test
    public void shouldDownlaodDataFromPrinter() throws IOException {
        List<String> data = service.downloadFileFromPrinter(url,"DSID", "1234", HttpUrlConnectionService.Type.COPY);
        assertNotNull(data);
        assertFalse(data.isEmpty());
        LOG.info(String.format("--------------------- %s", data));
    }
}