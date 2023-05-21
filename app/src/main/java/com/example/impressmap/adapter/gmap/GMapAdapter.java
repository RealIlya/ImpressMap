package com.example.impressmap.adapter.gmap;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.gcircle.CommonGCircle;
import com.example.impressmap.model.data.gcircle.GCircle;
import com.example.impressmap.model.data.gmarker.AddressGMarker;
import com.example.impressmap.model.data.gmarker.CommonGMarker;
import com.example.impressmap.model.data.gmarker.GMarker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
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

        CameraPosition lastCameraPosition = viewModel.getLastCameraPosition();
        if (lastCameraPosition != null)
        {
            zoomTo(lastCameraPosition);
        }

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {
                if (googleMap.getCameraPosition().zoom < MIN_VISIBLE)
                {
                    viewModel.hideMarkers();
                }
                else
                {
                    viewModel.showMarkers();
                }

                onCameraMoved();
            }
        });

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

                    animateZoomTo(marker.getPosition());

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

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                viewModel.setLastCameraPosition(googleMap.getCameraPosition());
            }
        });
    }

    public void addZone(@NonNull List<GMarkerMetadata> gMarkerMetadataList)
    {
        List<GMarker> gMarkers = new ArrayList<>();
        GCircleMeta gCircleMeta = new GCircleMeta();

        GCircle gCircle = null;

        for (GMarkerMetadata gMarkerMetadata : gMarkerMetadataList)
        {
            if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
            {
                gCircle = viewModel.findGCircleByGMarkerMetadata(gMarkerMetadata);

                if (gCircle == null)
                {
                    gCircleMeta.setAddressId(gMarkerMetadata.getId());
                    gCircleMeta.setCenter(gMarkerMetadata.getPosition());
                }
            }

            if (viewModel.inGMarkersCache(gMarkerMetadata))
            {
                continue;
            }

            GMarker gMarker = (GMarker) addMarker(gMarkerMetadata).getTag();
            if (gCircle != null)
            {
                gMarker.setClickable(true);
            }
            gMarkers.add(gMarker);
        }

        if (gCircle == null)
        {
            gCircleMeta.addGMarkers(gMarkers);
            addCircle(gCircleMeta);
        }
        else
        {
            gCircle.getGCircleMeta().addGMarkers(gMarkers);
        }
    }

    @Override
    public Marker addMarker(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        Marker marker = super.addMarker(gMarkerMetadata);

        GMarker gMarker;
        if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
        {
            gMarker = new AddressGMarker(context, marker, gMarkerMetadata);
        }
        else
        {
            gMarker = new CommonGMarker(context, marker, gMarkerMetadata);
        }

        viewModel.addGMarkerInCache(gMarker);
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

        viewModel.addGCircleInCache(gCircle);
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

    @Nullable
    public LatLng getGCircleLatLng(Address address)
    {
        return viewModel.getGCircle(address).getGCircleMeta().getCenter();
    }

    public void animateZoomTo(Address address)
    {
        animateZoomTo(getGCircleLatLng(address));
    }

    public void animateZoomTo(Address address,
                              OnFinishCallback callback)
    {
        animateZoomTo(getGCircleLatLng(address), callback);
    }

    @Override
    public void clearMap()
    {
        super.clearMap();
        viewModel.clearCache();
    }
}
