package com.example.impressmap.ui.util;

import com.example.impressmap.model.data.GMarkerMetadata;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

public class GMarkersRepository
{
    private static List<GMarkerMetadata> gMarkerMetadata = Arrays.asList(
            new GMarkerMetadata(0, "Троллейный жилмассив", new LatLng(54.989938, 82.840130),
                    GMarkerMetadata.COMMON_MARKER),
            new GMarkerMetadata(1, "Кировский район", new LatLng(54.971844, 82.892546),
                    GMarkerMetadata.COMMON_MARKER));

    public static List<GMarkerMetadata> getGMarkerMetadata()
    {
        return gMarkerMetadata;
    }
}
