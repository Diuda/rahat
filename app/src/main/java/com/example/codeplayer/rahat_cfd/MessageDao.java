package com.example.codeplayer.rahat_cfd;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("Select * from messageStruct")
    List<messageStruct> getAll();



    @Insert
    void insertAll(messageStruct messageStruct);

}
