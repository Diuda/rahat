package com.example.codeplayer.rahat_cfd;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private MessageRepo messageRepo;

    private LiveData<List<messageStruct>> mAllMessage;
    private List<String> msgIdList;


    public MessageViewModel(Application application) {
        super(application);
        messageRepo = new MessageRepo(application);
        mAllMessage = messageRepo.getmAllMessages();
        msgIdList = messageRepo.getIdList();
    }

    LiveData<List<messageStruct>> getmAllMessage() {
        return mAllMessage;
    }
    List<String> getIdList() {
        return msgIdList;
    }


    public void insert(messageStruct messageStruct) {
        messageRepo.insert(messageStruct);
    }
}
