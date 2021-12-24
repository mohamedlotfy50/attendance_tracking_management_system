package com.example.attendance_tracking_management_system;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LeaveRequest {
   final  String userID,message;
   String response;
   int status;// the status has three values 0 for no response , 1 for acceptance and 2 for rejection
   final String date;

   public LeaveRequest(String userID,String message,String response, int status,String date){
       this.userID=userID;
       this.message=message;
       this.response=response;
       this.status=status;
       this.date=date;
   }
    static public LeaveRequest fromMap(HashMap map){
        return new LeaveRequest((String) map.get("userID") ,(String) map.get("message"),(String) map.get("response"),(int) map.get("status"),
                (String) map.get("date")
        );
    }
    public Map toMap(){
        Map map = new HashMap();
        map.put("userID",userID);
        map.put("message",message);
        map.put("response",response);
        map.put("status",status);
        map.put("date",date);
        return  map;
    }

    public  boolean hasResponse(){
       return response != "" && status != 0;
    }
    public void addResponse(boolean isAccepted,String response){
       if(isAccepted){
           this.status=1;
       }else{
           this.status=2;

       }
        this.response = response;

    }
}
