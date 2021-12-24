package com.example.attendance_tracking_management_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeesFragment extends Fragment {

    ListView listView;
    Button all , dp1 , dp2;
    List<UserModel> employees;
    FirebaseFirestore db;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_employees, container, false);

        listView = view.findViewById(R.id.employee_listview);
        all = view.findViewById(R.id.allBtn);
        dp1 = view.findViewById(R.id.dp1Btn);
        dp2 = view.findViewById(R.id.dp2Btn);
        db = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Employees");
        dialog.setMessage("Fetching Employees....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        employees = new ArrayList<UserModel>();

        AllEmployees();

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.rounded_button_dark);
                dp1.setBackgroundResource(R.drawable.rounded_button);
                dp2.setBackgroundResource(R.drawable.rounded_button);
                employees.clear();
                AllEmployees();
                if(employees.isEmpty()){
                    listView.setAdapter(null);
                }
            }
        });

        dp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.rounded_button);
                dp1.setBackgroundResource(R.drawable.rounded_button_dark);
                dp2.setBackgroundResource(R.drawable.rounded_button);
                employees.clear();
                dp1_Employees();
                if(employees.isEmpty()){
                    listView.setAdapter(null);
                }
            }
        });

        dp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setBackgroundResource(R.drawable.rounded_button);
                dp1.setBackgroundResource(R.drawable.rounded_button);
                dp2.setBackgroundResource(R.drawable.rounded_button_dark);
                employees.clear();
                dp2_Employees();
                if(employees.isEmpty()){
                    listView.setAdapter(null);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserModel user = (UserModel) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), EmployerInfo.class);
                intent.putExtra("list user",user);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
/*                Log.i("the user data is",String.format("username: %s,email: %s,phone: %s,id: %s,password: %s",user.name,user.email,user.phone,user.id,user.password));
                Fragment fragment = new EmployeeInfo(user);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();*/

            }
        });



        return view;
    }
    private void dp2_Employees() {
        db.collection("users").whereEqualTo("role","Employee")
                .whereEqualTo("department","Dep 2").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                getEmployees(snapshots,e);
            }
        });

    }
    private void dp1_Employees() {
        db.collection("users").whereEqualTo("role","Employee")
                .whereEqualTo("department","Dep 1").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                getEmployees(snapshots,e);
            }
        });
    }
    private void AllEmployees() {
        db.collection("users").whereEqualTo("role","Employee").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                getEmployees(snapshots,e);
            }
        });

    }

    private void getEmployees(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e){
        if (e != null) {
            System.err.println("Listen failed:" + e);
            return;
        }
        if(snapshots.isEmpty()){
            Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
        }
        if (snapshots != null) {
            for(DocumentSnapshot d : snapshots){
                Map<String,Object> userMap = d.getData();
                UserModel employee = UserModel.fromMap(userMap);
                employees.add(employee);
            }
            EmployeeAdapter adapter = new EmployeeAdapter(getContext(),employees);
            listView.setAdapter(adapter);
            dialog.dismiss();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}