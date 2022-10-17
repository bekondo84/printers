package cm.pak.canon.beans;

import java.io.Serializable;
import java.util.List;

public class HttpResponseData implements Serializable {
    private String page ;
    private List<String> cookies;

    public HttpResponseData(String page, List<String> cookies) {
        this.page = page;
        this.cookies = cookies;
    }

    public HttpResponseData() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<String> getCookies() {
        return cookies;
    }

    public void setCookies(List<String> cookies) {
        this.cookies = cookies;
    }
}
