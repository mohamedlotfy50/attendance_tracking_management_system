package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class leave_request extends AppCompatActivity {
    private UserModel user;
    private  PrefHelper pref;
    private EditText username,email,phone,reason;
    private Button request;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        pref = new PrefHelper(getBaseContext(),ConstName.sharedPref);
        user = UserModel.fromMap(pref.getMap(ConstName.user));
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        reason = findViewById(R.id.reason);
        username.setText(user.name);
        email.setText(user.email);
        phone.setText(user.phone);
        disableEditText(username);
        disableEditText(email);
        disableEditText(phone);
        request =findViewById(R.id.requestbtn);
        db =FirebaseFirestore.getInstance();
        request.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
              if(reason.getText().toString().trim().isEmpty()){
                  reason.setError("the reason can't be empty");
                  reason.requestFocus();
                  return;
              }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();

                final LeaveRequest r = new LeaveRequest(user.id,reason.getText().toString().trim(),"",0,dtf.format(now));
                db.collection(ConstName.leaveRequests).document(String.format("%s%s",user.id, now.getDayOfYear())).set(r.toMap(),  SetOptions.merge()).addOnCompleteListener(new OnCompleteListener(){

                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){

                            Intent intent  = new Intent(leave_request.this,EmployeeHomeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }
                });
            }
        });

    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}