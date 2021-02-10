package com.example.keytronome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keytronome.R;
import com.example.keytronome.ui.fragments.CyclesFragment;
import com.example.keytronome.ui.fragments.MpkFragment;
import com.example.keytronome.ui.fragments.OrderFragment;
import com.example.keytronome.ui.fragments.TempoScrollerFragment;
import com.example.keytronome.ui.fragments.TimeSignatureFragment;
import com.example.keytronome.viewmodels.MainActivityViewModel;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;

    CircularProgressBar progressBar;
    TextView playButton;
    private TextView tempoButtonValue;

    FragmentManager fm;
    private TextView tempoView;
    private TextView nextKeytv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set top padding for status bar
        findViewById(R.id.main_activity).setPadding(0, getStatusBarHeight(), 0, 0);

        //Hide Action Bar
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Log.w("MAIN ACTIVITY", "COULD NOT HIDE ACTION BAR");
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        fm = getSupportFragmentManager();

        //Initialize the viewmodel
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();

        progressBar = findViewById(R.id.circularProgressBar);

        //Detect tempo changes
        mMainActivityViewModel.getTempo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer tempo) {
                tempoView = (TextView) findViewById(R.id.tempoView);
                tempoView.setText(tempo.toString() + getString(R.string.bpm));
            }
        });


        //Changes in progress
        mMainActivityViewModel.getProgress().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float progress) {
                Log.d("MAIN ACTIVITY", "PROGRESS: " + progress);
                progressBar.setProgressWithAnimation(progress, (long) 500);
            }
        });

        //Main Play button
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivityViewModel.setIsPlaying(!mMainActivityViewModel.getIsPlaying().getValue());
            }
        });

        //Bind to isPlaying
        mMainActivityViewModel.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPlaying) {
                if (isPlaying){
                    progressBar.animate().alpha(1.0f).setDuration(1000);
                    playButton.setTextSize(Float.parseFloat(getResources().getString(R.string.keytronome_play_Button_font_size)));


                }else{
                    progressBar.animate().alpha(0.0f).setDuration(1000);
                    playButton.setText(R.string.app_name);
                    playButton.setTextSize(Float.parseFloat(getResources().getString(R.string.keytronome_button_font_size)));
                }
            }
        });


        //Set next note observer
        nextKeytv = findViewById(R.id.nextKeyView);
        mMainActivityViewModel.getNextKey().observe(this, new Observer<Pair<Integer, String>>() {
            @Override
            public void onChanged(Pair<Integer, String> nextKey) {
                String currentKey = mMainActivityViewModel.getCurrentKey().getValue().second;
                if (mMainActivityViewModel.getIsPlaying().getValue()) {
                    playButton.setText(currentKey);
                    nextKeytv.setText(nextKey.second);
                    Log.d("MAIN ACTIVITY", "Observed next Key change. Current Key: " + currentKey + " Next Key :" + nextKey);
                } else {
                    nextKeytv.setText(currentKey);
                }
            }
        });

        initGridMenu();
    }

    private void initGridMenu() {

        //Time signature changes
        ImageView timeSigImage = findViewById(R.id.timeSignatureImage);
        mMainActivityViewModel.getTimeSig().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String timeSig) {

                if (timeSig.equals("4/4")) {
                    timeSigImage.setImageDrawable(getResources().getDrawable(R.drawable.white44));
                } else if (timeSig.equals("3/4")) {
                    timeSigImage.setImageDrawable(getResources().getDrawable(R.drawable.white34));
                } else if ((timeSig.equals("2/4"))) {
                    timeSigImage.setImageDrawable(getResources().getDrawable(R.drawable.white24));
                } else if (timeSig.equals("6/8")) {
                    timeSigImage.setImageDrawable(getResources().getDrawable(R.drawable.white68));
                }
            }
        });

        //Starting Key changes
        mMainActivityViewModel.getStartingKey().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String startingKey) {
                ((TextView) findViewById(R.id.startingKeyValue)).setText(startingKey);
            }
        });

        //Number of cycles changes
        mMainActivityViewModel.getCycles().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer cycles) {
                ((TextView) findViewById(R.id.cyclesButtonValue)).setText(String.format("x%s", cycles.toString()));
            }
        });

        mMainActivityViewModel.getMpk().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mpk) {
                TextView mpkTv = findViewById(R.id.mpk_tv);
                mpkTv.setText(String.format("%s %s", getResources().getString(R.string.measure_per_key_prefix), mpk.toString()));
            }
        });


        //On click listeners
        View tempoButton = findViewById(R.id.tempoButton);
        tempoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new TempoScrollerFragment());
            }
        });

        View timeSigButton = findViewById(R.id.timeSigButton);
        timeSigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new TimeSignatureFragment());
            }
        });

        View startingKeyButton = findViewById(R.id.startingKeyButton);
        startingKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new OrderFragment());
            }
        });

        View cyclesButton = findViewById(R.id.cyclesButton);
        cyclesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new CyclesFragment());
            }
        });

        View mpkButton = findViewById(R.id.mpkButton);
        mpkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new MpkFragment());
            }
        });


    }

    private void changeFragment(Fragment fragment) {
        fm.beginTransaction()
//                .setCustomAnimations(R.animator.slide_up, R.animator.slide_down, R.animator.slide_up, R.animator.slide_down)
                .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit, R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                .replace(R.id.gridLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}