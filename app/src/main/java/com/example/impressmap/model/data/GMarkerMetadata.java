package com.example.impressmap.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class GMarkerMetadata implements Parcelable
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

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public LatLng getPositionLatLng()
    {
        String[] pos = position.split(" ");
        return new LatLng(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
    }

    public void setPositionLatLng(LatLng latLng)
    {
        this.position = String.format("%s %s", latLng.latitude, latLng.longitude);
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
