package com.example.simplechat.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.simplechat.R;
import com.example.simplechat.models.User;
import com.example.simplechat.util.APIClient;
import com.example.simplechat.util.APIInterface;
import com.example.simplechat.util.Constants;
import com.example.simplechat.viewmodels.ViewModelMembers;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ChangeNameDialog extends DialogFragment {

    public static final String TAG = ChangeNameDialog.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private String oldName;
    private TextInputLayout ti_newName;
    private ProgressBar progressBar;
    private ViewModelMembers viewmodel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.dialog_change_name, container, false);

        initView(layout);
        setViewModel();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return layout;
    }

    public void setOldName(String oldName){
        this.oldName = oldName;
    }

    private void changeName(String newName){

        ti_newName.setError(null);

        if(!newName.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

            Observable<User> observable = apiInterface.updateUserName(Constants.userId, newName)
                    .delay(1, TimeUnit.SECONDS)
                    .doOnError(error -> Log.d("ERROR REPORT", error.getMessage()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            CompositeDisposable dp = new CompositeDisposable();
            dp.add(observable.subscribe(this::handleResponse));

        }else{
            ti_newName.setError(getString(R.string.new_name_invalid));
        }
    }


    private void handleResponse(User user) throws IOException{
        String result = user.getResult();
        if(result.equals(getActivity().getString(R.string.result_success))){

            viewmodel.setNewUser(user);
            Constants.userName = user.getUserName();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_NAME, user.getUserName());
            editor.apply();

        }else {
            Toast.makeText(getActivity(), R.string.server_problem, Toast.LENGTH_SHORT).show();
        }

        dismiss();
    }

    private void setViewModel(){
        viewmodel = ViewModelProviders.of(getActivity()).get(ViewModelMembers.class);
    }


    private void initView(View v){

        ti_newName = v.findViewById(R.id.ti_new_name);
        EditText et_newName = v.findViewById(R.id.et_new_name);
        progressBar = v.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        et_newName.setText(oldName);
        Button btn_changeName = v.findViewById(R.id.btn_change_name);
        btn_changeName.setOnClickListener(view -> changeName(et_newName.getText().toString()));
        Button btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(view -> dismiss());
    }

}
