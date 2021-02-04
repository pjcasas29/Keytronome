package com.example.keytronome.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.keytronome.models.KeytronomeModel;
import com.example.keytronome.repositories.KeytronomeRepository;
import com.example.keytronome.tasks.MetronomeTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *The view model keeps track of the live state of the application
 */
public class MainActivityViewModel extends AndroidViewModel {

    private KeytronomeRepository mRepo;
    private MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private ExecutorService executor;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (mRepo != null) {
            return;
        }
        mRepo = KeytronomeRepository.getInstance();
        setStartState();
    }

    private void setStartState(){
        isPlaying.setValue(false);
    }

    public LiveData<Integer> getTempo(){
        return mRepo.getTempo();
    }

    public void setTempo(int bpm){
        Log.d("DEBUG", "Setting tempo to " + bpm);
        mRepo.setTempo(bpm);
    }

    //Start a new thread and pass the viewmodel to the thread so the thread can update the values
    public void startMetronome() {
        isPlaying.setValue(true);
        executor = Executors.newSingleThreadExecutor();
        executor.execute(new MetronomeTask(this, this.getApplication()));
    }

    public void stopMetronome() {
        isPlaying.postValue(false);
        executor.shutdownNow();
    }

    public LiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean playing){
        isPlaying.setValue(playing);
    }
}