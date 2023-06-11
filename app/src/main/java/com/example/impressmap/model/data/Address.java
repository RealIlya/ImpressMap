package com.example.impressmap.model.data;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Data class for store data about address
 */
public class Address
{
    protected String id = "";
    protected String desc = "";
    protected String ownerId = "";
    protected String position = "";

    protected boolean selected;

    public Address()
    {
        selected = false;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
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

    public void setPositionLatLng(LatLng position)
    {
        this.position = String.format("%s %s", position.latitude, position.longitude);
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
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
        if (!(obj instanceof Address))
        {
            return false;
        }
        Address o = (Address) obj;
        return o.id.equals(id);
    }
}
