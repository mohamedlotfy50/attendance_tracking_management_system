package com.example.attendance_tracking_management_system;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.concurrent.Executor;

public class Auth {
  final private FirebaseAuth firebaseAuth ;
    final private   FirebaseFirestore db;
    Auth(){
    firebaseAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
}

    public UserModel login(String email,String password){
        final UserModel[] currentUser = new UserModel[1];
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                currentUser[0] = UserModel.fromMap(userMap);
                           }
                       }
                   } );

               } else {
                 currentUser[0] = null;

               }
           }

       });
    return currentUser[0];
    }

    public void signOut(){
        firebaseAuth.signOut();
    }
    public boolean register(){
        //TODO: please add the registration logic and parameters here
        // if registration succeed return true else return false

        return false;
    }



 }
