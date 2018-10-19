package com.example.codeplayer.rahat_cfd;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ConnectionsClient mConnectionsClient;
//    private  final Map<String,Endpoint> mDiscoveredEndpoints  = new HashMap<>();
//    private final Map<String, Endpoint> mPendingConnections = new HashMap<>();
//    private final Map<String, Endpoint> mEstablishedConnections = new HashMap<>();


    //initialize DB object

    BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    String deviceName = myDevice.getName();
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    private Set<String> connectedList = new HashSet<>();

    private String currentConnection = null;
    EditText editText;
    Button button;
    Button connect;
    ListView connectionsList;
    TextView textView;
    private boolean mIsConnecting = false;

    /** True if we are discovering. */
    private boolean mIsDiscovering = false;

    /** True if we are advertising. */
    private boolean mIsAdvertising = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }


//        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        //DatabaseInitializer.populateAsync(appDatabase);
        connectionsList = findViewById(R.id.connectionsList);
        mConnectionsClient = Nearby.getConnectionsClient(this);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        connectionsList.setAdapter(adapter);
        connectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                currentConnection = connectionsList.getItemAtPosition(i).toString();

            }
        });


        advertise();
        discover();


    }


    //-----------__PERMISSION CODE_-------------------

    public void checkPermission(){
        //Can add more as per requirement
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);
    }
    //----------------------------------------
    private  void advertise(){

        mConnectionsClient.startAdvertising(deviceName,"com.paddy",mConnectionLifecycleCallback, new AdvertisingOptions(Strategy.P2P_CLUSTER))
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unUsedResult) {

                Log.i("CODEFUNDO","Advertising now -> " + deviceName);

            }
        });
    }

    private void discover(){
        mConnectionsClient.startDiscovery("com.paddy",mEndpointDiscoveryCallback,new DiscoveryOptions(Strategy.P2P_CLUSTER)).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

        Log.i("CODEFUNDO","Discovering now");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.i("CODEFUNDO","Discovering failed : -> " + e.toString());
            }
        });

    }

    private void connect(String advertiserID){

        mConnectionsClient.requestConnection(deviceName,advertiserID,mConnectionLifecycleCallback);
    }


    public void makeConnection(View view){


        connect(currentConnection);


    }
    // ----------------------- Discovery Callback-----------------------------------
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback=
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(
                        String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
                    Log.i("CODEFUNDO","FOUND ENDPOINT: " + endpointId);
                        listItems.add(endpointId);
                        adapter.notifyDataSetChanged();
                }

                @Override
                public void onEndpointLost(String endpointId) {
                    // A previously discovered endpoint has gone away.
                    Log.i("CODEFUNDO"," ENDPOINT LOST: " + endpointId);
                }
            };








    //--------------------------------------------------------------------------------


    // -----------------------Callbacks for connection to devices --------------------------

    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    mConnectionsClient.acceptConnection(endpointId, mPayloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:

                            Log.i("CODEFUNDO","SUCCESFFULL connection");
                            textView = findViewById(R.id.response);
                            textView.setText("Ready to send data");
                            connectedList.add(endpointId);

                            // We're connected! Can now start sending and receiving data.
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:



                            break;
                        default:
                            // The connection was broken before it was accepted.
                            break;
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {

                    connectedList.remove(endpointId);
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.
                }
            };

    //---------------------------------------------------------------------------


    protected  void sendMsg(View view){

        editText = findViewById(R.id.msgPayload);
        String data = editText.getText().toString();
        sendData(data);
        Log.i("CODEFUNDO","SENDING MESSAGE NOW");
    }

    protected  void sendData(String data){


        try {
            mConnectionsClient.sendPayload(new ArrayList<String>(connectedList),Payload.fromBytes(data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {

            Log.e("CODEFUNDO","ERROR in sending " + e.toString());
            e.printStackTrace();
        }
    }
    //-------------_Payload Callbacks -------------------------------------------

    //--------------------------------------------------------------------------


    private final PayloadCallback mPayloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {

                    Log.i("CODEFUNDO","PAYLOAD Receive called");
                    String data = null;
                    try {
                        data = new String(payload.asBytes(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    textView = findViewById(R.id.response);
                    textView.setText(data);
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    // Payload progress has updated.
                }
            };




}
