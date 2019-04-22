package com.example.simplechat.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplechat.R;
import com.example.simplechat.models.ChatMessage;
import com.example.simplechat.util.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterForChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> messageList = new ArrayList<>();
    private Context context;

    public AdapterForChat(List<ChatMessage> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {

        String senderId = messageList.get(position).getSenderId();

        return Constants.userId.equals(senderId) ? 0 : 1; //if ths message from myself returns 0, otherwise 1
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;

        switch (viewType) {
            case 0:
                View v0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_sent, parent, false);
                vh = new ViewHolder_For_My_Text(v0);
                break;

            case 1:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_received, parent, false);
                vh = new ViewHolder_For_Member_Text(v2);
                break;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = messageList.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder_For_My_Text vh0 = (ViewHolder_For_My_Text)holder;
                vh0.tv_myMsg.setText(msg.getMessage());
                break;

            case 1:
                ViewHolder_For_Member_Text vh2 = (ViewHolder_For_Member_Text)holder;
                vh2.tv_memberName.setText(msg.getSenderName());
                vh2.tv_memberText.setText(msg.getMessage());
                vh2.iv_memberColor.setImageDrawable(getSenderColor(msg.getSenderId()));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }



    private Drawable getSenderColor(String id){
        Drawable color = null;

        switch(id){
            case "RED":
                color = context.getResources().getDrawable(R.drawable.circle_red);
                break;

            case "YELLOW":
                color = context.getResources().getDrawable(R.drawable.circle_yello);
                break;

            case "BLUE":
                color = context.getResources().getDrawable(R.drawable.circle_blue);
                break;
        }
        return color;
    }



    /**
     * We have twoViewHolder classes for different items types
     * */

    public class ViewHolder_For_My_Text extends RecyclerView.ViewHolder {

        private TextView tv_myMsg;
        private TextView tv_myMsgTime;

        public ViewHolder_For_My_Text(View v) {
            super(v);
            tv_myMsg = v.findViewById(R.id.tv_my_msg);
            tv_myMsgTime = v.findViewById(R.id.text_message_time);
        }
    }

    public class ViewHolder_For_Member_Text extends RecyclerView.ViewHolder {

        private ImageView iv_memberColor;
        private TextView tv_memberName;
        private TextView tv_memberText;
        private TextView tv_memberTextTime;

        public ViewHolder_For_Member_Text(View v) {
            super(v);
            iv_memberColor = v.findViewById(R.id.image_message_profile);
            tv_memberName = v.findViewById(R.id.tv_sender_name);
            tv_memberText = v.findViewById(R.id.tv_sender_msg);
            tv_memberTextTime = v.findViewById(R.id.text_message_time);
        }
    }

}
