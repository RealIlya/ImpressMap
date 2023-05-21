package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.DESC_NODE;
import static com.example.impressmap.util.Constants.Keys.FULL_ADDRESS_NODE;
import static com.example.impressmap.util.Constants.Keys.NOT_PUBLIC_NODE;
import static com.example.impressmap.util.Constants.Keys.OWNER_ID_NODE;

import androidx.annotation.Nullable;
import androidx.room.Entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Address implements TransferableToDatabase
{
    private String id = "";
    private String desc = "";
    private String ownerId = "";
    private String fullAddress = "";
    private boolean notPublic = false; // private

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
        data.put(NOT_PUBLIC_NODE, notPublic);

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

    public String getFullAddress()
    {
        return fullAddress.replaceAll("\\|", " ");
    }

    public void setFullAddress(String fullAddress)
    {
        this.fullAddress = fullAddress;
    }

    public String getFullAddressReversed()
    {
        List<String> list = Arrays.asList(fullAddress.split("\\|"));
        Collections.reverse(list);

        return String.join(" ", list);
    }

    public String getCountry()
    {
        return fullAddress.split("\\|")[0];
    }

    public String getCity()
    {
        return fullAddress.split("\\|")[1];
    }

    public String getState()
    {
        return fullAddress.split("\\|")[2];
    }

    public String getStreet()
    {
        return fullAddress.split("\\|")[3];
    }

    public String getHouse()
    {
        return fullAddress.split("\\|")[4];
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public boolean isNotPublic()
    {
        return notPublic;
    }

    public void setNotPublic(boolean notPublic)
    {
        this.notPublic = notPublic;
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
