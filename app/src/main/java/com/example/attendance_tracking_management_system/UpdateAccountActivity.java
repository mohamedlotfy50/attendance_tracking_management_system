package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

    EditText username , phone  ;
    String user_name, phone_num, pass_word, pass_con;
    Button save , close, reset;
    private final String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        initComponents();
        fStore = FirebaseFirestore.getInstance();
        pref = new PrefHelper(this,ConstName.sharedPref);
        userMap = new HashMap();
        userMap = pref.getMap(ConstName.user);
        userModel = UserModel.fromMap(userMap);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = username.getText().toString();
                phone_num = phone.getText().toString();

                if(!validateInputs(user_name,phone_num)){
                    final ProgressDialog dialog = new ProgressDialog(UpdateAccountActivity.this);
                    dialog.setTitle("Update Account");
                    dialog.setMessage("Updating....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    fStore.collection("users").document(userModel.id).update("name",username.getText().toString(),
                            "phone",phone.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UpdateAccountActivity.this , "User Updated Successfully" , Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder passResetDialog = new AlertDialog.Builder(view.getContext());
                passResetDialog.setTitle("Reset Password");
                passResetDialog.setMessage("Enter new Password > 6 Characters");

                LinearLayout l = new LinearLayout(view.getContext());
                l.setOrientation(LinearLayout.VERTICAL);

                final EditText password = new EditText(view.getContext());
                password.setHint("Password");
                l.addView(password);

                final EditText passcon = new EditText(view.getContext());
                passcon.setHint("Confirm Password");
                l.addView(passcon);

                passResetDialog.setView(l);

                passResetDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String pass_word = password.getText().toString().trim();
                        String pass_con = passcon.getText().toString().trim();
                        if(!validatePassword(pass_word,pass_con)){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(userModel.email, userModel.password);
                            final ProgressDialog dialog1 = new ProgressDialog(UpdateAccountActivity.this);
                            dialog1.setTitle("Update Account");
                            dialog1.setMessage("Updating....");
                            dialog1.setCanceledOnTouchOutside(false);
                            dialog1.show();
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        user.updatePassword(pass_word).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(UpdateAccountActivity.this , "Password Updated Successfully " , Toast.LENGTH_LONG).show();
                                                dialog1.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UpdateAccountActivity.this , e.getMessage().toString() , Toast.LENGTH_LONG).show();
                                                dialog1.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(UpdateAccountActivity.this , task.getException().toString() , Toast.LENGTH_LONG).show();
                                        dialog1.dismiss();
                                    }

                                }
                            });
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Retry!!",Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();

            }
        });



    }
    private void initComponents(){
        username = findViewById(R.id.username_edttxt);
        phone = findViewById(R.id.phonenum_edttxt);

        save = findViewById(R.id.saveBtn);
        close =findViewById(R.id.closeBtn);
        reset = findViewById(R.id.resetBtn);
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