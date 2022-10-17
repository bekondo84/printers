package cm.pak.canon.services.impl;

import cm.pak.canon.beans.HttpResponseData;
import cm.pak.canon.services.HttpUrlConnectionService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class DefaultHttpUrlConnectionService implements HttpUrlConnectionService {
    private final String USER_AGENT = "Mozilla/5.0";


    @Override
    public HttpResponseData downloadCsv(String url, final String durl, String username, String password) throws IOException {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
        final HtmlPage login =  webClient.getPage(url);
        final HtmlForm form =  login.getFormByName("login");
        final HtmlButton button = form.getButtonByName("LoginButton");
        final HtmlTextInput usernameTF = form.getInputByName("USERNAME");
        usernameTF.setValueAttribute(username);
        final HtmlPasswordInput passwordTF = form.getInputByName("PASSWORD_T");
        passwordTF.setValueAttribute(password);
        final HtmlSelect select = form.getSelectByName("domainname");
        select.setDefaultValue("localhost");
        HtmlPage homepage = button.click();
        HtmlAnchor anchor = homepage.getAnchorByHref("/sysmonitor");
        final HtmlPage jobLogPage = anchor.click();
        HtmlAnchor jobLogAnchor = jobLogPage.getAnchorByText("Job Log");

        HtmlPage jobLobDpage = jobLogAnchor.click();
        HtmlForm jobLogDForm = jobLobDpage.getFormByName("PROP_FORM");
        HtmlInput storeCsv = jobLogDForm.getInputByValue("Store in CSV Format... ");

        UnexpectedPage downloadPage = (UnexpectedPage) storeCsv.click();
        StringBuffer response = new StringBuffer();

        try (InputStream downloadedContent  = downloadPage.getInputStream()){
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(downloadedContent));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        final HttpResponseData data = new HttpResponseData(response.toString(), null);
        return data;
    }
}
