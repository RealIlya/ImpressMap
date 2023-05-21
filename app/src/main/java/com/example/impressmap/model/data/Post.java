package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DATE_NODE;
import static com.example.impressmap.util.Constants.Keys.GMARKER_ID;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.TEXT_NODE;
import static com.example.impressmap.util.Constants.Keys.TITLE_NODE;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Post implements TransferableToDatabase, Owner
{
    public static final Creator<Post> CREATOR = new Creator<Post>()
    {
        @Override
        public Post createFromParcel(Parcel in)
        {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size)
        {
            return new Post[size];
        }
    };
    private String id = "";
    private OwnerUser ownerUser = new OwnerUser();
    private Date date = new Date();
    private String title = "";
    private String text = "";
    private String gMarkerId = "";

    public Post()
    {
    }

    protected Post(Parcel in)
    {
        id = in.readString();
        ownerUser.setId(in.readString());
        ownerUser.setFullName(in.readString());
        title = in.readString();
        text = in.readString();
        gMarkerId = in.readString();
    }

    @Override
    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(OWNER_ID_NODE, ownerUser.getId());
        data.put(DATE_NODE, date.getTime());
        data.put(TITLE_NODE, title);
        data.put(TEXT_NODE, text);
        data.put(GMARKER_ID, gMarkerId);

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

    public String getGMarkerId()
    {
        return gMarkerId;
    }

    public void setGMarkerId(String gMarkerId)
    {
        this.gMarkerId = gMarkerId;
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
        if (!(obj instanceof Post))
        {
            return false;
        }
        Post o = (Post) obj;
        return o.id.equals(id);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel,
                              int i)
    {
        parcel.writeString(id);
        parcel.writeString(ownerUser.getId());
        parcel.writeString(ownerUser.getFullName());
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeString(gMarkerId);
    }
}
