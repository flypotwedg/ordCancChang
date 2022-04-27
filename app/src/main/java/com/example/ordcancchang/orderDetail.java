package com.example.ordcancchang;

public class orderDetail{
    String orderUID;
    String vendName;
    String apptDate;
    String apptTime;

    public orderDetail(String orderUID, String vendName, String apptDate, String apptTime)
    {
        this.orderUID=orderUID;
        this.vendName=vendName;
        this.apptDate=apptDate;
        this.apptTime=apptTime;
    }
    public void setOrderUID(String orderUID)
    {
        this.orderUID=orderUID;
    }
    public void setVendName(String vendName)
    {
        this.vendName=vendName;
    }
    public void setApptDate(String apptDate)
    {
        this.apptDate=apptDate;
    }
    public void setApptTime(String apptTime)
    {
        this.apptTime=apptTime;
    }
}
