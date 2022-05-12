package cm.pak.canon.beans;

import java.io.Serializable;
import java.util.Date;

public class Week implements Serializable {
    private String code;
    private Date from;
    private Date to ;

    public Week() {
    }

    public Week(String code, Date from, Date to) {
        this.code = code;
        this.from = from;
        this.to = to;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Week{" +
                "code='" + code + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
