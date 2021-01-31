package com.example.keytronome.threads;

import android.content.Context;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

import com.example.keytronome.models.KeytronomeModel;

public class MetronomeTask implements Runnable  {

    private final KeytronomeModel mKeyModel;
    private int executionTick = 0;


    private SoundPool mSoundPool = new SoundPool.Builder().build();
    public Uri SOUND;

    public MetronomeTask(final KeytronomeModel kModel){
        mKeyModel = kModel;
    }

    @Override
    public void run() {
        //active == true
        this.mKeyModel.start();
        try {
            while (this.mKeyModel.isActive()) {
                if (this.executionTick == 10) {
                    this.mKeyModel.stop();
                } else {
                    Log.i("METRONOME THREAD", this.executionTick++ + ": " + this.mKeyModel.getBpm());
                    Thread.sleep(1000L);
                }
            }
        } catch (InterruptedException ie) {
            Log.e("METRONOME THREAD", ie.getMessage());
        }

    }

}