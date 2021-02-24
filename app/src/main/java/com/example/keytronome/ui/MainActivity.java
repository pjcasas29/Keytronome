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
import com.example.keytronome.ui.fragments.SaveFragment;
import com.example.keytronome.ui.fragments.TempoScrollerFragment;
import com.example.keytronome.ui.fragments.TimeSignatureFragment;
import com.example.keytronome.viewmodels.MainActivityViewModel;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;

    CircularProgressBar progressBar;
    TextView playButtonTv;

    FragmentManager fm;
    private TextView tempoView;
    private TextView nextKeytv;
    private ImageView playButtonIv;
    private TextView titleTv;
    private TextView orderTv;
    private TextView startingKeyButtonTv;
    private TextView orderButtonTv;
    private TextView readyTv;
    private View nextKeyGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set views here
        progressBar = findViewById(R.id.circularProgressBar);
        View playButton = findViewById(R.id.playButton);
        nextKeytv = findViewById(R.id.nextKeyView);
        playButtonTv = findViewById(R.id.playButtonTv);
        playButtonIv = findViewById(R.id.playButtonIv);
        titleTv = findViewById(R.id.keytronomeTv);
        orderTv = findViewById(R.id.keysOrderTv);
        startingKeyButtonTv = findViewById(R.id.startingKeyButtonTv);
        orderButtonTv = findViewById(R.id.orderButtonTv);
        readyTv = findViewById(R.id.ready);
        nextKeyGroup = findViewById(R.id.nextKeyGroup);

        //Set top padding for status bar
        findViewById(R.id.main_activity).setPadding(0, getStatusBarHeight(), 0, 0);

        //Hide Action Bar
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            Log.w("MAIN ACTIVITY", "COULD NOT HIDE ACTION BAR");
        }

        //Animate title
        findViewById(R.id.keytronomeTv).animate().alpha(1.0f).setDuration(2000);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        fm = getSupportFragmentManager();

        //Initialize the viewmodel
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();


        //Main Play button
        playButton.setOnClickListener(view -> mMainActivityViewModel.play());

        setObservers();
        initGridMenu();
    }

    private void setObservers() {

        //Detect tempo changes
        mMainActivityViewModel.getTempo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer tempo) {
                tempoView = (TextView) findViewById(R.id.tempoView);
                tempoView.setText(String.format("%s%s", tempo.toString(), getString(R.string.bpm)));
            }
        });

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
        mMainActivityViewModel.getStartingKey().observe(this, startingKey -> startingKeyButtonTv.setText(String.format("%s%s", getString(R.string.order_button_prefix), startingKey)));

        //Number of cycles changes
        mMainActivityViewModel.getCycles().observe(this, cycles -> ((TextView) findViewById(R.id.cyclesButtonValue)).setText(String.format("x%s", cycles.toString())));

        //Measures per key changes
        mMainActivityViewModel.getMpk().observe(this, mpk -> {
            TextView mpkTv = findViewById(R.id.mpk_tv);
            mpkTv.setText(String.format("%s %s", getResources().getString(R.string.measure_per_key_prefix), mpk.toString()));
        });

        //Change the preview whenever the preview list changes
        mMainActivityViewModel.getPreviewList().observe(this, strings -> refreshPreviewList());

        //Change the order button when the order changes
        mMainActivityViewModel.getOrder().observe(this, order -> {

            //Order Button
            orderButtonTv.setText(order.toUpperCase());

        });

        //Changes in progress update
        mMainActivityViewModel.getProgress().observe(this, progress -> {
            Log.d("MAIN ACTIVITY", "PROGRESS: " + progress);
            progressBar.setProgressWithAnimation(progress, (long) 500);
        });

        //Set an observer whenever a key changes. We will use current key
        mMainActivityViewModel.getCurrentKey().observe(this, currentKey ->{
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
                        nextKeytv.setText(mMainActivityViewModel.getNextKey().getValue().second);
                    }
                }
            });
            playButtonTv.startAnimation(anim);
            nextKeytv.startAnimation(anim);
            Log.d("MAIN ACTIVITY", "Current key changed to " + mMainActivityViewModel.getCurrentKey().getValue() + "and " + mMainActivityViewModel.getNextKey().getValue());

        });

        //Main activity is cueing
        mMainActivityViewModel.getIsCueing().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isCueing) {
                if(isCueing){
                    //Hide
                    titleTv.animate().alpha(0.0f).setDuration(1000);
                    playButtonIv.animate().alpha(0.0f).setDuration(1000);
                    //Show
                    readyTv.animate().alpha(1.0f).setDuration(1000);
                    nextKeyGroup.animate().alpha(1.0f).setDuration(1000);
                    nextKeytv.setText((CharSequence) mMainActivityViewModel.getCurrentKey().getValue().second);


                }else{
                    //Hide views
                    readyTv.animate().alpha(0.0f).setDuration(1000);
                }
            }
        });

        //Main activity is playing
        mMainActivityViewModel.getIsPlaying().observe(this, isPlaying -> {
            if (isPlaying) {
                //Hide views
                orderTv.animate().alpha(0.0f).setDuration(1000);
                //Show views
                progressBar.animate().alpha(1.0f).setDuration(1000);
                playButtonTv.animate().alpha(1.0f).setDuration(1000);

                //Change Views
                nextKeytv.setText((CharSequence) mMainActivityViewModel.getNextKey().getValue().second);
                playButtonTv.setText((CharSequence) mMainActivityViewModel.getCurrentKey().getValue().second);


            } else {
                //Hide views
                progressBar.animate().alpha(0.0f).setDuration(1000);
                nextKeyGroup.animate().alpha(0.0f).setDuration(1000);
                playButtonTv.animate().alpha(0.0f).setDuration(1000);
                //Show views
                playButtonIv.animate().alpha(1.0f).setDuration(1000);
                titleTv.animate().alpha(1.0f).setDuration(1000);
                orderTv.animate().alpha(1.0f).setDuration(1000);
            }
        });
    }

    private void initGridMenu() {

        //On click listeners for fragments
        View tempoButton = findViewById(R.id.tempoButton);
        tempoButton.setOnClickListener(view -> changeFragment(new TempoScrollerFragment()));

        View timeSigButton = findViewById(R.id.timeSigButton);
        timeSigButton.setOnClickListener(view -> changeFragment(new TimeSignatureFragment()));

        View startingKeyButton = findViewById(R.id.startingKeyButton);
        startingKeyButton.setOnClickListener(view -> changeFragment(new OrderFragment()));

        View cyclesButton = findViewById(R.id.cyclesButton);
        cyclesButton.setOnClickListener(view -> changeFragment(new CyclesFragment()));

        View mpkButton = findViewById(R.id.mpkButton);
        mpkButton.setOnClickListener(view -> changeFragment(new MpkFragment()));

        View saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> changeFragment(new SaveFragment()));

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

    private void refreshPreviewList(){
        //Order View
        String keysList = mMainActivityViewModel.getPreviewList().getValue().toString();
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