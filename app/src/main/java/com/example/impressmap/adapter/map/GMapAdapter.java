package com.example.impressmap.adapter.map;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.gcircle.CommonGCircle;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.AddressGMarker;
import com.example.impressmap.model.data.gmarker.CommonGMarker;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class GMapAdapter extends MapAdapter
{
    private final Context context;

    public GMapAdapter(Context context,
                       GoogleMap googleMap)
    {
        super(googleMap);
        this.context = context;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(@NonNull LatLng latLng)
            {
                deselectLastMarker();

                onMapClicked(latLng);
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng)
            {
                onMapLongClicked(latLng);
            }
        });

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {
                /*if (googleMap.getCameraPosition().zoom < MIN_ZOOM)
                {
                    hideMarkers();
                }
                else
                {
                    showMarkers();
                }*/
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker)
            {
                Object markerTag = marker.getTag();
                if (markerTag == null)
                {
                    return false;
                }

                deselectLastMarker();

                lastSelectedMarker = marker;

                googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM));

                GMarker gMarker = (GMarker) markerTag;
                gMarker.setSelected(true);

                return onMarkerClicked(marker);
            }
        });

        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener()
        {
            @Override
            public void onCircleClick(@NonNull Circle circle)
            {
                Object circleTag = circle.getTag();
                if (circleTag == null)
                {
                    return;
                }

                deselectLastCircle();

                lastSelectedCircle = circle;

                GCircle gCircle = (GCircle) circleTag;
                gCircle.setSelected(true);

                onCircleClicked(circle);
            }
        });
    }

    public void setItems(List<GMarkerMetadata> gMarkerMetadataList)
    {
        clearMap();

        for (GMarkerMetadata gMarkerMetadata : gMarkerMetadataList)
        {
            addMarker(gMarkerMetadata);
            if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                GCircleMeta gCircleMeta = new GCircleMeta();
                gCircleMeta.setAddressId(gMarkerMetadata.getId());
                gCircleMeta.setCenter(gMarkerMetadata.getPosition());
                addCircle(gCircleMeta);
            }
        }
    }

    public Marker addMarker(GMarkerMetadata gMarkerMetadata)
    {
        Marker marker = super.addMarker(gMarkerMetadata);

        GMarker gMarker;
        switch (gMarkerMetadata.getType())
        {
            case GMarkerMetadata.ADDRESS_MARKER:
                gMarker = new AddressGMarker(context, marker, gMarkerMetadata);
                break;
            default:
                gMarker = new CommonGMarker(context, marker, gMarkerMetadata);
                break;
        }

        marker.setTag(gMarker);
        return marker;
    }

    @Override
    public Circle addCircle(GCircleMeta gCircleMeta)
    {
        Circle circle = super.addCircle(gCircleMeta);

        GCircle gCircle = new CommonGCircle(context, circle, gCircleMeta);

        circle.setTag(gCircle);
        return circle;
    }
}
