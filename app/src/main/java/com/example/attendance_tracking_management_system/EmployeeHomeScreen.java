package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;

public class EmployeeHomeScreen extends AppCompatActivity {
    BottomNavigationView navBar;
    private UserMainView userHomeFragment;
    private SettingsFragment settingsFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_screen);
        userHomeFragment = new UserMainView();
        settingsFragment = new SettingsFragment();
        navBar = findViewById(R.id.employeeBottomNav);

        InitializeFragment(userHomeFragment);


        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.userMainView:

                        InitializeFragment(userHomeFragment);


                        return true;

                    case R.id.settingsFragment:
                        InitializeFragment(settingsFragment);
                        return true;
                }
                return true;
            }});

    }
    private void InitializeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}