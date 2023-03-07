package com.example.impressmap.model.data;

import java.util.Date;

public class Post
{
    private long id;
    private User user;
    private Date date;
    private String title;
    private String text;


    public Post(long id,
                Date date,
                String title,
                String text)
    {
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
    }

    public long getId()
    {
        return id;
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
}
