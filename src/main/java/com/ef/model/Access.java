package com.ef.model;


import java.util.Date;

public class Access {
    Date date;
    String IPAddress;
    String request;
    int status;
    String userAgent;

    public Access() {
    }

    public Access(Date date, String IPAddress, String request, int status, String userAgent) {
        this.date = date;
        this.IPAddress = IPAddress;
        this.request = request;
        this.status = status;
        this.userAgent = userAgent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date dt_access) {
        this.date = dt_access;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "ipAddess: " + IPAddress ;
    }
}
