package com.example.keytronome.tasks;

import android.content.Context;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.example.keytronome.R;
import com.example.keytronome.models.KeytronomeModel;
import com.example.keytronome.viewmodels.MainActivityViewModel;

public class MetronomeTask implements Runnable {

    private static int DEFAULT_TICK_ID = R.raw.claptick;

    private int executionTick = 0;
    private MainActivityViewModel viewModel;

    public Context context;

    private SoundPool mSoundPool = new SoundPool.Builder().build();
    public int tickId;

    private Integer bpm;

    public MetronomeTask(final MainActivityViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.bpm = this.viewModel.getTempo().getValue();

        this.context = context;
        this.tickId = DEFAULT_TICK_ID;
    }

    public MetronomeTask(MainActivityViewModel viewModel, int tickPath, Context context) {
        this.tickId = tickPath;
        this.viewModel = viewModel;
        this.context = context;

    }

    @Override
    public void run() {
        int loadId = mSoundPool.load(context, tickId, 1);
        try {
            while (this.viewModel.getIsPlaying().getValue()) {
                if (this.executionTick == 10) {
                    this.viewModel.stopMetronome();
                    mSoundPool.release();
                } else {
                    mSoundPool.play(loadId, 1.0f, 1.0f, 1, 0, 1.0f);
                    Log.i("METRONOME THREAD", this.executionTick++ + ": ");
                    Thread.sleep((long) (1000 * (60.0 / this.bpm)));
                }
            }
        } catch (InterruptedException ie) {
            mSoundPool.release();
            Log.e("METRONOME THREAD", "Interrupted");
        }

    }

}