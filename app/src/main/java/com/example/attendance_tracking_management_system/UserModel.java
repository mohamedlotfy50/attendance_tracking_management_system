package com.example.attendance_tracking_management_system;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
<<<<<<< HEAD
    final public String id,name,email,department,phone,role,password,image;


    public  UserModel(String id,String name, String email,String department,String phone,String role,String password,String image){
=======
    final public String id,name,email,department,phone,role,password,imgUrl;


    public  UserModel(String id,String name, String email,String department,String phone,String role,String password,String imgUrl){
>>>>>>> 9d13ce8a0dcacfc82ae384c5f7c4f8943e88a27b
        this.id=id;
        this.name=name;
        this.email=email;
        this.department=department;
        this.phone=phone;
        this.role =role;
        this.password=password;
<<<<<<< HEAD
        this.image =image;

=======
        this.imgUrl = imgUrl;
>>>>>>> 9d13ce8a0dcacfc82ae384c5f7c4f8943e88a27b
    }

    static public UserModel fromMap(Map map){
        return new UserModel((String) map.get("id") ,
                (String) map.get("name"),
                (String) map.get("email"),
                (String) map.get("department"),
<<<<<<< HEAD
                (String) map.get("phone"),(String) map.get("role"),(String) map.get("password"),(String) map.get("image"));
=======
                (String) map.get("phone"),
                (String) map.get("role"),
                (String) map.get("password"),
                (String) map.get("profile_pic_URL"));
>>>>>>> 9d13ce8a0dcacfc82ae384c5f7c4f8943e88a27b
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
<<<<<<< HEAD
         map.put("image",image);
=======
         map.put("profile_pic_URL",imgUrl);
>>>>>>> 9d13ce8a0dcacfc82ae384c5f7c4f8943e88a27b

         return  map;
    }
    public boolean isAdmin(){
        return role == "admin";
    }
}
