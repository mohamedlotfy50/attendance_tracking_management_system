package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText username , email , phone_number , pass , conPass;
    Button add_Emp;
    FloatingActionButton add_imgBtn;
    ImageView profile_img;
    Spinner Role , Department;
    String user_name , e_mail , phoneNumber , password , confirm_password , role , department;
    String UId;
    FirebaseAuth fAuth;
    //ProgressDialog dialog;
    FirebaseFirestore fStore;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initComponent();
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
                R.array.role, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Role.setAdapter(roleAdapter);
        Role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role = Role.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> depAdapter = ArrayAdapter.createFromResource(this,
                R.array.department, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Department.setAdapter(depAdapter);
        Department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = Department.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_Emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = username.getText().toString().trim();
                e_mail = email.getText().toString().trim();
                phoneNumber = phone_number.getText().toString().trim();
                password = pass.getText().toString().trim();
                confirm_password =conPass.getText().toString().trim();



                if(!validateInputs(user_name,e_mail,phoneNumber,password,confirm_password,role,department)){
                    //final ProgressDialog dialog = ProgressDialog.show(AddActivity.this,"Add Employee","Adding....");
                    final ProgressDialog dialog = new ProgressDialog(AddActivity.this);
                    dialog.setTitle("Add Employee");
                    dialog.setMessage("Adding....");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    fAuth.createUserWithEmailAndPassword(e_mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UId = fAuth.getCurrentUser().getUid();
                                UserModel userModel = new UserModel(UId,user_name,e_mail,department,phoneNumber,role,password);
                                DocumentReference dr = fStore.collection("users").document(UId);
                                Map<String,Object> user = userModel.toMap();
                                dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(AddActivity.this, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        finish();
                                    }
                                });
                            }else{
                                dialog.dismiss();
                                Toast.makeText(getBaseContext(), "Failed to add Employee !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    private  boolean validateInputs(String name , String e_mail , String phone , String pass_word , String confirm , String role , String department){
        if(name.isEmpty()){
            username.setError("User Name Required!");
            username.requestFocus();
            return true;
        }
        if(e_mail.isEmpty()){
            email.setError("E-Mail Required!");
            email.requestFocus();
            return true;
        }
        if(phone.isEmpty()){
            phone_number.setError("Phone Number Required!");
            phone_number.requestFocus();
            return true;
        }
        if(pass_word.isEmpty()){
            pass.setError("Password Required!");
            pass.requestFocus();
            return true;
        }
        if(pass_word.length()<6){
            pass.setError("password should be at lease 6 characters");
            pass.requestFocus();
            return  true;
        }
        if(confirm.isEmpty()){
            conPass.setError("Confirm Password Required!");
            conPass.requestFocus();
            return true;
        }
        if(confirm.trim() == pass_word.trim()){
            conPass.setError("Password doesn't match!");
            conPass.requestFocus();
            return true;
        }
        if(role == "Role"){
            TextView errorText = (TextView)Role.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("A Role must be chosen!");//changes the selected item text to this
            Role.requestFocus();
            return true;
        }
        if(department == "Department"){
            TextView errorText = (TextView)Department.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("A Role must be chosen!");//changes the selected item text to this
            Department.requestFocus();
            return true;
        }
        return false;
    }
    private void initComponent(){
        username = findViewById(R.id.username_edttxt);
        email = findViewById(R.id.email_edttxt);
        phone_number = findViewById(R.id.phonenum_edttxt);
        pass = findViewById(R.id.passwordttxt);
        conPass = findViewById(R.id.passcon_edttxt);
        add_Emp = findViewById(R.id.buttonAdd);
        Role = findViewById(R.id.roleSpinner);
        Department = findViewById(R.id.depSpinner);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

}