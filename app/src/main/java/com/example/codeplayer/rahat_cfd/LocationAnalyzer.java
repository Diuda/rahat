package com.example.codeplayer.rahat_cfd;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

public class LocationAnalyzer {


    private   ArrayList<LocationData> coordinatorList;


    private  ArrayList<LatLng> latlngs;


     public LocationAnalyzer() {

         this.coordinatorList  = new ArrayList<>();

         this.latlngs = new ArrayList<>();
     }

     public  void parseLocationData(String data, Context context){


        String [] locations = data.split("###");
        LocationData ld = new LocationData();

        for (int i=0;i<locations.length;i++){
            String location  = locations[i];
            String [] locationData = location.split("#");
            Toast.makeText(context,"That"+Arrays.toString(locationData),Toast.LENGTH_LONG).show();

            ld.setLat(locationData[1]);
            ld.setLon(locationData[2]);
            ld.setDist(locationData[3]);
            coordinatorList.add(ld);

            Log.v("ERRRSize",String.valueOf(coordinatorList.size()));



        }
    }

    public ArrayList<LocationData> plotData(){

//        if(this.coordinatorList.size()==0){
//            Toast.makeText(context,"No coordinates available",Toast.LENGTH_LONG).show();
//            return null;
//        }
//        for(LocationData ele: coordinatorList){
//
//            Log.d("WORKING",String.valueOf(coordinatorList.size()));
//            this.latlngs.add(new LatLng(ele.getLat(),ele.getLon()));
//        }
//
//        return new LatLng(latlngs.get(0).latitude,latlngs.get(0).longitude);

        return coordinatorList;

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

