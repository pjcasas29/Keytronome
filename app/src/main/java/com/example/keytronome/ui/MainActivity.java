package com.example.keytronome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.keytronome.R;
import com.example.keytronome.models.KeytronomeModel;
import com.example.keytronome.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;

    ToggleButton playButton;
    private TextView tempoButtonValue;

    FragmentManager fm;
    private TextView tempoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();

        //Initialize the viewmodel
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();

        //Detect tempo changes
        mMainActivityViewModel.getTempo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                tempoView = (TextView) findViewById(R.id.tempoView);
                tempoView.setText(integer.toString() + "bpm");

                Log.d("DEBUGB", "Tempo change detected on view");
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
        tempoButtonValue = findViewById(R.id.tempoButtonValue);
        tempoButtonValue.setText(mMainActivityViewModel.getTempo().getValue().toString());

        View tempoButton = findViewById(R.id.tempoButton);
        tempoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction()
                        .setCustomAnimations(R.animator.slide_up, android.R.anim.fade_out)
                        .replace(R.id.optionsFragment, new TempoScrollerFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}