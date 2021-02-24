package com.example.keytronome.viewmodels;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.repositories.KeytronomeRepository;
import com.example.keytronome.tasks.CueTask;
import com.example.keytronome.tasks.MetronomeTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;


/**
 * The view model keeps track of the live state of the application
 */
public class MainActivityViewModel extends AndroidViewModel {

    private KeytronomeRepository mRepo;
    ExecutorService executor;
    private final HashMap<String, Integer> beatsPerMeasureMap = new HashMap<String, Integer>() {{
        put("4/4", 4);
        put("3/4", 3);
        put("2/4", 2);
        put("6/8", 6);
    }};

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (mRepo != null) {
            return;
        }
        mRepo = KeytronomeRepository.getInstance(getApplication());
    }

    public LiveData<Integer> getTempo() {
        return mRepo.getTempo();
    }

    public void setTempo(int bpm) {
        Log.d("DEBUG", "Setting tempo to " + bpm);
        mRepo.setTempo(bpm);
    }

    public LiveData<Boolean> getIsPlaying() {
        return mRepo.getIsPlaying();
    }

    public void setIsPlaying(boolean playing) {
        Log.d("VM", "SET IS PLAYING TO " + playing);
        mRepo.setIsPlaying(playing);
    }

    public Integer getMinTempo() {
        return mRepo.getMinTempo();
    }

    public int getMaxTempo() {
        return mRepo.getMaxTempo();
    }

    public LiveData<String> getTimeSig() {
        return mRepo.getTimeSig();
    }

    public Integer getBeatsPerMeasure() {
        return beatsPerMeasureMap.get(mRepo.getTimeSig().getValue());
    }

    public void setTimeSig(String timeSig) {
        mRepo.setTimeSig(timeSig);
    }

    public LiveData<ArrayList<String>> getActiveKeysList() {
        return mRepo.getActiveKeysList();
    }

    public LiveData<String> getStartingKey() {
        return mRepo.getStartingKey();
    }

    public void setStartingKey(String key) {
        mRepo.setStartingKey(key);
    }

    public LiveData<Pair<Integer, String>> getNextKey() {
        return mRepo.getNextKey();
    }

    public MutableLiveData<Integer> getCycles() {
        return mRepo.getCycles();
    }

    public int getMaxCycles() {
        return mRepo.getMaxCycles();
    }

    public void setCycles(int newCycles) {
        mRepo.setCycles(newCycles);
    }

    public ArrayList<String> getKeys() {
        return mRepo.getKeys();
    }

    public void goToNextKey() {
        mRepo.goToNextKey();
    }

    public LiveData<Pair<Integer, String>> getCurrentKey() {
        return mRepo.getCurrentKey();
    }

    public void setProgress(float progress) {
        mRepo.setProgress(progress);
    }

    public LiveData<Float> getProgress() {
        return mRepo.getProgress();
    }

    public int getMaxMpk() {
        return mRepo.getMaxMpk();
    }

    public void setMpk(int newMpk) {
        mRepo.setMpk(newMpk);
    }

    public LiveData<Integer> getMpk() {
        return mRepo.getMpk();
    }

    public LiveData<String> getOrder() {
        return mRepo.getOrder();
    }

    public ArrayList<String> getOrders() {
        return mRepo.getOrders();
    }

    public void setOrder(String newOrder) {
        mRepo.setOrder(newOrder);
    }

    public LiveData<List<String>> getPreviewList() {
        return mRepo.getPreviewList();
    }

    public void play() {

        if (mRepo.getIsPlaying().getValue()) {
            executor.shutdownNow();
            mRepo.setIsPlaying(false);

        } else if (mRepo.getIsCueing().getValue()) {
            executor.shutdownNow();
            mRepo.setIsCueing(false);
        } else {
            mRepo.setIsCueing(true);
            executor = Executors.newSingleThreadExecutor();

            //Cue metronome here
            executor.submit(new CueTask(this, this.getApplication()));

        }

        // emit an observable every time interval
//        Observable<Long> intervalObservable = Observable
//                .interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .takeWhile(new Predicate<Long>() { // stop the process if more than 5 seconds passes
//                    @Override
//                    public boolean test(Long aLong) throws Exception {
//                        return aLong <= 5;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread());
//
//        intervalObservable.subscribe(new Observer<Long>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//            }
//            @Override
//            public void onNext(Long aLong) {
//                Log.d(TAG, "onNext: interval: " + aLong);
//            }
//            @Override
//            public void onError(Throwable e) {
//
//            }
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }

    public LiveData<Boolean> getIsCueing() {
        return mRepo.getIsCueing();
    }

    public void setIsCueing(boolean cueing){
        mRepo.setIsCueing(cueing);
    }

    public void doneCueing() {

        this.setIsCueing(false);

        Log.d("VIEWMODEL", "SETTING PLAYING TO TRUE");
        this.setIsPlaying(true);

        //Start playing metronome
        executor.submit(new MetronomeTask(this, this.getApplication()));
        executor.shutdown();
    }


    public Completable savePreset(String name){
        return mRepo.savePreset(name);
    }
}