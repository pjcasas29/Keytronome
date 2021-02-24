package com.example.keytronome.db;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "presets")
public class Keytronome {

    @NonNull
    public String getPresetName() {
        return presetName;
    }

    @NonNull
    public Boolean getAscending() {
        return ascending;
    }

    @NonNull
    public String getStartingKey() {
        return startingKey;
    }

    @NonNull
    public String getKeyOrder() {
        return keyOrder;
    }

    @NonNull
    public Integer getTempo() {
        return tempo;
    }

    @NonNull
    public String getTimeSig() {
        return timeSig;
    }

    @NonNull
    public Integer getCycles() {
        return cycles;
    }

    @NonNull
    public Integer getMpk() {
        return mpk;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String presetName;

    @NonNull
    @ColumnInfo(name = "ascending")
    private Boolean ascending;

    @NonNull
    @ColumnInfo(name = "starting key")
    private String startingKey;

    @NonNull
    @ColumnInfo(name = "key order")
    private String keyOrder;

    @NonNull
    @ColumnInfo(name = "tempo")
    private Integer tempo;

    @NonNull
    @ColumnInfo(name = "time signature")
    private String timeSig;

    @NonNull
    @ColumnInfo(name = "cycles")
    private Integer cycles;

    @NonNull
    @ColumnInfo(name = "mesures per key")
    private Integer mpk;

    public Keytronome(String presetName, Boolean ascending, String startingKey, String keyOrder, Integer tempo, String timeSig, Integer cycles, Integer mpk){
        this.presetName = presetName;
        this.ascending = ascending;
        this.startingKey = startingKey;
        this.keyOrder = keyOrder;
        this.tempo = tempo;
        this.timeSig = timeSig;
        this.cycles = cycles;
        this.mpk = mpk;
    }
}
