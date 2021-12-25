package com.example.attendance_tracking_management_system;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    PrefHelper pref ;
    UserModel userModel;
    Map userMap ;
    FirebaseFirestore db;
    List<UserModel> employees;
    List<UserModel> attendance;

    ProgressDialog dialog;

    TextView name , dep, attended , absent ;
    ImageView img;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_landing_page, container, false);
        pref = new PrefHelper(getContext(),ConstName.sharedPref);
        userMap = new HashMap();
        userMap = pref.getMap(ConstName.user);
        userModel = UserModel.fromMap(userMap);

        db = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.usernameHome);
        dep = view.findViewById(R.id.departmentHome);
        img = view.findViewById(R.id.imgHome);
        attended = view.findViewById(R.id.attend_num);
        absent = view.findViewById(R.id.absent_num);

        employees = new ArrayList<UserModel>();
        attendance = new ArrayList<UserModel>();

        Picasso.get().load(userModel.imgUrl).resize(83,86).into(img);
        name.setText(userModel.name);
        dep.setText(userModel.department);

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Employees");
        dialog.setMessage("Fetching Employees....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        db.collection("users").whereEqualTo("role","Employee").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                getEmployees(snapshots,e);
                db.collection("attendance").whereEqualTo("date","2021/12/23"/*dtf.format(now)*/).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        getAttendance(snapshots,e);
                        if(!employees.isEmpty() && !attendance.isEmpty()){
                            int attend_total = attendance.size();
                            int employees_total = employees.size();
                            int absent_total = employees_total - attend_total;

                            attended.setText(attend_total+ "");
                            absent.setText(absent_total +"");
                        }

                    }
                });
            }
        });


        return view ;
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
        }
    }

    private void getAttendance(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e){
        if (e != null) {
            System.err.println("Listen failed:" + e);
            return;
        }
        if(snapshots.isEmpty()){
            Toast.makeText(getContext(), "No attendance today !! ", Toast.LENGTH_SHORT).show();
        }
        if (snapshots != null) {
            for(DocumentSnapshot d : snapshots){
                Map<String,Object> user_Map = d.getData();
                UserModel employee = UserModel.fromMap(user_Map);
                attendance.add(employee);
            }
            dialog.dismiss();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}