package com.whiskas68.cloud.event;


public class JobEvent {

    private String itemCode;

    private String jobName;

    public JobEvent(){}

    public JobEvent(String itemCode, String jobName) {
        this.itemCode = itemCode;
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobEvent{" +
                "itemCode='" + itemCode + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
