package com.example.keytronome.models;

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

    public String keyOrder;
    public int tempo;
    public String timeSig;

    private MetronomeTask mMetronomeTask;

    public KeytronomeModel(int bpm, String sig){
        tempo = bpm;
        timeSig = sig;
    }

    public KeytronomeModel(){
        tempo = DEFAULT_TEMPO;
        timeSig = DEFAULT_TIMESIG;
    }

    public void setTempo(int bpm) {
        if(bpm < 300 && bpm > 20){
            tempo = bpm;
        }
        else{
            throw new IllegalArgumentException("VALUE OF TEMPO MUST BE BETWEEN 0 AND 300");
        }
    }

    public int getBpm(){
        return this.tempo;
    }
}
