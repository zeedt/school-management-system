package com.zeed.assignment.sms.apimodels;


import java.util.List;

public class ResultUploadResponseModel {

    private List<String> failureReasons;

    private List<Integer> failureRows;

    private Integer successfulCount;

    private Integer failureCount;

    private String message;

    public List<String> getFailureReasons() {
        return failureReasons;
    }

    public void setFailureReasons(List<String> failureReasons) {
        this.failureReasons = failureReasons;
    }

    public List<Integer> getFailureRows() {
        return failureRows;
    }

    public void setFailureRows(List<Integer> failureRows) {
        this.failureRows = failureRows;
    }

    public Integer getSuccessfulCount() {
        return successfulCount;
    }

    public void setSuccessfulCount(Integer successfulCount) {
        this.successfulCount = successfulCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
