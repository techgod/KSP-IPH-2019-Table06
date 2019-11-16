package com.ksp.donut.uca.tasks;

public class TaskDetails {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRead_receipt() {
        return read_receipt;
    }

    public void setRead_receipt(String read_receipt) {
        this.read_receipt = read_receipt;
    }

    private String message;
    private String time;
    private String sender;
    private String receiver;
    private String read_receipt;


}
