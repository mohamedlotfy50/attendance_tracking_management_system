package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.errorprone.annotations.FormatString;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    private HomeFragment homeFragment;
    private UserMainView userHomeFragment;
    private EmployeesFragment employeesFragment;
    private SettingsFragment settingsFragment;
    private UserModel user;
    private PrefHelper pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navBar = findViewById(R.id.bottomNavView);
        userHomeFragment = new UserMainView();
        homeFragment = new HomeFragment();
        employeesFragment = new EmployeesFragment();
        settingsFragment = new SettingsFragment();
        pref =new  PrefHelper(getBaseContext(),ConstName.sharedPref);
        user = UserModel.fromMap(pref.getMap(ConstName.user));

        if(user.isAdmin()){

            InitializeFragment(homeFragment);

        }else{
            InitializeFragment(userHomeFragment);
        }
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Log.i("is admin", String.format("%s",user.isAdmin()));
                        if(user.isAdmin()){

                            InitializeFragment(homeFragment);

                        }else{
                            InitializeFragment(userHomeFragment);
                        }
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
