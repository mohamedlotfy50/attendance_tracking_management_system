package com.example.attendance_tracking_management_system;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter<UserModel> {
    List<UserModel> employees;
    Context context;
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
        if(employees.isEmpty()){
            UserModel current_employee = employees.get(position);
            viewHolder.getUserName().setText(current_employee.getName());
            viewHolder.getDepartment().setText(current_employee.getDepartment());
            viewHolder.getDeleteBtnBtn().setImageResource(R.drawable.ic_icon11_101144);
            Picasso.get().load(current_employee.getImgUrl()).into(viewHolder.getProfile_img());
        }
        UserModel current_employee = employees.get(position);
        viewHolder.getUserName().setText(current_employee.getName());
        viewHolder.getDepartment().setText(current_employee.getDepartment());
        viewHolder.getDeleteBtnBtn().setImageResource(R.drawable.ic_icon11_101144);
        Picasso.get().load(current_employee.getImgUrl()).into(viewHolder.getProfile_img());
        /*.resize(74,84).centerCrop().*/

        return view;
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
    static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                  int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;   //Default subsampling size
        // See if image raw height and width is bigger than that of required view
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            //bigger
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
