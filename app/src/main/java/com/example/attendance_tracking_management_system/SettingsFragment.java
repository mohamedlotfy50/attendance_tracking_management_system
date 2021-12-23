package com.example.attendance_tracking_management_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsFragment extends Fragment {
    Button accountBtn , addBtn , logoutBtn;
    Intent intent  ;
    private PrefHelper pref;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        accountBtn = view.findViewById(R.id.accountBtn);
        addBtn = view.findViewById(R.id.addBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);
        auth = FirebaseAuth.getInstance();
        pref = new PrefHelper(getActivity().getBaseContext(),ConstName.sharedPref);
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), UpdateAccountActivity.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                pref.deleteKey(ConstName.user);
                intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);


            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}