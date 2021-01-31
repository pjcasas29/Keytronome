package com.example.keytronome.tasks;

import android.content.Context;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

import com.example.keytronome.R;
import com.example.keytronome.models.KeytronomeModel;

public class MetronomeTask implements Runnable {

    private static int DEFAULT_TICK_ID = R.raw.claptick;

    private final KeytronomeModel mKeyModel;
    private int executionTick = 0;

    public Context context;

    private SoundPool mSoundPool = new SoundPool.Builder().build();
    public int tickId;

    public MetronomeTask(final KeytronomeModel kModel, Context context) {
        this.mKeyModel = kModel;
        this.context = context;
        this.tickId = DEFAULT_TICK_ID;
    }

    public MetronomeTask(final KeytronomeModel kModel, int tickPath, Context context) {
        this.tickId = tickId;
        this.context = context;
        this.mKeyModel = kModel;

    }

    @Override
    public void run() {
        //active == true
        this.mKeyModel.start();
        int loadId = mSoundPool.load(context, tickId, 1);
        try {
            while (this.mKeyModel.isActive()) {
                if (this.executionTick == 10) {
                    this.mKeyModel.stop();
                    mSoundPool.release();
                } else {
                    mSoundPool.play(loadId, 1.0f, 1.0f, 1, 0, 1.0f);
                    Log.i("METRONOME THREAD", this.executionTick++ + ": " + this.mKeyModel.getBpm());
                    Thread.sleep(1000L);
                }
            }
        } catch (InterruptedException ie) {
            mSoundPool.release();
            Log.e("METRONOME THREAD", ie.getMessage());
        }

    }

}