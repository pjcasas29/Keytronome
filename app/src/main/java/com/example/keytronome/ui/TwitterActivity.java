package com.example.keytronome.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.keytronome.R;
import com.example.keytronome.models.Tweet;
import com.example.keytronome.models.Tweets;
import com.example.keytronome.viewmodels.TwitterViewModel;

import java.util.List;
import java.util.Objects;

public class TwitterActivity extends AppCompatActivity {

    TwitterViewModel mTwitterViewModel;

    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private int counter = 0;
    Tweets tList;

    TextView tContent;
    ImageView tImage;
    TextView tUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        //Set top padding for status bar
        findViewById(R.id.twitterActivity).setPadding(0, getStatusBarHeight(), 0, 0);

        //Hide Action Bar
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            Log.w("MAIN ACTIVITY", "COULD NOT HIDE ACTION BAR");
        }

        tContent = findViewById(R.id.tweetContent);
        tImage = findViewById(R.id.tweetImage);
        tUser = findViewById(R.id.tweetUsername);

        //Initialize the viewmodel
        mTwitterViewModel = new ViewModelProvider(this).get(TwitterViewModel.class);
        mTwitterViewModel.init();

        Button loginButton = findViewById(R.id.shareTwitter);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetUrl = "https://twitter.com/intent/tweet?text=I've been practicing with #Keytronome!";
                Uri uri = Uri.parse(tweetUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        mTwitterViewModel.tweets().observe(this, new Observer<Tweets>() {
            @Override
            public void onChanged(Tweets tweets) {
                findViewById(R.id.tweet).animate().alpha(1.0f).setDuration(2000);
                mHandler = new Handler();
                startRepeatingTask();
                tList = tweets;
                updatetweet();
            }
        });


    }

    private void updatetweet() {
        Tweet t;
        if (tList!= null){
            t = tList.getTweets().get(counter % tList.getTweets().size());
            counter++;

            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(800);
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
                    tContent.setText(t.getContent());
                    tUser.setText(t.getUsername().getName());

                }
            });
            tContent.startAnimation(anim);
            tUser.startAnimation(anim);

            Glide.with(this).load(t.getUsername().getImageUrl()).into(tImage);

//            Picasso.get()
//                    .load(t.getUsername().getImageUrl())
//                    .placeholder(R.drawable.twitter)
//                    .into(tImage);
        }

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updatetweet();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };


    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
}