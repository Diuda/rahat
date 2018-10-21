package com.example.codeplayer.rahat_cfd;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private MessageRepo messageRepo;

    private LiveData<List<messageStruct>> mAllMessage;

    public MessageViewModel(Application application) {
        super(application);
        messageRepo = new MessageRepo(application);
        mAllMessage = messageRepo.getmAllMessages();
    }

    LiveData<List<messageStruct>> getmAllMessage() {
        return mAllMessage;
    }


    public void insert(messageStruct messageStruct) {
        messageRepo.insert(messageStruct);
    }
}
