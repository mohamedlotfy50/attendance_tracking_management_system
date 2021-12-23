package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText username , phone , password , passcon ;
    String user_name, phone_num, pass_word, pass_con;
    Button save , close;
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
                pass_word = password.getText().toString();
                pass_con = passcon.getText().toString();

                if(!validateInputs(user_name,phone_num,pass_word,pass_con)){
                    final ProgressDialog dialog = new ProgressDialog(UpdateAccountActivity.this);
                    dialog.setTitle("Update Account");
                    dialog.setMessage("Updating....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    if(pass_word != userModel.password){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(userModel.email, "password1234");

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(pass_word).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        //Log.d(TAG, "Password updated");
                                                        Toast.makeText(UpdateAccountActivity.this , "Password updated" , Toast.LENGTH_SHORT).show();
                                                        fStore.collection("users").document(userModel.id).update("name",username.getText().toString(),
                                                                "phone",phone.getText().toString(),
                                                                "password",password.getText().toString())
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(UpdateAccountActivity.this , "User Updated Successfully" , Toast.LENGTH_LONG).show();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        //Log.d(TAG, "Error password not updated");
                                                        Toast.makeText(UpdateAccountActivity.this , "Error password not updated" , Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.d(TAG, "Error auth failed");
                                            Toast.makeText(UpdateAccountActivity.this , task.getException().toString() , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                    }



                }
            }
        });



    }
    private void initComponents(){
        username = findViewById(R.id.username_edttxt);
        phone = findViewById(R.id.phonenum_edttxt);
        password = findViewById(R.id.passwordttxt);
        passcon =findViewById(R.id.passcon_edttxt);

        save = findViewById(R.id.saveBtn);
        close =findViewById(R.id.closeBtn);
    }
    private  boolean validateInputs(String name , String phone_num , String pass_word , String confirm){
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
        if(pass_word.isEmpty()){
            password.setError("Password Required!");
            password.requestFocus();
            return true;
        }
        if(pass_word.length()<6){
            password.setError("password should be at lease 6 characters");
            password.requestFocus();
            return  true;
        }
        if(confirm.isEmpty()){
            passcon.setError("Confirm Password Required!");
            passcon.requestFocus();
            return true;
        }
        if(confirm.trim() == pass_word.trim()){
            passcon.setError("Password doesn't match!");
            passcon.requestFocus();
            return true;
        }

        return false;
    }
}