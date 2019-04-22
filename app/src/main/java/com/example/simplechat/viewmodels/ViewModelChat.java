package com.example.simplechat.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.simplechat.SocketService;
import com.example.simplechat.models.ChatMessage;
import com.example.simplechat.models.Member;
import com.example.simplechat.util.Constants;
import com.example.simplechat.util.DBManager;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewModelChat extends AndroidViewModel {


    private MutableLiveData<List<ChatMessage>> liveMessages;
    private List<ChatMessage> messages = new ArrayList<>();
    private ChatMessage mChatMessage;
    private DBManager db;
    private String memberId;

    public ViewModelChat(@NonNull Application application, String memberId) {
        super(application);
        this.memberId = memberId;
        initViewModel();
    }


    private void initViewModel(){
        if(liveMessages == null){
            liveMessages = new MutableLiveData<>();
            db = new DBManager(getApplication(), "data.db", null, 1, Constants.userId + "_" + memberId);
            getMessages();
            waitForNewMessage();
        }
    }

    private void getMessages(){
        HashMap<String, ArrayList> msgMap = db.getMsgMap();
        if(msgMap.containsKey(Constants.MAP_MSG_CHAT)){
            messages.addAll(msgMap.get(Constants.MAP_MSG_CHAT));
            liveMessages.setValue(messages);
        }
    }


    @SuppressLint("HandlerLeak")
    private void waitForNewMessage(){
        Constants.msgHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();
                ChatMessage chatMessage = (ChatMessage)bundle.getParcelable("msg");
                mChatMessage = chatMessage;

                messages.add(mChatMessage);
                liveMessages.postValue(messages);
                db.insertMsg(chatMessage);
            }
        };
    }

    public void setData(ChatMessage chatMessage){
        mChatMessage = chatMessage;
        messages.add(mChatMessage);
        liveMessages.postValue(messages);
        sendObject(chatMessage);

        db.insertMsg(chatMessage);

    }


    private void sendObject(ChatMessage chatMessage){
        new Thread(() -> {
            try {
                SocketService.oos.writeObject(chatMessage);
                SocketService.oos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


    public LiveData<List<ChatMessage>> getLiveMessage(){
        return liveMessages;
    }

}
