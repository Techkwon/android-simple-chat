package com.example.simplechat.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.simplechat.models.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DBManager class is a manager for SQLite database which saves data in your device.
 * Data(Object of ChatMessage) can be saved with insertMsg(),
 * and the data can be called with the getMsgMap().
 *
 * With the table name, you can handle data for each chat room for each userId
 *
 *
 * */


public class DBManager extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private String tableName;

    public DBManager(
            @Nullable Context context,
            @Nullable String name,
            @Nullable SQLiteDatabase.CursorFactory factory,
            int version,
            String tableName
    ) {
        super(context, name, factory, version);
        this.tableName = tableName;  //tableName = userName + roomName;

        createTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    private void createTable(){
        db = getWritableDatabase();
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + tableName +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "groupType TEXT, " +
                        "senderId TEXT, " +
                        "senderName TEXT, " +
                        "receiverid TEXT, " +
                        "receiverName TEXT, " +
                        "msg TEXT);"
        );
        db.close();
    }

    public void insertMsg(ChatMessage msg){
        db = getWritableDatabase();
        db.execSQL("INSERT INTO " + tableName + " VALUES (null, '" +
                msg.getGroupType() + "', '" +
                msg.getSenderId() + "', '" +
                msg.getSenderName() + "', '" +
                msg.getReceiverId() + "', '" +
                msg.getReceiverName() + "', '" +
                msg.getMessage() + "')");
        db.close();
    }

    public HashMap<String, ArrayList> getMsgMap(){
        HashMap<String, ArrayList> msgMap = new HashMap<>();
        ArrayList<ChatMessage> messageList = new ArrayList<>();

        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        while (cursor.moveToNext()){
            String groupType = cursor.getString(1);
            String senderId = cursor.getString(2);
            String senderName = cursor.getString(3);
            String receiverId = cursor.getString(4);
            String receiverName = cursor.getString(5);
            String msg = cursor.getString(6);
            messageList.add(new ChatMessage(groupType, senderId, senderName, receiverId, receiverName, msg));
        }

        msgMap.put(Constants.MAP_MSG_CHAT, messageList);

        return msgMap;
    }//getMsgMap...
}
