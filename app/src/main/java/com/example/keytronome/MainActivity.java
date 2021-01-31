package com.example.keytronome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.keytronome.models.KeytronomeModel;
import com.example.keytronome.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;

    ToggleButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mMainActivityViewModel.init();

        //Bind to whole Keytronome model. This detects any changes to the whole keytronome model
        mMainActivityViewModel.getTempo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                return;
            }
        });

        //Bind to isPlaying
        mMainActivityViewModel.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPlaying) {
                if(isPlaying){
                    updateView();
                }else{
                    resetView();
                }
            }
        });

        //Main Play button
        playButton = (ToggleButton) findViewById(R.id.playButton);
        playButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mMainActivityViewModel.startMetronome();
                }
                else{
                    mMainActivityViewModel.stopMetronome();
                }
            }
        });

        initGridMenu();

    }
    // Resets the view when the app stops playing
    private void resetView() {
    playButton.setChecked(false);
    }


    private void updateView() {
    }

    private void initGridMenu(){
        //tempo = mMainActivityViewModel.getTempo();
    }

}