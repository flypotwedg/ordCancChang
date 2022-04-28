package com.example.ordcancchang;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class orderUpd{
    String date;
    String time;
    String address;

    public orderUpd()
    {
        this.date=null;
        this.time=null;
        this.address=null;
    }

    public void setAppt(String apptDate, String apptTime)
    {
        date=apptDate;
        time=apptTime;
    }
    public void setAddress(String addr)
    {
        address=addr;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result=new HashMap<>();
        result.put("apptDate",date);
        result.put("apptTime",time);
        result.put("apptAddr",address);

        return result;
    }
}
