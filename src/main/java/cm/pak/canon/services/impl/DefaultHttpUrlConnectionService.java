package cm.pak.canon.services.impl;

import cm.pak.canon.services.HttpUrlConnectionService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static cm.pak.canon.utils.Constants.*;

@Service
public class DefaultHttpUrlConnectionService implements HttpUrlConnectionService {


    @Override
    public List<String> downloadFileFromPrinter(String url, String username, String password, Type type) throws IOException {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
        final HtmlPage login =  webClient.getPage(url);
        final HtmlForm form =  login.getFormByName(LOGIN_FORM_NAME);
        final HtmlButton button = form.getButtonByName(LOGIN_BUTTON_NAME);
        final HtmlTextInput usernameTF = form.getInputByName(USERNAME_INPUT_NAME);
        usernameTF.setValueAttribute(username);
        final HtmlPasswordInput passwordTF = form.getInputByName(PASSWORD_INPUT_NAME);
        passwordTF.setValueAttribute(password);
        final HtmlSelect select = form.getSelectByName(DOMAIN_NAME_SELECT_NAME);
        select.setDefaultValue(DOMAIN_NAME_SELECT_VALUE);
        HtmlPage homepage = button.click();
        HtmlAnchor anchor = homepage.getAnchorByHref(SYSMONITOR_ANCHOR);
        final HtmlPage jobLogPage = anchor.click();
        HtmlAnchor jobLogAnchor = jobLogPage.getAnchorByText(JOB_LOG_ANCHOR);

        HtmlPage jobLobDpage = jobLogAnchor.click();
        HtmlForm jobLogDForm = jobLobDpage.getFormByName(PROP_FORM_NAME);

        if (type.equals(Type.COPY)) {
            HtmlSelect logType = jobLogDForm.getSelectByName(LOG_TYPE_SELECT_NAME);
            logType.setDefaultValue("5");
            HtmlInput logTypeBtn= jobLogDForm.getInputByName(LOG_TYPE_BUTTON_NAME);
            jobLobDpage = logTypeBtn.click();
            jobLogDForm = jobLobDpage.getFormByName(PROP_FORM_NAME);
        }
        HtmlInput storeCsv = jobLogDForm.getInputByValue(STORE_IN_CSV_FORMAT);

        UnexpectedPage downloadPage = (UnexpectedPage) storeCsv.click();
        List<String> response = new ArrayList<>();

        try (InputStream downloadedContent  = downloadPage.getInputStream()){
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(downloadedContent));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.add(inputLine);
            }
        }
       return response;
    }
}
