package cm.pak.canon.models;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;


public class PrintUsageId implements Serializable {

    private String jobId ;

    private Long printerId ;

    public PrintUsageId(String jobId, Long printer) {
        this.jobId = jobId;
        this.printerId = printer;
    }

    public PrintUsageId() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Long getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Long printerId) {
        this.printerId = printerId;
    }
}
