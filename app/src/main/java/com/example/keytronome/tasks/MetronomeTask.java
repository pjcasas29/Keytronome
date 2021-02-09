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
    private int totalBeats;
    private int mpk;

    public MetronomeTask(final MainActivityViewModel viewModel, Context context) {
        this.viewModel = viewModel;

        this.bpm = this.viewModel.getTempo().getValue();
        this.beatsPerMeasure = this.viewModel.getBeatsPerMeasure();
        this.mpk = this.viewModel.getMpk().getValue();

        this.totalBeats = this.beatsPerMeasure*this.viewModel.getActiveKeysList().getValue().size()*this.mpk;

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

        boolean playing = true;
        try {
            while (this.viewModel.getIsPlaying().getValue() && playing) {

                if (this.executionTick == this.totalBeats) {
                    //Stop metronome
                    mSoundPool.release();
                    playing = false;
                } else if(this.executionTick % beatsPerMeasure == 0){
                    //One measure
                    mSoundPool.play(loadPingId, 1.0f, 1.0f, 1, 0, 1.0f);
                    Log.i("METRONOME THREAD", "Current Key: " + this.viewModel.getCurrentKey().getValue());
                }
                else {
                    //Tick in between
                    mSoundPool.play(loadTickId, 1.0f, 1.0f, 1, 0, 1.0f);
                }

                //End of key
                if(this.executionTick % (beatsPerMeasure*mpk) == 0){
                    this.viewModel.goToNextKey();
                }

                //Sleep for correct BPM
                Thread.sleep((long) (1000 * (60.0 / this.bpm)));

                this.executionTick++;

                //Setting progress
                this.viewModel.setProgress(((float)this.executionTick / (float)this.totalBeats)*100);
            }
        } catch (InterruptedException ie) {
            mSoundPool.release();
            Log.e("METRONOME THREAD", "Interrupted");
        }
        this.viewModel.setIsPlaying(false);

    }

}