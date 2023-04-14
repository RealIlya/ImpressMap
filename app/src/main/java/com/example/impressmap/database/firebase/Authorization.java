package com.example.impressmap.database.firebase;

import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.util.Log;

import com.example.impressmap.database.firebase.repos.UsersRepo;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.auth.FirebaseUser;

public class Authorization
{
    private final UsersRepo usersRepo;

    public Authorization()
    {
        usersRepo = new UsersRepo();
    }


    public void signUp(String email,
                       String password,
                       SuccessCallback onSuccessCallback)
    {
        AUTH.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult ->
            {
                User user = User.createUser(email);
                FirebaseUser firebaseUser = authResult.getUser();

                user.setId(firebaseUser.getUid());
                user.setPhoneNumber(firebaseUser.getPhoneNumber());

                usersRepo.insert(user);

                UID = AUTH.getCurrentUser().getUid();

                onSuccessCallback.onSuccess();
            })
            .addOnFailureListener(e -> Log.e("signUp", e.getMessage()));
    }

    public void signIn(String email,
                       String password,
                       SuccessCallback onSuccessCallback)
    {
        AUTH.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(authResult ->
            {
                UID = AUTH.getCurrentUser().getUid();

                onSuccessCallback.onSuccess();
            })
            .addOnFailureListener(e -> Log.e("signIn", e.getMessage()));
    }


    public void signOut()
    {
        AUTH.signOut();
    }
}
