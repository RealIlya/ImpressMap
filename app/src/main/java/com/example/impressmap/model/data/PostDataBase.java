package com.example.impressmap.model.data;

import java.util.Date;

public class PostDataBase
{
    private String id = "";
    private String ownerUserId = "";
    private long date = 0;
    private String text = "";

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOwnerUserId()
    {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
