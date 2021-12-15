package com.example.attendance_tracking_management_system;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Auth auth;
    private Button btn;
    private PrefHelper pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = new Auth();
        pref = new PrefHelper(getBaseContext(),ConstName.sharedPref);
        setContentView(R.layout.activity_login);
        btn = (Button) findViewById(R.id.loginBtn) ;
        btn.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        Map me= new HashMap();
        me.put("name","mohamed");
        pref.setMap("name",me);
        pref.getMap("name");
//        auth.login("admin@atms.com","admin@123");
    }
}
