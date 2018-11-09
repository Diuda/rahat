package com.example.codeplayer.rahat_cfd;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.*;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;


public class chatFragment extends Fragment {

    private EditText messageToSend;
    final private String SERVICE_ID = "RAHAT_CFD";
    private ListView messagesView;
    private ImageButton sendButton;
    private ImageButton gpsButton;
    public RecyclerView recyclerView;
    MainActivity act;
    private MessageViewModel messageViewModel;
    LocationManager lm;
    List<messageStruct> messageStructList;
    private ImageButton sosButton;
    public static int tempsize=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageStructList = new ArrayList<>();


        recyclerView = view.findViewById(R.id.messagerecycleview);
        final MessageListAdapter adapter = new MessageListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        sosButton = view.findViewById(R.id.SOSSignal);
        sosButton.setOnClickListener(SOSMessageListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        gpsButton = view.findViewById(R.id.my_location);
        gpsButton.setOnClickListener(myLocationListener);
        messageToSend = view.findViewById(R.id.messageToSend);
//        messageAdapter = new MessageAdapter(getActivity());
//        messagesView =  view.findViewById(R.id.messages_view);
        sendButton = (ImageButton) view.findViewById(R.id.sendButton);
//        messagesView.setAdapter(messageAdapter);
////
        sendButton.setOnClickListener(sendMessageListener);
        act = ((MainActivity) getActivity());

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {

                recyclerView.scrollToPosition(tempsize-1);
            }
        });


        messageViewModel = ViewModelProviders.of(getActivity()).get(MessageViewModel.class);

        Log.i("databasek]chal", "sahoo story dekh");
        messageViewModel.getmAllMessage().observe(getActivity(), new Observer<List<messageStruct>>() {
            @Override
            public void onChanged(@Nullable List<messageStruct> messageStructs) {

                if (messageStructs.isEmpty()) {


                } else {
                    Log.i("databasek]chal", String.valueOf(messageStructs.size()));

                    adapter.setWords(messageStructs);
                    tempsize = messageStructs.size();
                    recyclerView.scrollToPosition(messageStructs.size()-1);
                }

            }


        });







    }



    private View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String message = messageToSend.getText().toString();
            messageToSend.setText("");
            act.sendData(message, null, 0);

            //Logic to send message

        }
    };

    private  View.OnClickListener SOSMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            act.sendData(null,act.connectedList,4);
            Snackbar.make(v.getRootView(),"SOS initiated",Snackbar.LENGTH_SHORT).show();


        }
    };

    private View.OnClickListener myLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

            }

        }
    };

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            String lat  = Double.toString(location.getLatitude());
            String lon  = Double.toString(location.getLongitude());

            act.sendData(lat+"#"+lon, null, 2);
            lm.removeUpdates(mLocationListener);
            lm = null;
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

