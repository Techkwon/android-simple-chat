package com.example.simplechat;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.example.simplechat.models.ChatMessage;
import com.example.simplechat.util.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 * 1. What is this service:
 * This service is for opening client socket, and it waits for the coming object from another client
 *
 * 2. oos: sends object of ChatMessage class to server, and server passes it to another client
 * 3. ois: reads object of ChatMessage class from server(other clients)
 * */

public class SocketService extends Service {
    private String TAG = "SocketService";
    public static Socket socket;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;
    private SharedPreferences pref;

    private String userId;
    private String userName;

    @Override
    public void onCreate() {
        super.onCreate();
        connectSocket();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        Log.d(TAG, userId + " | " + userName);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void connectSocket(){

        new Thread(() -> { //Lambda for run()
            try {
                socket = new Socket(Constants.ip, Constants.port); //serverSocket.accept() awaits this line of code for opening new client socket
                showToast(getApplication().getString(R.string.socket_connected));

                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject(new ChatMessage("sign in", userId, userName, "", "", "sign in request"));
                oos.flush();

                ChatMessage msg = (ChatMessage)ois.readObject();

                if(msg.getGroupType().equals("sign in") && msg.getMessage().equals("success")){
                    waitForMessages();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void waitForMessages(){
        try {
            while(ois != null){

                ChatMessage msg = (ChatMessage)ois.readObject();
                String groupType = msg.getGroupType();
                String senderId = msg.getSenderId();
                String senderName = msg.getSenderName();
                String msgBody = msg.getMessage();


                switch (groupType){
                    case "p2p":
                    case "groupMsg":
                        if (Constants.CURRENT_ROOM.equals(senderId) || Constants.CURRENT_ROOM.equals("GROUP")) {
                            sendBundleMsg(msg);
                        }

                        break;

                    case "noti":

                        break;

                    case "sign out":
                        socket.close();
                        socket = null;
                        ois.close();
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBundleMsg(ChatMessage message){
        Bundle bundle = new Bundle();
        Message m = new Message();
        m.setData(bundle);
        bundle.putParcelable("msg", message);
        Constants.msgHandler.sendMessage(m);
    }


    private void showToast(String msg) {
        if(null != getBaseContext()){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run(){
                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
