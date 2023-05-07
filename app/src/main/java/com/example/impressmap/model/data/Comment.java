package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.COMMENT_IDS_NODE;
import static com.example.impressmap.util.Constants.Keys.DATE_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.TEXT_NODE;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Comment implements TransferableToDatabase
{
    private String id = "";
    private OwnerUser ownerUser = new OwnerUser();
    private Date date = new Date();
    private String text = "";
    private List<String> commentIds = new ArrayList<>();

    @Override
    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(OWNER_ID_NODE, ownerUser.getId());
        data.put(DATE_NODE, date.getTime());
        data.put(TEXT_NODE, text);
        data.put(COMMENT_IDS_NODE, String.join(" ", commentIds));

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

    public List<String> getCommentIds()
    {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds)
    {
        this.commentIds = commentIds;
    }

    public void setCommentIds(@NonNull String commentIds)
    {
        this.commentIds = Arrays.asList(commentIds.split(" "));
    }
}
