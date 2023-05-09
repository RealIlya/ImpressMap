package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DATE_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.TEXT_NODE;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Comment implements TransferableToDatabase, Owner
{
    private String id = "";
    private OwnerUser ownerUser = new OwnerUser();
    private Date date = new Date();
    private String text = "";

    @Override
    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(OWNER_ID_NODE, ownerUser.getId());
        data.put(DATE_NODE, date.getTime());
        data.put(TEXT_NODE, text);

        return data;
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

    public void setDate(long date)
    {
        this.date = new Date(date);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Comment))
        {
            return false;
        }
        Comment o = (Comment) obj;
        return o.id.equals(id);
    }
}
