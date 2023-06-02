package com.example.impressmap.adapter.gmap;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class MapAdapter
{
    public static final int MIN_ZOOM = 19;
    public static final int MIN_VISIBLE = 16;

    private final GoogleMap googleMap;
    protected Marker pointer = null;
    private GoogleMap.OnCameraMoveListener onCameraMoveListener;
    private GoogleMap.OnMapClickListener onMapClickListener;
    private GoogleMap.OnMapLongClickListener onMapLongClickListener;
    private GoogleMap.OnMarkerClickListener onMarkerClickListener;
    private GoogleMap.OnCircleClickListener onCircleClickListener;

    public MapAdapter(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        removeListeners();
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    public Marker addMarker(@NonNull GMarkerMetadata gMarkerMetadata)
    {
        return googleMap.addMarker(new MarkerOptions().title(gMarkerMetadata.getTitle())
                                                      .position(gMarkerMetadata.getPosition()));
    }

    public Circle addCircle(@NonNull GCircleMeta GCircleMeta)
    {
        return googleMap.addCircle(new CircleOptions().center(GCircleMeta.getCenter()));
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
    }

    public void animateZoomTo(LatLng latLng)
    {
        animateZoomTo(latLng, () ->
        {

        });
    }

    public void animateZoomTo(LatLng latLng,
                              GoogleMap.CancelableCallback callback)
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                Math.max(googleMap.getCameraPosition().zoom, MIN_ZOOM)), callback);
    }

    public void animateZoomTo(LatLng latLng,
                              OnFinishCallback callback)
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                Math.max(googleMap.getCameraPosition().zoom, MIN_ZOOM)), callback);
    }

    public void animateZoomTo(CameraPosition cameraPosition)
    {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void zoomTo(CameraPosition cameraPosition)
    {
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void onCameraMoved()
    {
        onCameraMoveListener.onCameraMove();
    }

    protected void onMapClicked(LatLng latLng)
    {
        onMapClickListener.onMapClick(latLng);
    }

    protected void onMapLongClicked(LatLng latLng)
    {
        onMapLongClickListener.onMapLongClick(latLng);
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

    public void setOnCameraMoveListener(GoogleMap.OnCameraMoveListener listener)
    {
        this.onCameraMoveListener = listener;
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

    public void setOnCircleClickListener(GoogleMap.OnCircleClickListener listener)
    {
        onCircleClickListener = listener;
    }

    public void removeListeners()
    {
        onCameraMoveListener = () ->
        {
        };
        onMapClickListener = latLng ->
        {
        };
        onMapLongClickListener = latLng ->
        {
        };
        onMarkerClickListener = marker -> false;
        onCircleClickListener = circle ->
        {
        };
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"})
    public void setMyLocationEnabled(boolean enabled)
    {
        googleMap.setMyLocationEnabled(enabled);
    }

    public void animateZoomToMyLocation()
    {
        Location myLocation = googleMap.getMyLocation();
        if (myLocation == null)
        {
            return;
        }
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        animateZoomTo(new LatLng(latitude, longitude));
    }

    public interface OnFinishCallback extends GoogleMap.CancelableCallback
    {
        @Override
        default void onCancel()
        {
        }
    }
}
