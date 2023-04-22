package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DESC_NODE;
import static com.example.impressmap.util.Constants.Keys.FULL_ADDRESS_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.USER_IDS_NODE;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Address implements TransferableToDatabase
{
    //    @TypeConverters(StringListConverter.class)
    private String userIds = "";
    private String id = "";
    private String desc = "";
    private String ownerId = "";
    private String fullAddress = "";

    private boolean selected;

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
        data.put(FULL_ADDRESS_NODE, fullAddress);
        data.put(USER_IDS_NODE, userIds);

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

    public List<String> getUserIds()
    {
        return new ArrayList<>(Arrays.asList(userIds.split(" ")));
    }

    public void setUserIds(String userIds)
    {
        this.userIds = userIds;
    }

    public String getFullAddress()
    {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress)
    {
        this.fullAddress = fullAddress;
    }

    public String getCountry()
    {
        return fullAddress.split(" ")[0];
    }

    public String getCity()
    {
        return fullAddress.split(" ")[1];
    }

    public String getState()
    {
        return fullAddress.split(" ")[2];
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
