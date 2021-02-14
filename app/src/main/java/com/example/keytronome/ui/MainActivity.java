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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;

    CircularProgressBar progressBar;
    TextView playButtonTv;
    private TextView tempoButtonValue;

    FragmentManager fm;
    private TextView tempoView;
    private TextView nextKeytv;
    private View playButton;
    private ImageView playButtonIv;
    private TextView titleTv;
    private TextView orderTv;
    private TextView startingKeyButtonTv;
    private TextView orderButtonTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set views here
        progressBar = findViewById(R.id.circularProgressBar);
        playButton = findViewById(R.id.playButton);
        nextKeytv = findViewById(R.id.nextKeyView);
        playButtonTv = findViewById(R.id.playButtonTv);
        playButtonIv = findViewById(R.id.playButtonIv);
        titleTv = findViewById(R.id.keytronomeTv);
        orderTv = findViewById(R.id.keysOrderTv);
        startingKeyButtonTv = findViewById(R.id.startingKeyButtonTv);
        orderButtonTv = findViewById(R.id.orderButtonTv);

        //Set top padding for status bar
        findViewById(R.id.main_activity).setPadding(0, getStatusBarHeight(), 0, 0);

        //Hide Action Bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.w("MAIN ACTIVITY", "COULD NOT HIDE ACTION BAR");
        }

        //Animate title
        findViewById(R.id.keytronomeTv).animate().alpha(1.0f).setDuration(2000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        fm = getSupportFragmentManager();

        //Initialize the viewmodel
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();


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
                if (isPlaying) {
                    progressBar.animate().alpha(1.0f).setDuration(1000);
                    playButtonIv.animate().alpha(0.0f).setDuration(1000);
                    playButtonTv.animate().alpha(1.0f).setDuration(1000);
                    nextKeytv.animate().alpha(1.0f).setDuration(1000);
                    titleTv.animate().alpha(0.0f).setDuration(1000);
                    orderTv.animate().alpha(0.0f).setDuration(1000);

                } else {
                    progressBar.animate().alpha(0.0f).setDuration(1000);
                    playButtonIv.animate().alpha(1.0f).setDuration(1000);
                    playButtonTv.animate().alpha(0.0f).setDuration(1000);
                    nextKeytv.animate().alpha(0.0f).setDuration(1000);
                    titleTv.animate().alpha(1.0f).setDuration(1000);
                    orderTv.animate().alpha(1.0f).setDuration(1000);
                }
            }
        });


        //Set next key observer
        mMainActivityViewModel.getNextKey().observe(this, new Observer<Pair<Integer, String>>() {
            @Override
            public void onChanged(Pair<Integer, String> nextKey) {
                Log.d("MAIN ACTIVITY", "Next key changed");
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(300);
                anim.setRepeatCount(1);
                anim.setRepeatMode(Animation.REVERSE);

                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        String currentKey = mMainActivityViewModel.getCurrentKey().getValue().second;
                        if (mMainActivityViewModel.getIsPlaying().getValue()) {
                            playButtonTv.setText(currentKey);
                            nextKeytv.setText(nextKey.second);
                            Log.d("MAIN ACTIVITY", "Observed next Key change. Current Key: " + currentKey + " Next Key :" + nextKey);
                        } else {
                            nextKeytv.setText(currentKey);
                        }
                    }
                });

                playButtonTv.startAnimation(anim);
                nextKeytv.startAnimation(anim);

            }
        });

        //Change order of keys view whenever the order changes and the order button
        mMainActivityViewModel.getOrder().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String order) {

                refreshOrdersList();
                //Order Button
                orderButtonTv.setText(order.toUpperCase());
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
                startingKeyButtonTv.setText("Starting from " + startingKey);
                refreshOrdersList();
            }
        });

        //Number of cycles changes
        mMainActivityViewModel.getCycles().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer cycles) {
                ((TextView) findViewById(R.id.cyclesButtonValue)).setText(String.format("x%s", cycles.toString()));
            }
        });

        //Measures per key changes
        mMainActivityViewModel.getMpk().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer mpk) {
                TextView mpkTv = findViewById(R.id.mpk_tv);
                mpkTv.setText(String.format("%s %s", getResources().getString(R.string.measure_per_key_prefix), mpk.toString()));
            }
        });


        //On click listeners for fragments
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

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void refreshOrdersList(){
        //Order View
        String keysList = mMainActivityViewModel.getActiveKeysList().getValue().subList(0, 12).toString();
        keysList = keysList.substring(1, keysList.length()-1);
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(800);
        anim.setRepeatCount(1);
        anim.setRepeatMode(Animation.REVERSE);

        String finalKeysList = keysList;
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                orderTv.setText(finalKeysList);
            }
        });
        orderTv.startAnimation(anim);
    }

}