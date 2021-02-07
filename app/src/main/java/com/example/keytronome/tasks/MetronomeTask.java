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
    private static int DEFAULT_PING_ID = R.raw.ping;

    private int executionTick = 0;
    private MainActivityViewModel viewModel;
    private Context context;
    private SoundPool mSoundPool = new SoundPool.Builder().build();
    private int tickId;
    private int pingId;
    private Integer bpm;
    private int beatsPerMeasure;

    public MetronomeTask(final MainActivityViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.bpm = this.viewModel.getTempo().getValue();
        this.beatsPerMeasure = this.viewModel.getBeatsPerMeasure();
        this.context = context;
        this.tickId = DEFAULT_TICK_ID;
        this.pingId = DEFAULT_PING_ID;
    }

    public MetronomeTask(MainActivityViewModel viewModel, int tickPath, Context context) {
        this.tickId = tickPath;
        this.viewModel = viewModel;
        this.context = context;

    }

    @Override
    public void run() {
        int loadTickId = mSoundPool.load(context, tickId, 1);
        int loadPingId = mSoundPool.load(context, pingId, 1);
        try {
            Thread.sleep((long) (100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            while (this.viewModel.getIsPlaying().getValue()) {
                if (this.executionTick == 10) {
                    mSoundPool.release();
                    this.viewModel.stopMetronome();
                } else if(this.executionTick % beatsPerMeasure == 0){
                    mSoundPool.play(loadPingId, 1.0f, 1.0f, 1, 0, 1.0f);
                    Log.i("METRONOME THREAD", this.executionTick++ + ": ");
                    Thread.sleep((long) (1000 * (60.0 / this.bpm)));
                }
                else {
                    mSoundPool.play(loadTickId, 1.0f, 1.0f, 1, 0, 1.0f);
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