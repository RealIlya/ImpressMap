package com.example.impressmap.model.data.db;

public class PostDatabase
{
    private String id = "";
    private String ownerId = "";
    private long dateTime = 0;
    private String title = "";
    private String text = "";

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public long getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(long dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
