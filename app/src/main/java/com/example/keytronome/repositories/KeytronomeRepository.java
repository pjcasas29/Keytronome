package com.example.keytronome.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.models.Tweets;
import com.example.keytronome.apis.TwitterAPIClient;
import com.example.keytronome.apis.TwitterAPIInterface;
import com.example.keytronome.apis.authToken;
import com.example.keytronome.db.Keytronome;
import com.example.keytronome.db.KeytronomeDao;
import com.example.keytronome.db.KeytronomeRoomDatabase;
import com.example.keytronome.models.KeytronomeModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class KeytronomeRepository {

    private static KeytronomeRepository instance;
    private static KeytronomeModel model = new KeytronomeModel();

    private static LiveData<List<Keytronome>> mAllPresets;

    private static Context mContext;

    private KeytronomeDao keytronomeDao;
    private KeytronomeRoomDatabase keytronomeDB;

    TwitterAPIInterface twitterApi;

    private MutableLiveData<Tweets> tweets = new MutableLiveData<>();

    public static KeytronomeRepository getInstance(Application context) {
        if (instance == null) {
            mContext = context;
            instance = new KeytronomeRepository(context);
        }
        return instance;
    }

    public KeytronomeRepository(Application context) {
        //Instantiate DB
        keytronomeDB = KeytronomeRoomDatabase.getDatabase(context);
        keytronomeDao = keytronomeDB.keytronomeDao();

        //Instantiate the session manager
    }

    public LiveData<List<Keytronome>> getPresets() {
        mAllPresets = keytronomeDao.getAllPresets();
        return mAllPresets;
    }

    public Completable savePreset(String name) {
        model.setName(name);
        Keytronome entity = model.toEntity();
        return Completable.fromCallable(() -> keytronomeDB.keytronomeDao().insert(entity));
    }


    public MutableLiveData<Integer> getTempo() {
        return model.getBpm();
    }

    public void setTempo(int bpm) {
        model.setTempo(bpm);
    }

    public Integer getMinTempo() {
        return model.getMinTempo();
    }

    public int getMaxTempo() {
        return model.getMaxTempo();
    }

    public MutableLiveData<String> getTimeSig() {
        return model.getTimeSig();
    }

    public void setTimeSig(String timeSig) {
        model.setTimeSig(timeSig);
    }

    public LiveData<ArrayList<String>> getActiveKeysList() {
        return model.getActiveKeysList();
    }

    public LiveData<String> getStartingKey() {
        return model.getStartingKey();
    }

    public void setStartingKey(String key) {
        model.setStartingKey(key);
    }

    public LiveData<Pair<Integer, String>> getNextKey() {
        return model.getNextKey();
    }

    public MutableLiveData<Integer> getCycles() {
        return model.getCycles();
    }

    public int getMaxCycles() {
        return model.getMaxCycles();
    }

    public void setCycles(int newCycles) {
        model.setCycles(newCycles);
    }

    public ArrayList<String> getKeys() {
        return model.getKeys();
    }

    public void setIsPlaying(boolean isPlaying) {
        Log.d("REPO", "SET IS PLAYING TO " + isPlaying);
        model.setIsPlaying(isPlaying);
    }

    public void goToNextKey() {
        model.goToNextKey();
    }

    public LiveData<Pair<Integer, String>> getCurrentKey() {
        return model.getCurrentKey();
    }

    public LiveData<Boolean> getIsPlaying() {
        return model.isPlaying();
    }

    public void setProgress(float progress) {
        model.setProgress(progress);
    }

    public LiveData<Float> getProgress() {
        return model.getProgress();
    }

    public int getMaxMpk() {
        return model.getMaxMpk();
    }

    public void setMpk(int newMpk) {
        model.setMpk(newMpk);
    }

    public LiveData<Integer> getMpk() {
        return model.getMpk();
    }

    public LiveData<String> getOrder() {
        return model.getOrder();
    }

    public ArrayList<String> getOrders() {
        return model.getOrders();
    }

    public void setOrder(String newOrder) {
        model.setOrder(newOrder);
    }

    public LiveData<List<String>> getPreviewList() {
        return model.getPreviewList();
    }

    public LiveData<Boolean> getIsCueing() {
        return model.isCueing();
    }

    public void setIsCueing(boolean cueing) {
        model.setIsCueing(cueing);
    }

    public LiveData<Tweets> getTweets() {
        Log.i("REPO", "Getting tweets");
        return tweets;
    }


    public void setupTwitterApi() {

        twitterApi = TwitterAPIClient.getClient().create(TwitterAPIInterface.class);

        twitterApi.postCredentials("client_credentials")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<authToken>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull authToken oAuthToken) {
                        TwitterAPIClient.saveToken(oAuthToken);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("REPO", "COULD NOT ACCESS API: " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("REPO", "AUTH complete");

                        twitterApi.getTweets("Metronome")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Tweets>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull Tweets mTweets) {
                                        tweets.setValue(mTweets);
                                        Log.d("REPO", "Set tweets");
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                        Log.e("REPO", e.toString());

                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d("REPO", "Got tweets");

                                    }
                                });
                    }
                });

    }

}

