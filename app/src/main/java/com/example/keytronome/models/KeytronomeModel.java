package com.example.keytronome.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.tasks.MetronomeTask;

/**
 * POJO for values of the settings of the keytronome model.
 *
 */
public class KeytronomeModel {
    //Default values
    private static int DEFAULT_TEMPO = 120;
    //TODO: Change to dictionary of int string pairs, key will be 4/4 and the int will be the number of beats in the measure.
    private static String DEFAULT_TIMESIG = "4/4";

    public MutableLiveData<String> keyOrder = new MutableLiveData<>();
    public MutableLiveData<Integer> tempo = new MutableLiveData<>();
    public MutableLiveData<String> timeSig = new MutableLiveData<>();

    private MetronomeTask mMetronomeTask;

    public KeytronomeModel(int bpm, String sig){
//        tempo = bpm;
//        timeSig = sig;
    }

    public KeytronomeModel(){

        tempo.setValue(DEFAULT_TEMPO);
        timeSig.setValue(DEFAULT_TIMESIG);
    }

    public void setTempo(int bpm) {
        if(bpm <= 300 && bpm >= 20){
            tempo.setValue(bpm);
        }
        else{
            throw new IllegalArgumentException("VALUE OF TEMPO MUST BE BETWEEN 0 AND 300");
        }
    }

    public MutableLiveData<Integer> getBpm(){
        return this.tempo;
    }
}
