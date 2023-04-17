package com.example.impressmap.model.data;

import androidx.room.Entity;

import java.util.Date;
import java.util.Map;

@Entity
public class Comment implements TransferableToDatabase
{
    private String id;
    private OwnerUser ownerUser;
    private Date date;
    private String title;
    private String text;

    @Override
    public Map<String, Object> prepareToTransferToDatabase()
    {
        return null;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public OwnerUser getOwnerUser()
    {
        return ownerUser;
    }

    public void setOwnerUser(OwnerUser ownerUser)
    {
        this.ownerUser = ownerUser;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
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
