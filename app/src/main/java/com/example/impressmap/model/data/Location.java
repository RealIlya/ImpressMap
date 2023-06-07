package com.example.impressmap.model.data;


import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Думаю есть смысл разбить полный адрес на составляющие в конструкторе и потом их отдавать, а не каждый раз преобразовывать данные при запросе
public class Location extends Address
{
    private String fullAddress = "";

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
}
