package com.example.impressmap.adapter.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.GObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public abstract class MapAdapter
{
    public static final int ZOOM = 18;
    public static final int MIN_ZOOM = 10;

    protected final List<Marker> markers = new ArrayList<>();
    protected final List<Circle> circles = new ArrayList<>();
    private final GoogleMap googleMap;
    protected Marker lastSelectedMarker;
    protected Circle lastSelectedCircle;
    protected Marker pointer = null;
    private GoogleMap.OnMapClickListener onMapClickListener;
    private GoogleMap.OnMapLongClickListener onMapLongClickListener;
    private GoogleMap.OnMarkerClickListener onMarkerClickListener;
    private GoogleMap.OnMarkerDragListener onMarkerDragListener;
    private GoogleMap.OnCircleClickListener onCircleClickListener;

    public MapAdapter(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        removeListeners();
    }

    public Marker addMarker(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        Marker marker = googleMap.addMarker(new MarkerOptions().title(gMarkerMetadata.getTitle())
                                                               .position(
                                                                       gMarkerMetadata.getPosition()));
        markers.add(marker);
        return marker;
    }

    public Circle addCircle(@NonNull GCircleMeta GCircleMeta)
    {
        Circle circle = googleMap.addCircle(new CircleOptions().center(GCircleMeta.getCenter()));
        circles.add(circle);
        return circle;
    }

    public void setPointer(LatLng latLng)
    {
        pointer = googleMap.addMarker(new MarkerOptions().position(latLng));
    }

    public void removePointer()
    {
        if (pointer != null)
        {
            pointer.remove();
            pointer = null;
        }
    }

    public void clearMap()
    {
        googleMap.clear();
        markers.clear();
        circles.clear();
    }

    public void deselectLastMarker()
    {
        if (lastSelectedMarker != null)
        {
            deselectObject((GObject) lastSelectedMarker.getTag());
        }
    }

    public void deselectLastCircle()
    {
        if (lastSelectedCircle != null)
        {
            deselectObject((GObject) lastSelectedCircle.getTag());
        }
    }

    private void deselectObject(GObject gObject)
    {
        gObject.setSelected(false);
    }

    public void zoomTo(LatLng latLng)
    {
        zoomTo(latLng, ZOOM);
    }

    public void zoomTo(LatLng latLng,
                       int zoom)
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void showMarkers()
    {
        toggleMarkersVisibility(true);
    }

    public void hideMarkers()
    {
        toggleMarkersVisibility(false);
    }

    private void toggleMarkersVisibility(boolean visible)
    {
        for (Marker marker : markers)
        {
            marker.setVisible(visible);
        }
    }

    protected void onMapLongClicked(LatLng latLng)
    {
        onMapLongClickListener.onMapLongClick(latLng);
    }

    protected void onMapClicked(LatLng latLng)
    {
        onMapClickListener.onMapClick(latLng);
    }

    protected boolean onMarkerClicked(Marker marker)
    {
        onMarkerClickListener.onMarkerClick(marker);
        return true;
    }

    protected void onCircleClicked(Circle circle)
    {
        onCircleClickListener.onCircleClick(circle);
    }

    @NonNull
    public CameraPosition getCameraPosition()
    {
        return googleMap.getCameraPosition();
    }

    public void setOnCameraIdleListener(@Nullable GoogleMap.OnCameraIdleListener listener)
    {
        googleMap.setOnCameraIdleListener(listener);
    }

    public void setOnMapClickListener(@Nullable GoogleMap.OnMapClickListener listener)
    {
        onMapClickListener = listener;
    }

    public void setOnMapLongClickListener(@Nullable GoogleMap.OnMapLongClickListener listener)
    {
        onMapLongClickListener = listener;
    }

    public void setOnMarkerClickListener(@Nullable GoogleMap.OnMarkerClickListener listener)
    {
        onMarkerClickListener = listener;
    }

    public void setOnMarkerDragListener(@Nullable GoogleMap.OnMarkerDragListener listener)
    {
        onMarkerDragListener = listener;
    }

    public void setOnCircleClickListener(GoogleMap.OnCircleClickListener listener)
    {
        onCircleClickListener = listener;
    }

    public void removeListeners()
    {
        onMapClickListener = latLng ->
        {
        };
        onMapLongClickListener = latLng ->
        {
        };
        onMarkerClickListener = marker -> false;
        onMarkerDragListener = new GoogleMap.OnMarkerDragListener()
        {
            @Override
            public void onMarkerDrag(@NonNull Marker marker)
            {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker)
            {

            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker)
            {

            }
        };

        onCircleClickListener = circle ->
        {
        };
    }
}
