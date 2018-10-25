package com.example.codeplayer.rahat_cfd;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

abstract public class LatLong {


    private static String currentLong;
    private static String currentLat;
    private static LocationManager lm;
    private static CoordinatesReadyListener listener;

    public static void setListener(CoordinatesReadyListener coordsListener){
        listener = coordsListener;
    }


    public static void getCurrentLocation(Activity act, Context context) {


        lm = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Log.i("LocationTag","Getting current location for user");

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);


    }
        private final static LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                Log.i("LocationTag","Location changed");

                currentLat  = Double.toString(location.getLatitude());
                currentLong  = Double.toString(location.getLongitude());

                lm.removeUpdates(mLocationListener);
                lm = null;
                listener.onCoordinatesReady(currentLat,currentLong);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }
