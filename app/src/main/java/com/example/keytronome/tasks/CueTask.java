package com.example.keytronome.tasks;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import com.example.keytronome.R;
import com.example.keytronome.viewmodels.MainActivityViewModel;

public class CueTask implements Runnable {

    private static int DEFAULT_TICK_ID = R.raw.claptick;


    private final MainActivityViewModel viewModel;
    private final int tickId;
    private final Context context;
    private SoundPool mSoundPool = new SoundPool.Builder().build();
    private int cueTicks;
    private int bpm;


    public CueTask(final MainActivityViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.bpm = this.viewModel.getTempo().getValue();
        this.cueTicks = this.viewModel.getBeatsPerMeasure();

        this.context = context;
        this.tickId = DEFAULT_TICK_ID;

    }

    @Override
    public void run() {
        int loadTickId = mSoundPool.load(context, tickId, 1);

        //Pause 1 second before cue
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        while (true) {

            if(this.cueTicks == 0 || !viewModel.getIsCueing().getValue()){
                Log.d("CUE TASK", "CUEING Stopped");

                break;
            }else{
                Log.d("CUE TASK", "CUEING Tick");

                mSoundPool.play(loadTickId, 1.0f, 1.0f, 1, 0, 1.0f);
                try {
                    Thread.sleep((long) (1000 * (60.0 / this.bpm)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.cueTicks--;
        }

        //Finalize here

        viewModel.doneCueing();

    }

}
