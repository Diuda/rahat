package com.example.codeplayer.rahat_cfd;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GlobalChatFragment extends Fragment {

    protected MessageAdapter messageAdapter;
    private RecyclerView globalrecyclerView;
    MainActivity act;
    private MessageViewModel messageViewModel;
    LocationManager lm;
    List<messageStruct> messageStructList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.globalchatfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageStructList = new ArrayList<>();


        globalrecyclerView = view.findViewById(R.id.globalmessagerecycleview);
        final MessageListAdapter adapter = new MessageListAdapter(getActivity());
        globalrecyclerView.setAdapter(adapter);
        globalrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




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
//                        Toast.makeText(getContext(), usermessage, Toast.LENGTH_LONG).show();

                        messageStructList.add(i, new messageStruct(data.getString("uuid"), data.getString("username"), data.getString("message"), 0));


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
