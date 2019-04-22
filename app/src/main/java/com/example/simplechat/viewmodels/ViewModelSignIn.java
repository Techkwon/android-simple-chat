package com.example.simplechat.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.simplechat.R;
import com.example.simplechat.models.User;
import com.example.simplechat.util.APIClient;
import com.example.simplechat.util.APIInterface;
import com.example.simplechat.util.Constants;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewModelSignIn extends AndroidViewModel {


    private MutableLiveData<User> liveUser;
    private Application application;
    private SharedPreferences sharedPreferences;

    public ViewModelSignIn(@NonNull Application application) {
        super(application);
        this.application = application;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void initViewModel(String userId){
        if(liveUser == null){
            liveUser = new MutableLiveData<>();
            authenticate(userId);
        }
    }


    private void authenticate(String userId) {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Observable<User> observable = apiInterface.singIn(userId)
                .delay(1, TimeUnit.SECONDS)
                .doOnError(err -> Log.d("ERROR REPORT", err.getMessage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        CompositeDisposable dp = new CompositeDisposable();
        dp.add(observable.subscribe(this::handleResponse));

    }

    private void handleResponse(User user) throws IOException {
        String result = user.getResult();
        if(result.equals(application.getString(R.string.result_success))){

            liveUser.postValue(user); //update live data

            Constants.userId = user.getUserId();
            Constants.userName = user.getUserName();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_ID, user.getUserId());
            editor.putString(Constants.USER_NAME, user.getUserName());
            editor.apply();

        }else {
            Log.d("SignInFragment", application.getString(R.string.check_id_result) + result);
            Toast.makeText(application, R.string.check_id_valid, Toast.LENGTH_SHORT).show();
        }

    }

    public LiveData<User> getLiveUser(){
        return liveUser;
    }

}
