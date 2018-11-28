package com.example.codeplayer.rahat_cfd;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class SelfHelpFragment extends Fragment {


    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.selfhelpfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<HelpModel> helpList = new ArrayList<>();

        helpList.add(new HelpModel("Flood", "Bhaago BC PAANI"));
        helpList.add(new HelpModel("EarthQuake", "hila hila ke dala tha"));
        helpList.add(new HelpModel("Tsunami", "Maroge ab to"));





        recyclerView = view.findViewById(R.id.recview);
        SelfHelpAdapter selfHelpAdapter = new SelfHelpAdapter(helpList);
        recyclerView.setAdapter(selfHelpAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));








    }


}
