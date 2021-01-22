package com.example.keytronome;

import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

//TODO: Async task, and finish timer
class Metronome extends TimerTask {

    Metronome(context, SOUND){
        super();

    }

    @Override
    public void run() {
        playSound();
    }
    private void  playSound() {
        final MediaPlayer mp = MediaPlayer.create(context, SOUND);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            };
        });
    }

    public void play() {
        Timer mainTimer = new Timer();
        Timer subTimer = new Timer();
        Metronome mainTimerTask = new Metronome();
        Metronome subTimerTask = new Metronome();

        mainTimer.schedule(mainTimerTask, 0, MILLIS_IN_MINUTE / bpm);
        subTimer.schedule(subTimerTask, (300 * (100+swing)) / bpm, MILLIS_IN_MINUTE / bpm);

    }
}