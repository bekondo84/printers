package cm.pak.canon.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "printUsage")
@IdClass(PrintUsageId.class)
public class PrintUsage implements Serializable {
    @Id
    private String jobId ;

    @Id
    private Long printerId ;

    @ManyToOne
    @JoinColumn(name = "prt_ID")
    private Imprimante printer ;
    private String result;
    private Date startTime ;
    private Date endTime ;
    private String departmentId;
    private String jobType ;
    private String fileName ;
    private String userName;
    private Integer originalPages;
    private Integer outputPages ;
    private String sheetCopies;
    private String endCode ;
    private String jobNote ;
    private String details ;

    public PrintUsage() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getOriginalPages() {
        return originalPages;
    }

    public void setOriginalPages(Integer originalPages) {
        this.originalPages = originalPages;
    }

    public Integer getOutputPages() {
        return outputPages;
    }

    public void setOutputPages(Integer outputPages) {
        this.outputPages = outputPages;
    }

    public String getSheetCopies() {
        return sheetCopies;
    }

    public void setSheetCopies(String sheetCopies) {
        this.sheetCopies = sheetCopies;
    }

    public String getEndCode() {
        return endCode;
    }

    public void setEndCode(String endCode) {
        this.endCode = endCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Imprimante getPrinter() {
        return printer;
    }

    public void setPrinter(Imprimante printer) {
        this.printer = printer;
    }

    public String getJobNote() {
        return jobNote;
    }

    public Long getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Long printerId) {
        this.printerId = printerId;
    }

    public void setJobNote(String jobNote) {
        this.jobNote = jobNote;
    }

    @Override
    public String toString() {
        return "PrintUsage{" +
                "jobId='" + jobId + '\'' +
                ", printer=" + printer +
                ", result='" + result + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", departmentId='" + departmentId + '\'' +
                ", jobType='" + jobType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", userName='" + userName + '\'' +
                ", originalPages=" + originalPages +
                ", outputPages=" + outputPages +
                ", sheetCopies='" + sheetCopies + '\'' +
                ", endCode='" + endCode + '\'' +
                ", jobNote='" + jobNote + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}