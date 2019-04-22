package com.example.simplechat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ChatMessage implements Serializable, Parcelable {

    //Serialzable is needed for sending object to java server
    //Parcelable is needed for passing object between classes


    private static final long serialVersionUID = 1L;

    private String groupType;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String message;

    public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>(){
        @Override
        public ChatMessage createFromParcel(Parcel source) {
            return new ChatMessage(source);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public ChatMessage(String groupType, String senderId, String senderName, String receiverId, String receiverName, String message) {
        this.groupType = groupType;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.message = message;
    }

    private ChatMessage(Parcel in){
        groupType = in.readString();
        senderId = in.readString();
        senderName = in.readString();
        receiverId = in.readString();
        receiverName = in.readString();
        message = in.readString();
    }

    public String getGroupType() {
        return groupType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(groupType);
        dest.writeString(senderId);
        dest.writeString(senderName);
        dest.writeString(receiverId);
        dest.writeString(receiverName);
        dest.writeString(message);
    }
}
