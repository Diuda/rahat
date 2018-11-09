package com.example.codeplayer.rahat_cfd;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MessageRepo {

    private MessageDao messageDao;
    private LiveData<List<messageStruct>> mAllMessages;
    private List<String> msgIdList;

    MessageRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        messageDao = db.MessageDao();
        mAllMessages = messageDao.getAllMessages();
        msgIdList = messageDao.getMessageIdList();
    }

    LiveData<List<messageStruct>> getmAllMessages() {
        return mAllMessages;
    }

    List<String> getIdList() {
        return msgIdList;
    }

    public void insert(messageStruct messageStruct) {
//        new insertAsyncTask(messageDao).execute()
        new insertAsyncTask(messageDao).execute(messageStruct);
    }


    private static class insertAsyncTask extends AsyncTask<messageStruct, Void, Void>{
        private MessageDao mAsyncTaskDao;
        insertAsyncTask(MessageDao messageDao){
            mAsyncTaskDao = messageDao;
        }

        @Override
        protected Void doInBackground(final messageStruct... params){
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }
}
