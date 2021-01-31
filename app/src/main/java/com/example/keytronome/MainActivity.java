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

        mMainActivityViewModel.getKnmSettings().observe(this, new Observer<KeytronomeModel>() {
            @Override
            public void onChanged(KeytronomeModel keytronomeModel) {

            }
        });

        initGridMenu();


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
    }

    private void initGridMenu(){
        //tempo = mMainActivityViewModel.getTempo();
    }

}