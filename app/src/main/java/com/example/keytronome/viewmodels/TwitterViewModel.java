package com.example.keytronome.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.keytronome.models.Tweet;
import com.example.keytronome.models.Tweets;
import com.example.keytronome.repositories.KeytronomeRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class TwitterViewModel extends AndroidViewModel {

    private KeytronomeRepository mRepo;


    public TwitterViewModel(Application application) {
        super(application);
    }

    public void init() {
        if (mRepo != null) {
            return;
        }
        mRepo = KeytronomeRepository.getInstance(getApplication());
        mRepo.setupTwitterApi();


    }

    public LiveData<Tweets> tweets() {
        return mRepo.getTweets();
    }
}
