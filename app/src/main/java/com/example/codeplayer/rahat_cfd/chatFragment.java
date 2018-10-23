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
    protected MessageAdapter messageAdapter;
    final private String SERVICE_ID = "RAHAT_CFD";
    private ListView messagesView;
    private ImageButton sendButton;
    private ImageButton gpsButton;
    private RecyclerView recyclerView;
    MainActivity act;
    private MessageViewModel messageViewModel;
    LocationManager lm;
    List<messageStruct> messageStructList;



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


        messageViewModel = ViewModelProviders.of(getActivity()).get(MessageViewModel.class);

        Log.i("databasek]chal", "sahoo story dekh");
        messageViewModel.getmAllMessage().observe(getActivity(), new Observer<List<messageStruct>>() {
            @Override
            public void onChanged(@Nullable List<messageStruct> messageStructs) {

                if (messageStructs.isEmpty()) {


                } else {
                    Log.i("databasek]chal", String.valueOf(messageStructs.size()));

                    adapter.setWords(messageStructs);
                }

            }


        });

        if(isNetworkAvailable()){


            try {
                getRestMessages(adapter);

//                Log.i("scenekyahai", String.valueOf(messageStructList.size()));
//                adapter.setWords(messageStructList);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else{

        }





    }


    private View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String message = messageToSend.getText().toString();
            messageToSend.setText("");
            act.sendData(message, messageAdapter, null, 0);

            //Logic to send message

        }
    };

    private View.OnClickListener myLocationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
    };

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            String lat  = Double.toString(location.getLatitude());
            String lon  = Double.toString(location.getLongitude());

            act.sendData(lat+"#"+lon, messageAdapter, null, 2);
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void getRestMessages(final MessageListAdapter adapter) throws JSONException {
        RestClient.get("/getData", null, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline

                try {
                    JSONObject firstEvent = timeline.getJSONObject(0);
                    String username = firstEvent.getString("username");
                    String message = firstEvent.getString("message");


                    for(int i=0;i<timeline.length(); i++){

                        JSONObject data = timeline.getJSONObject(i);
                        String usermessage = data.getString("message");
                        Toast.makeText(getContext(), usermessage, Toast.LENGTH_LONG).show();

                        messageStructList.add(i, new messageStruct(data.getString("username"), data.getString("message"), 0));


//                        messageStruct messageStruct = new messageStruct();
                    }

                    adapter.setWords(messageStructList);



                    // Do something with the response
                    System.out.println(username);

                }
                catch (JSONException e){
                    Log.e("APIERROR", "Kuch fata");
                }
            }
        });
    }


}

