package cm.pak.canon.models;

import java.io.Serializable;


public class PrintUsageId implements Serializable {

    private String jobId ;

    private Long printerId ;

    private String userName ;

    public PrintUsageId(String jobId, Long printerId, String userId) {
        this.jobId = jobId;
        this.printerId = printerId;
        this.userName = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
