package com.example.keytronome.controllers;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;

import java.util.Timer;
import java.util.TimerTask;

class Metronome extends Thread  {


    private SoundPool mSoundPool = new SoundPool.Builder().build();
    private int bpm;
    public Context context;
    public Uri SOUND;

    Metronome(Context app_context, int set_bpm){
        super();
        context = app_context;
        bpm = set_bpm;
    }

    @Override
    public void run() {


    }

}