package com.example.codeplayer.rahat_cfd;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import java.util.UUID;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private MessageViewModel messageViewModel;

    BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
    String deviceName = myDevice.getName();
    FragmentManager fm = getSupportFragmentManager();
    connectionFragment cf;
    ConnectionsClient mConnectionsClient;
    Map<String,String> endpointUser ;
    Map<String,Double> distanceMapper;
    ParsedMessagePayload parser;
    AckParser ackParser;
    private TabAdapter tabAdapter;
    private ViewPager pager;
    private TabLayout tabs;
    private int[] tabIcons = {
            R.drawable.ic_bluetooth_connected_black_24dp,
            R.drawable.ic_chat_black_24dp,
            R.drawable.ic_location_on_black_24dp
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }



        //-----------------------------------------------------------
        //--------__ Instantiating Objects__---------------------------------

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        mConnectionsClient =  Nearby.getConnectionsClient(this);
        endpointUser = new HashMap<String, String>();
        parser = new ParsedMessagePayload();
        ackParser = new AckParser();
        distanceMapper = new HashMap<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();



//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        pager = findViewById(R.id.viewPager);
        tabs = findViewById(R.id.tabs);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), this);
        tabAdapter.addFragment(new connectionFragment(), "Connections", tabIcons[0]);
        tabAdapter.addFragment(new chatFragment(), "Chat", tabIcons[1]);
        tabAdapter.addFragment(new MapFragment(), "Map", tabIcons[2]);


        pager.setAdapter(tabAdapter);
        tabs.setupWithViewPager(pager);

        highLightCurrentTab(0);



//        tabs.getTabAt(0).setIcon(tabIcons[0]);
//        tabs.getTabAt(1).setIcon(tabIcons[1]);
//        tabs.getTabAt(2).setIcon(tabIcons[2]);
//        navigationView.setNavigationItemSelectedListener(this);

    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            TabLayout.Tab tab = tabs.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(tabAdapter.getTabView(i));
        }
        TabLayout.Tab tab = tabs.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(tabAdapter.getSelectedTabView(position));
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
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
        else if(id==R.id.nav_location){
            fragment = new MapFragment();
        }

        if(fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area,fragment);
            ft.commit();
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
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
                        Log.i("ENDPOINTNAME",discoveredEndpointInfo.getEndpointName());
                    Log.i("ENDPOINTNAME",deviceName);
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

                        //---------------FIX ASAP------------------------------
                        //To be corrected: Will crash if message appears and
                        //User is on Connections screen
                        chatFragment ct = (chatFragment) getSupportFragmentManager().findFragmentById(R.id.screen_area);

                        //---------------------------------------------------

                        //Parsing initially to check for ACK
                        parser.parseData(payload);
                        int messageType = parser.getMessageType();

                        if(messageType==1){


                            ackParser.parseAckMessage(payload);
                            double distance = ackParser.findDistance();
                            Log.i("DISTANCECFD",Double.toString(distance));
                            //Add code to calculate Mean and Variance
                            //Get location in Real Time
                            return;

                        }
                        else if (messageType==2){

                            messageStruct messageStruct = new messageStruct(UUID.randomUUID().toString(),endpointUser.get(endpointId),LocationParser.getLatitude()+","+LocationParser.getLongitude(),2);
                            messageViewModel.insert(messageStruct);

                            return;



                        }


                        //------- FIX---------------------------
                        //Change to use a better Data Structure


                        HashSet<String> endpointConnection = new HashSet<String>();
                        endpointConnection.add(endpointId);
                        Log.i("ACKENDPT",endpointConnection.toString());
                        sendData(null,ct.messageAdapter,endpointConnection,1);
                        //----------------------------------


                        //If not an ack send ack
                        String parsedData = parser.getData();

                        messageStruct messageStruct = new messageStruct(UUID.randomUUID().toString(),endpointUser.get(endpointId),parsedData,1);
                        messageViewModel.insert(messageStruct);



                        Set<String> connections =  new HashSet<String>();

                        for (String connectionID: cf.connectedList) {

                            if(!connectionID.equals(endpointId)){

                                connections.add(connectionID);
                            }

                        }
                        Log.i("CFDPPP",connections.toString());

                        ct.messageAdapter.add(new Message(parsedData,new MemberData(endpointUser.get(endpointId), "Red"),0));
                        if(!connections.isEmpty())
                        sendData(parsedData,ct.messageAdapter,connections,0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    // Payload progress has updated.
                }
            };

    protected  void sendData(String data,MessageAdapter messageAdapter,Set<String> connectedList,int messageType){


        try {
            if(connectedList==null){
                if(cf==null){
                    Toast.makeText(getApplicationContext(),"Please connect to atleast one peer",Toast.LENGTH_SHORT).show();
                    return;
                }
                connectedList = cf.connectedList;
                if(connectedList==null){
                    Toast.makeText(getApplicationContext(),"Please connect to atleast one peer",Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String messageTypeString  = String.valueOf(messageType);
            Log.i("ACKVAL-SENDING",messageTypeString);
            String uuid = UUID.randomUUID().toString();
            //Send user message data

            if(messageType==0) {

                messageStruct messageStruct = new messageStruct(uuid,deviceName,data,messageType);
                messageViewModel.insert(messageStruct);
                mConnectionsClient.sendPayload(new ArrayList<String>(connectedList), Payload.fromBytes((messageTypeString + "#" + data + "#" + new Date().getTime()).getBytes("UTF-8")));

//                messageAdapter.add(new Message(data,new MemberData("Paddy", "Green"),true));
                Log.i("CFDPP","Message Adapter completed");

                if(isNetworkAvailable()){

                    try {
                        sendRestMessages(uuid, deviceName, data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            //Send Timestamp
            else if(messageType==1) {

                String currentTime =  Long.toString(new Date().getTime());
                mConnectionsClient.sendPayload(new ArrayList<String>(connectedList), Payload.fromBytes((messageTypeString+"#"+parser.getSendStamp()+"#"+parser.getReceiveStamp()+"#"+currentTime).getBytes("UTF-8")));
            }
            //Send Location
            else if(messageType==2){

                mConnectionsClient.sendPayload(new ArrayList<String>(connectedList), Payload.fromBytes((messageTypeString+"#"+data).getBytes("UTF-8")));
                messageStruct messageStruct = new messageStruct(uuid,deviceName,data.split("#")[0]+","+data.split("#")[1],messageType);
                messageViewModel.insert(messageStruct);
            }
        } catch (UnsupportedEncodingException e) {

            Log.e("CODEFUNDO","ERROR in sending " + e.toString());
            e.printStackTrace();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void sendRestMessages(String uuid, String username, String message) throws JSONException {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        params.put("user", username);
        params.put("message", message);
        RestClient.post("/addData", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline



                    Toast.makeText(getApplicationContext(), String.valueOf(statusCode), Toast.LENGTH_LONG).show();


            }
        });
    }






    //---------------------------------------------------------------------------



}
