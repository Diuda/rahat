package com.example.codeplayer.rahat_cfd;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    String deviceName = myDevice.getName();
    FragmentManager fm = getSupportFragmentManager();
    connectionFragment cf;
    ConnectionsClient mConnectionsClient;
    Map<String,String> endpointUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        mConnectionsClient =  Nearby.getConnectionsClient(this);
        endpointUser = new HashMap<String, String>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            Fragment fragment = null;
            if (id == R.id.nav_chat) {

                fragment = new chatFragment();


            } else if (id == R.id.nav_connections) {
                fragment = new connectionFragment();

            }

            if(fragment!=null){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.screen_area,fragment);
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
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





    protected void advertise(){

        mConnectionsClient.startAdvertising(deviceName,"com.paddy",mConnectionLifecycleCallback, new AdvertisingOptions(Strategy.P2P_CLUSTER))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unUsedResult) {

                        Log.i("CODEFUNDO","Advertising now -> " + deviceName);

                    }
                });
    }

    protected void discover(){
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

    protected void connect(String advertiserID){

        mConnectionsClient.requestConnection(deviceName,advertiserID,mConnectionLifecycleCallback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("CFDPP","Successfull request");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Log.i("CFDPP","Failed af on connection request" + e.toString());
            }
        });
        Log.i("CFDPP","Request Connection completed");
    }



    // ----------------------- Discovery Callback-----------------------------------
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback=
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(
                        String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
                    Log.i("CODEFUNDO","FOUND ENDPOINT: " + endpointId);
                    Fragment f;
                    f = getSupportFragmentManager().findFragmentById(R.id.screen_area);
                    if(f instanceof connectionFragment) {
                        cf = (connectionFragment) getSupportFragmentManager().findFragmentById(R.id.screen_area);
                        cf.listItems.add(endpointId);
                        cf.adapter.notifyDataSetChanged();
                         }
                         endpointUser.put(endpointId,discoveredEndpointInfo.getEndpointName());
                    Toast.makeText(getApplicationContext(),"Found new user in vicinity!",Toast.LENGTH_SHORT).show();


                    // ----Change to connection screen

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

                    Log.i("CODEFUNDO","Initiated connection");

                    mConnectionsClient.acceptConnection(endpointId, mPayloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:

                            Log.i("CODEFUNDO","SUCCESFFULL connection");

                            cf.connectedList.add(endpointId);
                            chatFragment cft = new chatFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.screen_area,cft,"FragmentTAG");
                            ft.commit();


                            // We're connected! Can now start sending and receiving data.
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:

                            Log.i("CODEFUNDO","Connection Rejected");

                            break;
                        default:

                            Log.i("CODEFUNDO","Connection default Rejected");
                            // The connection was broken before it was accepted.
                            break;
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {

                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.

                    Log.i("CODEFUNDO","Disconnected connection");


                }
            };

    private final PayloadCallback mPayloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {

                    Log.i("CODEFUNDO","PAYLOAD Receive called");
                    String data = null;
                    try {
                        chatFragment ct = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.screen_area);
                        data = new String(payload.asBytes(),"UTF-8");
                        Set<String> connections =  new HashSet<String>();

                        for (String connectionID: cf.connectedList) {

                            if(!connectionID.equals(endpointId)){

                                connections.add(connectionID);
                            }

                        }
                        Log.i("CFDPPP",connections.toString());

                        ct.messageAdapter.add(new Message(data,new MemberData(endpointUser.get(endpointId), "Red"),false));
                        if(!connections.isEmpty())
                        sendData(data,ct.messageAdapter,connections);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    // Payload progress has updated.
                }
            };

    protected  void sendData(String data,MessageAdapter messageAdapter,Set<String> connectedList){


        try {
            if(connectedList==null){
                connectedList = cf.connectedList;
            }
            messageAdapter.add(new Message(data,new MemberData("Paddy", "Green"),true));
            Log.i("CFDPP","Message Adapter completed");
            mConnectionsClient.sendPayload(new ArrayList<String>(connectedList),Payload.fromBytes(data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {

            Log.e("CODEFUNDO","ERROR in sending " + e.toString());
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------



}
