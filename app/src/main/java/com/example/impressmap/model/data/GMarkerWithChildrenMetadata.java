package com.example.impressmap.model.data;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

public class GMarkerWithChildrenMetadata extends GMarkerMetadata
{
    private List<GMarkerMetadata> gMarkerMetadata = new ArrayList<>();

    public GMarkerWithChildrenMetadata()
    {
    }

    protected GMarkerWithChildrenMetadata(Parcel in)
    {
        super(in);
        gMarkerMetadata = in.readArrayList(null);
    }

    public static GMarkerWithChildrenMetadata convert(GMarkerMetadata gMarkerMetadata)
    {
        GMarkerWithChildrenMetadata gMarkerWithChildrenMetadata = new GMarkerWithChildrenMetadata();
        gMarkerWithChildrenMetadata.setId(gMarkerMetadata.getId());
        gMarkerWithChildrenMetadata.setTitle(gMarkerMetadata.getTitle());
        gMarkerWithChildrenMetadata.setPositionLatLng(gMarkerMetadata.getPosition());
        gMarkerWithChildrenMetadata.setType(gMarkerMetadata.getType());
        return gMarkerWithChildrenMetadata;
    }

    @Override
    public void writeToParcel(Parcel parcel,
                              int i)
    {
        super.writeToParcel(parcel, i);
        parcel.writeValue(gMarkerMetadata);
    }

    public List<GMarkerMetadata> getGMarkerMetadata()
    {
        return gMarkerMetadata;
    }

    public void addGMarkersMetadata(List<GMarkerMetadata> gMarkerMetadata)
    {
        this.gMarkerMetadata.addAll(gMarkerMetadata);
    }
}
