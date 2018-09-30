package app.android.adam.androidapp.maps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationChangeListener implements LocationListener {

    private final GoogleMap googleMap;


    public LocationChangeListener(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        googleMap.clear();

        LatLng actual = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(actual).title("You are here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(actual));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // no need
    }

    @Override
    public void onProviderEnabled(String provider) {
        // no need
    }

    @Override
    public void onProviderDisabled(String provider) {
        googleMap.clear();
    }
}
