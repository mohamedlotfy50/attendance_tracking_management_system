package com.example.attendance_tracking_management_system;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class UserMainView extends Fragment {
private TextView username,department,attendance,absence,requests;
private  UserModel user;
private Button attendaceBtn,leaveRequestBtn;
private PrefHelper pref;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_user_main_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PrefHelper(getActivity().getBaseContext(),ConstName.sharedPref);
        user = UserModel.fromMap(pref.getMap(ConstName.user));
        db = FirebaseFirestore.getInstance();


        username = view.findViewById(R.id.username);
        department =   view.findViewById(R.id.department);
        attendance = view.findViewById(R.id.attend_num);

        absence = view.findViewById(R.id.absent_num);
        requests = view.findViewById(R.id.rqst_num);

        attendaceBtn = view.findViewById(R.id.attend);
        leaveRequestBtn = view.findViewById(R.id.leaveRequest);

        username.setText(user.name);
        department.setText(user.department);
attendaceBtn.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        final Attendance attend = new Attendance(user.id,dtf.format(now));
        db.collection(ConstName.attendance).document(String.format("%s%s",user.id,now.getDayOfYear())).set(attend.toMap(),  SetOptions.merge());
    }
});


    }
}