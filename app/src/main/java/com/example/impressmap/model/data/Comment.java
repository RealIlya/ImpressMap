package com.example.impressmap.model.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.impressmap.model.data.User;

import java.util.Date;

@Entity
public class Comment
{
    @PrimaryKey
    @ColumnInfo(name = "commentid")
    private long id;
    @ColumnInfo
    private User user;
    @ColumnInfo
    private Date date;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String text;
    private String firebaseId;

    public Comment(long id,
                   User user,
                   Date date,
                   String title,
                   String text)
    {
        this.id = id;
        this.user = user;
        this.date = date;
        this.title = title;
        this.text = text;
    }

    public long getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public Date getDate()
    {
        return date;
    }

    public String getTitle()
    {
        return title;
    }

    public String getText()
    {
        return text;
    }

    public String getFirebaseId()
    {
        return firebaseId;
    }
}
