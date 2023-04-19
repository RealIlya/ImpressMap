package com.example.impressmap.model.data;

import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.POSITION_NODE;
import static com.example.impressmap.util.Constants.Keys.TITLE_NODE;
import static com.example.impressmap.util.Constants.Keys.TYPE_NODE;

import androidx.room.Entity;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

// for database store
@Entity
public class GMarkerMetadata implements TransferableToDatabase
{
    public static final int ADDRESS_MARKER = 0, COMMON_MARKER = 1;

    private String id = "";
    private String title = "";
    private LatLng latLng = new LatLng(0, 0);
    private int type = -1;

    public Map<String, Object> prepareToTransferToDatabase()
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, id);
        data.put(TITLE_NODE, title);
        data.put(POSITION_NODE, String.format("%s %s", latLng.latitude, latLng.longitude));
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

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
