package com.example.codeplayer.rahat_cfd;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

abstract public class LocationAnalyzer {


    static List<LocationData> coordinatorList;
    private static MarkerOptions options;

    private static ArrayList<LatLng> latlngs;


    public static void parseLocationData(String data){




        String [] locations = data.split("###");
        LocationData [] ld = new LocationData[3];
        int i=0;
        for (String location: locations){

            String [] locationData = location.split("#");

            ld[i].setLat(locationData[0]);
            ld[i].setLon(locationData[1]);
            ld[i].setDist(locationData[2]);
            coordinatorList.add(ld[i]);

            i++;

        }
        latlngs = new ArrayList<>();
        options  = new MarkerOptions();






    }

    public static void plotData(GoogleMap googleMap){
        for(LocationData ele: coordinatorList){

            latlngs.add(new LatLng(ele.getLat(),ele.getLon()));
        }

        for (LatLng point : latlngs) {
            options.position(point);
            options.title("1");
            options.snippet("Person");
            googleMap.addMarker(options);
        }



    }
}

class LocationData{


    private Double lon;
    private Double lat;
    private Double dist;

    public void setLon(String lon) {
        this.lon = Double.parseDouble(lon);
    }

    public void setLat(String lat) {


        this.lat = Double.parseDouble(lat);
    }

    public void setDist(String dist) {


        this.dist = Double.parseDouble(dist);
    }

    public Double getDist() {
        return dist;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}

