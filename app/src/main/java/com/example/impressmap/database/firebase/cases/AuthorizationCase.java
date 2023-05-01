package com.example.impressmap.database.firebase.cases;

import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.util.Log;

import com.example.impressmap.database.firebase.repos.UsersRepo;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.auth.FirebaseUser;

public class AuthorizationCase
{
    private final UsersRepo usersRepo;

    public AuthorizationCase()
    {
        usersRepo = new UsersRepo();
    }

    public void signUp(String name,
                       String surname,
                       String email,
                       String password,
                       SuccessCallback successCallback)
    {
        AUTH.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult ->
        {
            User user = User.createUser(String.format("%s %s", name, surname), email);
            FirebaseUser firebaseUser = authResult.getUser();

            user.setId(firebaseUser.getUid());
            user.setPhoneNumber(firebaseUser.getPhoneNumber());

            usersRepo.insert(user, successCallback);

            UID = firebaseUser.getUid();
        }).addOnFailureListener(e -> Log.e("signUp", e.getMessage()));
    }

    public void signIn(String email,
                       String password,
                       SuccessCallback successCallback)
    {
        AUTH.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult ->
        {
            UID = authResult.getUser().getUid();

            successCallback.onSuccess();
        }).addOnFailureListener(e -> Log.e("signIn", e.getMessage()));
    }


    public void signOut(SuccessCallback successCallback)
    {
        AUTH.signOut();
        successCallback.onSuccess();
    }
}
