package com.example.impressmap.model.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userid")
    private long id;
    @ColumnInfo
    private String nickname;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String surname;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String phoneNumber;
    @ColumnInfo
    private String password;
    private String firebaseId;

    public User(long id,
                String nickname,
                String name,
                String surname,
                String email,
                String phoneNumber,
                String password)
    {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public long getId()
    {
        return id;
    }

    public String getNickname()
    {
        return nickname;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFirebaseId()
    {
        return firebaseId;
    }
}
