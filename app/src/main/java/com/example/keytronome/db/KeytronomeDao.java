package com.example.keytronome.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface KeytronomeDao {

    @Insert
    long insert(Keytronome keytronome);

    @Query("SELECT * FROM presets")
    LiveData<List<Keytronome>> getAllPresets();
}
