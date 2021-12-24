package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateAccountActivity extends AppCompatActivity {

    FirebaseFirestore fStore;
    PrefHelper pref ;
    UserModel userModel;
    Map userMap ;

    EditText username , phone ,passwordChange ;
    Button save , close, reset;
    private final String TAG = "log";
    private FirebaseAuth auth ;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        initComponents();
        fStore = FirebaseFirestore.getInstance();
        pref = new PrefHelper(this,ConstName.sharedPref);
        userMap = new HashMap();
        db = FirebaseFirestore.getInstance();
        userMap = pref.getMap(ConstName.user);
        userModel = UserModel.fromMap(userMap);
        auth =FirebaseAuth.getInstance();
        passwordChange = findViewById(R.id.resetpassword);
        username = findViewById(R.id.username_edttxt);

        phone = findViewById(R.id.phonenum_edttxt);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordChange.getText()!=null){


changePassword(username.getText().toString(),phone.getText().toString(),passwordChange.getText().toString());


                }else{

                    changeData(username.getText().toString(),phone.getText().toString(),passwordChange.getText().toString());
                }

            }
        });





    }
   private void changePassword(String username,String phoneNumber,String newPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        AuthCredential credential = EmailAuthProvider
                .getCredential(userModel.email, userModel.password);

        if(credential==null){
            Log.d(TAG, "cred is null");

        }
        if(user == null){

            Log.d(TAG, "user is null");


        }


        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        changeData(username,phoneNumber,newPassword);
                                        Log.d(TAG, "password updated");

                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });
    }

    private void changeData(String userName,String phoneNumber,String changedPass){
        Log.d(TAG, String.format("name %s phone %s pass %s",userName,phoneNumber,changedPass));
        String newName = userModel.name,newEmail=userModel.email,newPhone=userModel.phone,newPassword =userModel.password ;
        if(userName !=null){
newName =userName;
        }
        if(phoneNumber   !=null){
phoneNumber = newPhone;
        }
        if(changedPass  !=null){
            newPassword = changedPass;
        }



        final UserModel newModel = new UserModel(userModel.id,newName,newEmail,userModel.department,newPhone,userModel.role,newPassword,userModel.imgUrl);
        db.collection("users")
                .document(userModel.id)
                .update(newModel.toMap());
        pref.setMap(ConstName.user,newModel.toMap());
        Log.d(TAG, "Data changed");

    }
    private void initComponents(){
        username = findViewById(R.id.username_edttxt);
        phone = findViewById(R.id.phonenum_edttxt);

        save = findViewById(R.id.saveBtn);
        close =findViewById(R.id.closeBtn);
    }
    private  boolean validatePassword( String pass_word , String confirm){
        if(pass_word.isEmpty()){
            Toast.makeText(getBaseContext(),"Password Required!",Toast.LENGTH_LONG).show();
            return true;
        }
        if(pass_word.length()<6){
            Toast.makeText(getBaseContext(),"password should be at lease 6 characters",Toast.LENGTH_LONG).show();

            return  true;
        }
        if(confirm.isEmpty()){
            Toast.makeText(getBaseContext(),"Confirm Password Required!",Toast.LENGTH_LONG).show();
            return true;
        }
        if(confirm.trim() == pass_word.trim()){
            Toast.makeText(getBaseContext(),"Password doesn't match!",Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    private  boolean validateInputs(String name , String phone_num ){
        if(name.isEmpty()){
            username.setError("User Name Required!");
            username.requestFocus();
            return true;
        }

        if(phone_num.isEmpty()){
            phone.setError("Phone Number Required!");
            phone.requestFocus();
            return true;
        }

        return false;
    }
}