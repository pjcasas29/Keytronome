package com.example.keytronome.models;

public class KeytronomeModel {
    //Default values
    private static int DEFAULT_TEMPO = 120;
    private static String DEFAULT_TIMESIG = "4/4";


    private int tempo;
    private String timeSig;

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


}
