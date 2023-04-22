package com.example.impressmap.model.data.gmarker;

import android.content.Context;

import com.example.impressmap.R;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.google.android.gms.maps.model.Marker;

public class AddressGMarker extends GMarker
{
    public AddressGMarker(Context context,
                          Marker marker,
                          GMarkerMetadata gMarkerMetadata)
    {
        super(context, marker, gMarkerMetadata, R.drawable.ic_marker_address_48dp,
                R.drawable.ic_marker_address_36dp);
    }
}
