package com.example.attendance_tracking_management_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn;
    private EditText email,password;
    private PrefHelper pref;
     private FirebaseAuth firebaseAuth ;
    private   FirebaseFirestore db;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = (Button) findViewById(R.id.loginBtn) ;
        btn.setOnClickListener(this);
        email = (EditText) findViewById(R.id.reason);
        password = (EditText) findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        pref = new PrefHelper(getBaseContext(),ConstName.sharedPref);

        db = FirebaseFirestore.getInstance();


    }



    @Override
    public void onClick(View view) {
        final String emailTxt = email.getText().toString().trim();
        final String passwordtxt = password.getText().toString().trim();
        final boolean isValidEmail = Validation.isValidEmail(emailTxt);
        final boolean isValidpassword = Validation.isValidPassword(passwordtxt);
        if(!isValidEmail){
            email.setError("Invalid email");
            email.requestFocus();
            return;
        }
        if(!isValidpassword){
            password.setError("password should be at lease 6 characters");
            password.requestFocus();
            return;
        }



        if(isValidpassword&&isValidEmail){
        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this,"Login","Loading....");

            firebaseAuth.signInWithEmailAndPassword(emailTxt, passwordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){

                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    Map<String, Object> userMap = document.getData();
                                    UserModel   currentUser =(UserModel) UserModel.fromMap(userMap);
                                    dialog.dismiss();
                                    pref.setMap(ConstName.user,currentUser.toMap());
                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);


                                }
                            }
                        } );

                    } else {
                        dialog.dismiss();

                        Toast.makeText(getBaseContext(), "wrong email or password", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }

    }
}
