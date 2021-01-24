package com.example.keytronome;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    public static int DEFAULT_TEMPO = 120;

    int bpm;
    String order, startNote, keyLength, keySig;
    Metronome metronome;

    ToggleButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initValues();
        //Main Play button
        playButton = (ToggleButton) findViewById(R.id.playButton);
        playButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    metronome.start();
                }
                else{
                    metronome.interrupt();
                }
            }
        });
    }

    //TODO: Initiate values
    private void initValues(){
        bpm = DEFAULT_TEMPO;
        metronome = new Metronome(this.getApplicationContext(), bpm);
    }
}