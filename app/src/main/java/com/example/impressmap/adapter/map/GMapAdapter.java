package com.example.impressmap.adapter.map;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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

import java.util.ArrayList;
import java.util.List;

public class GMapAdapter extends MapAdapter
{
    private final Context context;
    private final GMapAdapterViewModel viewModel;

    public GMapAdapter(Context context,
                       GoogleMap googleMap,
                       ViewModelStoreOwner viewModelStoreOwner)
    {
        super(googleMap);
        this.context = context;

        viewModel = new ViewModelProvider(viewModelStoreOwner).get(GMapAdapterViewModel.class);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(@NonNull LatLng latLng)
            {
                viewModel.deselectLastGMarker();

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

                GMarker gMarker = (GMarker) markerTag;

                if (gMarker.isClickable())
                {
                    viewModel.deselectLastGMarker();
                    gMarker.setSelected(true);
                    viewModel.setLastSelectedGMarker(gMarker);

                    googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM));

                    return onMarkerClicked(marker);
                }

                return true;
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

                GCircle gCircle = (GCircle) circleTag;
                viewModel.deselectLastGCircle();
                gCircle.setSelected(true);
                viewModel.setLastSelectedGCircle(gCircle);

                onCircleClicked(circle);
            }
        });
    }

    public void addZone(@NonNull List<GMarkerMetadata> gMarkerMetadataList)
    {
        List<GMarker> gMarkers = new ArrayList<>();
        GCircleMeta gCircleMeta = new GCircleMeta();
        for (GMarkerMetadata gMarkerMetadata : gMarkerMetadataList)
        {
            gMarkers.add((GMarker) addMarker(gMarkerMetadata).getTag());
            if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                gCircleMeta.setAddressId(gMarkerMetadata.getId());
                gCircleMeta.setCenter(gMarkerMetadata.getPosition());
            }
        }

        gCircleMeta.setGMarkers(gMarkers);

        addCircle(gCircleMeta);
    }

    public Marker addMarker(@NonNull GMarkerMetadata gMarkerMetadata)
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
    public Circle addCircle(@NonNull GCircleMeta gCircleMeta)
    {
        Circle circle = super.addCircle(gCircleMeta);

        GCircle gCircle = new CommonGCircle(context, circle, gCircleMeta);

        if (viewModel.isGCircleEqualsLastSelectedGCircle(gCircle))
        {
            gCircle.setSelected(true);
            viewModel.setLastSelectedGCircle(gCircle);
        }

        circle.setTag(gCircle);
        return circle;
    }

    public boolean inSelectedGCircle(LatLng latLng)
    {
        return viewModel.inLastSelectedGCircle(latLng);
    }

    public void deselectGMarker()
    {
        viewModel.deselectLastGMarker();
    }

    public void deselectGCircle()
    {
        viewModel.deselectLastGCircle();
    }

    @Nullable
    public List<GMarker> getSelectedGCircleGMarkers()
    {
        return viewModel.getLastSelectedGCircleGMarkers();
    }
}
