package com.example.attendance_tracking_management_system;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    final public String id,name,email,department,phone,role,password;


    public  UserModel(String id,String name, String email,String department,String phone,String role,String password){
        this.id=id;
        this.name=name;
        this.email=email;
        this.department=department;
        this.phone=phone;
        this.role =role;
        this.password=password;

    }

    static public UserModel fromMap(Map map){
        return new UserModel((String) map.get("id") ,
                (String) map.get("name"),
                (String) map.get("email"),
                (String) map.get("department"),
                (String) map.get("phone"),(String) map.get("role"),(String) map.get("password"));
    }
     public Map toMap(){
         Map map = new HashMap();
         map.put("id",id);
         map.put("name",name);
         map.put("email",email);
         map.put("department",department);
         map.put("phone",phone);
         map.put("role",role);
         map.put("password",password);

         return  map;
    }
    public boolean isAdmin(){
        return role == "admin";
    }
}
