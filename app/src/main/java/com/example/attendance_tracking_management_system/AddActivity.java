package com.example.attendance_tracking_management_system;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    Uri resultUri;
    String imgUrl= "";


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

        add_imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
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
                                //img logic
                                if(resultUri != null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("Profile Images").child(UId);
                                    Bitmap bitmap = null;

                                    try{
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,BAOS);
                                    byte[] data = BAOS.toByteArray();
                                    filePath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Toast.makeText(getBaseContext() , uri.toString() , Toast.LENGTH_LONG).show();
                                                    imgUrl = uri.toString();
                                                    AddUserToFireBase(dialog);

                                                }
                                            });
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getBaseContext() , e.getMessage() , Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    /*UploadTask uploadTask = filePath.putBytes(data);
                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if(!task.isSuccessful()){
                                                    throw task.getException();
                                                }
                                            return filePath.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                imgUrl = downloadUri.toString();
                                                Toast.makeText(AddActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    }});*/

                                    /*filePath.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot ts) {
                                            if(ts.getMetadata() != null && ts.getMetadata().getReference() != null){
                                                Task<Uri> result = ts.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imgUrl = uri.toString();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddActivity.this,"Image Upload Failed",Toast.LENGTH_SHORT).show();

                                        }
                                    });*/

                                    /*uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddActivity.this,"Image Upload Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot ts) {
                                            if(ts.getMetadata() != null && ts.getMetadata().getReference() != null){
                                                Task<Uri> result = ts.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imgUrl = uri.toString();



                                                    }
                                                });
                                            }
                                        }
                                    });*/
                                }


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

    private void AddUserToFireBase(ProgressDialog dialog) {
        UserModel userModel = new UserModel(UId,user_name,e_mail,department,phoneNumber,role,password,imgUrl);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        if(requestCode == 1 && resultCode == RESULT_OK && data!= null){
            resultUri = data.getData();
            Picasso.get().load(resultUri).resize(120,120).centerCrop().into(profile_img);
            //profile_img.setImageURI(resultUri);
        }*/
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    // Set the image in ImageView
                    profile_img.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    private  boolean validateInputs(String name , String e_mail , String phone , String pass_word , String confirm , String role , String department/*,String img_Url*/){
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
/*        if(img_Url.isEmpty()){
            Toast.makeText(AddActivity.this,"please choose a profile image",Toast.LENGTH_SHORT).show();
            return true;
        }*/
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

        profile_img = findViewById(R.id.profile_image);
        add_imgBtn = findViewById(R.id.fab_img);
    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}