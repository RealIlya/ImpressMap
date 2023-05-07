package com.example.impressmap.database.firebase.cases;

import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import com.example.impressmap.database.firebase.repos.UsersRepo;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.FailCallback;
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
                       SuccessCallback successCallback,
                       FailCallback failCallback)
    {
        AUTH.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult ->
        {
            User user = User.createUser(String.format("%s %s", name, surname), email);
            FirebaseUser firebaseUser = authResult.getUser();

            user.setId(firebaseUser.getUid());
            user.setPhoneNumber(firebaseUser.getPhoneNumber());

            usersRepo.insert(user, successCallback);

            UID = firebaseUser.getUid();
        }).addOnFailureListener(e -> failCallback.onFail());
    }

    public void signIn(String email,
                       String password,
                       SuccessCallback successCallback,
                       FailCallback failCallback)
    {
        AUTH.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult ->
        {
            UID = authResult.getUser().getUid();

            successCallback.onSuccess();
        }).addOnFailureListener(e -> failCallback.onFail());
    }


    public void signOut(SuccessCallback successCallback)
    {
        AUTH.signOut();
        successCallback.onSuccess();
    }
}
