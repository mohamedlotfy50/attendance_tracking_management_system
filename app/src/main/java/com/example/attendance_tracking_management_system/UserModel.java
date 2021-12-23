package com.example.attendance_tracking_management_system;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

<<<<<<< HEAD
public class UserModel {

    final public String id,name,email,department,phone,role,password,imgUrl;
=======
public class UserModel implements Serializable {
    public String id,name,email,department,phone,role,password,imgUrl;
    public UserModel(){}


    public String getId() {
        return id;
    }
>>>>>>> e0593787673ea2c033359908f088b5e28094a358

    public String getName() {
        return name;
    }

<<<<<<< HEAD
    public  UserModel(String id,String name, String email,String department,String phone,String role,String password,String imgUrl){
=======
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

    public String getImgUrl() {
        return imgUrl;
    }


    public  UserModel(String id, String name, String email, String department, String phone, String role, String password, String imgUrl){
>>>>>>> e0593787673ea2c033359908f088b5e28094a358
        this.id=id;
        this.name=name;
        this.email=email;
        this.department=department;
        this.phone=phone;
        this.role =role;
        this.password=password;
<<<<<<< HEAD

=======
>>>>>>> e0593787673ea2c033359908f088b5e28094a358
        this.imgUrl = imgUrl;
    }


    static public UserModel fromMap(Map map){
        return new UserModel((String) map.get("id") ,
                (String) map.get("name"),
                (String) map.get("email"),
                (String) map.get("department"),
<<<<<<< HEAD

                (String) map.get("phone"),
                (String) map.get("role"),
                (String) map.get("password"),
                (String) map.get("profile_pic_URL"));
=======
                (String) map.get("phone"),
                (String) map.get("role"),
                (String) map.get("password"),
                (String) map.get("imgUrl"));
>>>>>>> e0593787673ea2c033359908f088b5e28094a358
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

=======
>>>>>>> e0593787673ea2c033359908f088b5e28094a358
         map.put("profile_pic_URL",imgUrl);

         return  map;
    }
    public boolean isAdmin(){
        return role == "admin";
    }
}
