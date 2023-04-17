package com.example.impressmap.adapter.map;

import androidx.annotation.Nullable;

import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.markers.GMarker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
    protected Marker lastSelected;
    protected Marker pointer = null;

    private GoogleMap googleMap;

    private GoogleMap.OnMapClickListener onMapClickListener;
    private GoogleMap.OnMapLongClickListener onMapLongClickListener;
    private GoogleMap.OnMarkerClickListener onMarkerClickListener;
    private GoogleMap.OnMarkerDragListener onMarkerDragListener;

    public MapAdapter(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    public Marker addMarker(GMarkerMetadata gMarkerMetadata)
    {
        Marker marker = googleMap.addMarker(new MarkerOptions().title(gMarkerMetadata.getTitle())
                                                               .position(
                                                                       gMarkerMetadata.getLatLng()));
        markers.add(marker);
        return marker;
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
    }

    public void deselectLastMarker()
    {
        if (lastSelected != null)
        {
            GMarker gMarker = (GMarker) lastSelected.getTag();
            gMarker.setSelected(false);
        }
    }

    private void toggleMarkersVisibility(boolean visible)
    {
        for (Marker marker : markers)
        {
            marker.setVisible(visible);
        }
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
}
