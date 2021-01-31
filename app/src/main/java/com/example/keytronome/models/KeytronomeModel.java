package com.example.keytronome.models;

import com.example.keytronome.threads.MetronomeTask;

/**
 * POJO for encapsulating the state of the keytronome model
 *
 */
public class KeytronomeModel {
    //Default values
    private static int DEFAULT_TEMPO = 120;
    private static String DEFAULT_TIMESIG = "4/4";

    public String keyOrder;
    public int tempo;
    public String timeSig;
    public boolean isActive;

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

    public void start(){
        this.isActive =  true;
    }

    public void stop(){
        this.isActive = false;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public int getBpm(){
        return this.tempo;
    }
}
