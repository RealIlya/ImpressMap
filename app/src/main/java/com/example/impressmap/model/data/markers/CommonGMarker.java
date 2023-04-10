package com.example.impressmap.model.data.markers;

import android.content.Context;

import com.example.impressmap.R;
import com.google.android.gms.maps.model.Marker;

public class CommonGMarker extends GMarker
{
    public CommonGMarker(Context context,
                         Marker marker)
    {
        super(context, marker, R.drawable.ic_marker_common_48dp, R.drawable.ic_marker_common_36dp);
    }
}
