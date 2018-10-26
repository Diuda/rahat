package com.example.codeplayer.rahat_cfd;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class connectionFragment  extends Fragment {



    MainActivity act;
    View previousSelectedItem;

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;


    private String currentConnection = null;


    Button connect;
    ListView connectionsList;

    private RecyclerView connectionRecyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.frament_connections,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        act= ((MainActivity)getActivity());

//        connectionRecyclerView = view.findViewById(R.id.connectionrecylerview);

        connect  = view.findViewById(R.id.connect);
        connect.setOnClickListener(makeConnectionListener);
        connectionsList = view.findViewById(R.id.connectionsList);
        adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,listItems);
        connectionsList.setAdapter(adapter);
        connectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentConnection = connectionsList.getItemAtPosition(i).toString();

                if (previousSelectedItem!=null) {
                    previousSelectedItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                previousSelectedItem=view;
                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }




        });

        act.advertise();
        act.discover();


    }
    private  View.OnClickListener makeConnectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("CFDPP","Mai chala");

            act.connect(act.connectionNameToId.get(currentConnection));


        }
    };



}