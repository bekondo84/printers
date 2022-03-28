package cm.pak.canon.beans;

import java.io.Serializable;

public class SearchBean implements Serializable {
   private String from ;
   private String to ;
   private int groupBy ;

    public SearchBean() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(int groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", groupBy=" + groupBy +
                '}';
    }
}
