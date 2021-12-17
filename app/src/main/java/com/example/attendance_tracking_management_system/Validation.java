package com.example.attendance_tracking_management_system;

import java.util.regex.Pattern;

public class Validation {

    public static boolean isValidEmail(String email){
        if(Pattern.matches("^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$", email)){
            return true;
        }
        return  false;
    }

    public static boolean isValidPassword(String password){
        if(password.length()>=6){
            return true;
        }
        return  false;
    }



}
