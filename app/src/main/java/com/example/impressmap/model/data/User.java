package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.EMAIL_NODE;
import static com.example.impressmap.util.Constants.Keys.FULL_NAME_NODE;
import static com.example.impressmap.util.Constants.Keys.PHONE_NUMBER_NODE;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User implements TransferableToDatabase
{
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;

    @Override
    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(FULL_NAME_NODE, fullName);
        data.put(EMAIL_NODE, email);
        data.put(PHONE_NUMBER_NODE, phoneNumber);

        return data;
    }

    @NonNull
    public static User createUser(String fullName, String email)
    {
        User user = new User();
        user.email = email;
        user.fullName = fullName;
        user.phoneNumber = "";

        return user;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getName()
    {
        String[] split = fullName.split(" ");
        return split[0];
    }

    public String getSurname()
    {
        String[] split = fullName.split(" ");
        return split.length > 1 ? split[1] : "";
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
