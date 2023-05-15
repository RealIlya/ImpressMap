package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.POSITION_NODE;
import static com.example.impressmap.util.Constants.Keys.TITLE_NODE;
import static com.example.impressmap.util.Constants.Keys.TYPE_NODE;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

// for database store
@Entity
public class GMarkerMetadata implements TransferableToDatabase, Parcelable
{
    public static final int ADDRESS_MARKER = 0, COMMON_MARKER = 1;
    public static final Creator<GMarkerMetadata> CREATOR = new Creator<GMarkerMetadata>()
    {
        @Override
        public GMarkerMetadata createFromParcel(Parcel in)
        {
            return new GMarkerMetadata(in);
        }

        @Override
        public GMarkerMetadata[] newArray(int size)
        {
            return new GMarkerMetadata[size];
        }
    };
    private String id = "";
    private String title = "";
    private String position = "";
    private int type = -1;

    public GMarkerMetadata()
    {
    }

    protected GMarkerMetadata(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        position = in.readString();
        type = in.readInt();
    }

    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(TITLE_NODE, title);
        data.put(POSITION_NODE, position);
        data.put(TYPE_NODE, type);

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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public LatLng getPosition()
    {
        String[] pos = position.split(" ");
        return new LatLng(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public void setPositionLatLng(LatLng position)
    {
        this.position = String.format("%s %s", position.latitude, position.longitude);
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
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
        if (!(obj instanceof GMarkerMetadata))
        {
            return false;
        }
        GMarkerMetadata o = (GMarkerMetadata) obj;
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
        parcel.writeString(title);
        parcel.writeString(position);
        parcel.writeInt(type);
    }
}
