package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DESC_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.POSITION_NODE;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class Address implements TransferableToDatabase
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

    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(DESC_NODE, desc);
        data.put(OWNER_ID_NODE, ownerId);
        data.put(POSITION_NODE, position);

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
