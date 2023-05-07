package com.example.impressmap.model.data.db;

public class PostDatabase
{
    private String id = "";
    private String ownerId = "";
    private long date = 0;
    private String text = "";
    private String commentIds = "";

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

    public String getCommentIds()
    {
        return commentIds;
    }

    public void setCommentIds(String commentIds)
    {
        this.commentIds = commentIds;
    }
}
