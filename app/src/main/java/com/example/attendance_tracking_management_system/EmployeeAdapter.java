package com.example.attendance_tracking_management_system;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeAdapter extends ArrayAdapter<UserModel> {
    List<UserModel> employees;
    Context context;
    PrefHelper pref ;
    UserModel user;
    Map userMap ;
    public EmployeeAdapter(@NonNull Context context , @NonNull List<UserModel> employees){
        super(context,R.layout.employeelistview,R.id.username_txt,employees);
        this.employees = employees;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView , @NonNull ViewGroup parent){
        View view =convertView;
        ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.employeelistview,parent,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)view.getTag();
        }
        pref = new PrefHelper(getContext(),ConstName.sharedPref);
        userMap = new HashMap();
        userMap = pref.getMap(ConstName.user);
        user = UserModel.fromMap(userMap);
        if(!employees.isEmpty()){
            UserModel current_employee = employees.get(position);
            viewHolder.getUserName().setText(current_employee.name);
            viewHolder.getDepartment().setText(current_employee.department);
            viewHolder.getDeleteBtnBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteuser(user.email, user.password);
                }
            });
            Picasso.get().load(current_employee.imgUrl).into(viewHolder.getProfile_img());
        }

        /*.resize(74,84).centerCrop().*/

        return view;
    }

    private void deleteuser(String email, String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        if (user != null) {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User account deleted.");
//                                                startActivity(new Intent(DeleteUser.this, StartActivity.class));
//                                                Toast.makeText(DeleteUser.this, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
        }
    }

    class ViewHolder{
        TextView userName, department;
        ImageView profile_img;
        ImageButton deleteBtn;
        View view;
        ViewHolder(View v){
            view = v ;
        }

        public TextView getUserName() {
            if(userName == null){
                userName = view.findViewById(R.id.username_txt);
            }
            return userName;
        }

        public TextView getDepartment() {
            if(department == null){
                department = view.findViewById(R.id.dep_txt);
            }
            return department;
        }

        public ImageView getProfile_img() {
            if(profile_img == null ){
                profile_img = view.findViewById(R.id.profile_img);
            }
            return profile_img;
        }

        public ImageButton getDeleteBtnBtn() {
            if(deleteBtn==null){
                deleteBtn = view.findViewById(R.id.delete_btn);
            }
            return deleteBtn;
        }
    }

}
