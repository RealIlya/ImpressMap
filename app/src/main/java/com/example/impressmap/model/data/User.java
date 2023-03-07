package com.example.impressmap.model.data;

public class User
{
    private long id;
    private String nickname;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;

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
}
