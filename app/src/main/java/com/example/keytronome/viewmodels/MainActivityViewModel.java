package com.example.keytronome.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.keytronome.models.KeytronomeModel;
import com.example.keytronome.repositories.KeytronomeRepository;
import com.example.keytronome.threads.MetronomeTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<KeytronomeModel> mKeytronomeModel;
    private KeytronomeRepository mRepo;


    public void init() {
        if (mKeytronomeModel != null) {
            return;
        }
        mRepo = KeytronomeRepository.getInstance();
        mKeytronomeModel = mRepo.getDefaultSettings();
    }

    public LiveData<KeytronomeModel> getKnmSettings() {
        return mKeytronomeModel;
    }


    public void startMetronome() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new MetronomeTask(mKeytronomeModel.getValue()));
        executor.shutdown();
    }

    public void stopMetronome() {
        mKeytronomeModel.getValue().stop();
    }
}