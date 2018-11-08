package com.example.codeplayer.rahat_cfd;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("Select * from messageStruct")
    LiveData<List<messageStruct>> getAllMessages();

    @Query("Select messageid from messageStruct")
    List<messageStruct> getMessageIdList();

    @Insert
    void insertAll(messageStruct messageStruct);

}
