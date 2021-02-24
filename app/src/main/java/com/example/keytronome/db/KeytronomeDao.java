package com.example.keytronome.db;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface KeytronomeDao {

    @Insert
    long insert(Keytronome keytronome);
}
