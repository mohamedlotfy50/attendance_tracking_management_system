package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    private HomeFragment homeFragment;
    private EmployeesFragment employeesFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navBar = findViewById(R.id.bottomNavView);

        homeFragment = new HomeFragment();
        employeesFragment = new EmployeesFragment();
        settingsFragment = new SettingsFragment();


        InitializeFragment(homeFragment);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        InitializeFragment(homeFragment);
                        return true;
                    case R.id.nav_employees:
                        InitializeFragment(employeesFragment);
                        return true;
                    case R.id.nav_settings:
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
