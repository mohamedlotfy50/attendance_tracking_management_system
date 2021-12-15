package com.example.attendance_tracking_management_system;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Attendance {
    final String userID;
    final Date date;

    public Attendance(String userID,Date date){
        this.userID=userID;
        this.date=date;
    }


    static public Attendance fromMap(HashMap map){
        return new Attendance((String) map.get("userID") ,
                (Date) map.get("date")
                );
    }
    public Map toMap(){
        Map map = new HashMap();
        map.put("userID",userID);
        map.put("date",date);
        return  map;
    }
}
