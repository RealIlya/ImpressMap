package com.example.impressmap.model.data.gmarker;

import android.content.Context;

import com.example.impressmap.R;
import com.google.android.gms.maps.model.Marker;

public class AddressGMarker extends GMarker
{
    public AddressGMarker(Context context,
                          Marker marker)
    {
        super(context, marker, R.drawable.ic_marker_address_48dp, R.drawable.ic_marker_address_36dp);
    }
}
