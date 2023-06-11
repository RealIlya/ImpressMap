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

        googleMap.setOnCameraMoveListener(() ->
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
        });

        googleMap.setOnMapClickListener(latLng ->
        {
            viewModel.deselectLastGMarker();

            onMapClicked(latLng);
        });

        googleMap.setOnMapLongClickListener(this::onMapLongClicked);

        googleMap.setOnMarkerClickListener(marker ->
        {
            Object markerTag = marker.getTag();
            if (markerTag == null)
            {
                return false;
            }

            GMarker gMarker = (GMarker) markerTag;

            if (inSelectedGCircle(gMarker.getGMarkerMetadata().getPositionLatLng()))
            {
                viewModel.deselectLastGMarker();
                gMarker.setSelected(true);
                viewModel.setLastSelectedGMarker(gMarker);

                animateZoomTo(marker.getPosition());

                return onMarkerClicked(marker);
            }

            return true;
        });

        googleMap.setOnCircleClickListener(circle ->
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
        });

        googleMap.setOnCameraIdleListener(
                () -> viewModel.setLastCameraPosition(googleMap.getCameraPosition()));
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
                    gCircleMeta.setCenter(gMarkerMetadata.getPositionLatLng());
                }
            }

            if (viewModel.inGMarkersCache(gMarkerMetadata))
            {
                continue;
            }

            GMarker gMarker = (GMarker) addMarker(gMarkerMetadata).getTag();
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
    public List<GMarkerMetadata> getSelectedGCircleGMarkerMetadata()
    {
        List<GMarkerMetadata> gMarkerMetadataList = new ArrayList<>();

        List<GMarker> lastSelectedGCircleGMarkers = viewModel.getLastSelectedGCircleGMarkers();
        if (lastSelectedGCircleGMarkers == null)
        {
            return null;
        }

        for (GMarker gMarker : lastSelectedGCircleGMarkers)
        {
            gMarkerMetadataList.add(gMarker.getGMarkerMetadata());
        }

        return gMarkerMetadataList;
    }

    @Nullable
    public LatLng getGCircleLatLng(Address address)
    {
        GCircle gCircle = viewModel.getGCircle(address);
        if (gCircle != null)
        {
            return gCircle.getGCircleMeta().getCenter();
        }
        return null;
    }

    public void animateZoomTo(Address address,
                              GoogleMap.CancelableCallback callback)
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
