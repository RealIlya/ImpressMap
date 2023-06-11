package com.example.impressmap.model.data;

import androidx.annotation.NonNull;

/**
 * Data class for store data about user
 */
public class User
{
    private String id = "";
    private String fullName = "";
    private String email = "";
    private String phoneNumber = "";

    @NonNull
    public static User createUser(String fullName,
                                  String email)
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
