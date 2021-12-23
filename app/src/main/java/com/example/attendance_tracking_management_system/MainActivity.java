package com.example.attendance_tracking_management_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private PrefHelper pref ;
    private Handler handler = new Handler();
    private UserModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = new PrefHelper(getBaseContext(),ConstName.sharedPref);
        final boolean isLogged = pref.keyExists(ConstName.user);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

              final Intent intent;
                if(isLogged){
                    user = UserModel.fromMap(pref.getMap(ConstName.user));
                    if(user.isAdmin()){
                        intent = new Intent(MainActivity.this,HomeActivity.class);
                    }else{

                        intent = new Intent(MainActivity.this,EmployeeHomeScreen.class);

                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    intent = new Intent(MainActivity.this, LoginActivity.class);

                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        },5000);




    }
}