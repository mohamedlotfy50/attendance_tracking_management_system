package com.example.attendance_tracking_management_system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class UserModel implements Serializable {
    final public String id,name,email,department,phone,role,password,imgUrl;
    //public UserModel(){}

/*    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getImgUrl() { return imgUrl; }*/


    public  UserModel(String id, String name, String email, String department, String phone, String role, String password, String imgUrl){
        this.id=id;
        this.name=name;
        this.email=email;
        this.department=department;
        this.phone=phone;
        this.role =role;
        this.password=password;
        this.imgUrl = imgUrl;
    }
    public static UserModel init(){
        return new UserModel(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "");
    }


    static public UserModel fromMap(Map map){
        return new UserModel((String) map.get("id") ,
                (String) map.get("name"),
                (String) map.get("email"),
                (String) map.get("department"),
                (String) map.get("phone"),
                (String) map.get("role"),
                (String) map.get("password"),
                (String) map.get("profile_pic_URL"));

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
         map.put("imgUrl",imgUrl);

         return  map;
    }
    public boolean isAdmin(){
        return role.equals("Admin");
    }
}
