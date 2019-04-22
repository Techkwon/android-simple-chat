package com.example.simplechat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplechat.R;
import com.example.simplechat.models.Member;

import java.util.ArrayList;
import java.util.List;

public class AdapterForMembers extends BaseAdapter {

    private List<Member> members = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;


    public AdapterForMembers(List<Member> members, Context context) {
        this.members = members;
        this.context = context;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MyViewHolder vh;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_member, null);
            vh = new MyViewHolder(convertView);
            convertView.setTag(vh);

        } else {
            vh = (MyViewHolder)convertView.getTag();
        }

        vh.tv_memberName.setText(members.get(position).getMemberName());
        String memberId = (members.get(position).getMemberId());
        setMemberColor(memberId, vh);

        return convertView;
    }

    private void setMemberColor(String memberId, MyViewHolder vh){
        if(memberId.equals(context.getResources().getString(R.string.id_red))){
            vh.iv_memberColor.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_red));
        }else if(memberId.equals(context.getResources().getString(R.string.id_yello))){
            vh.iv_memberColor.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_yello));
        }else if(memberId.equals(context.getResources().getString(R.string.id_blue))){
            vh.iv_memberColor.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_blue));
        }else if(memberId.equals(context.getString(R.string.id_group))){
            vh.iv_memberColor.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_group));
        }
    }



    class MyViewHolder {
        private ImageView iv_memberColor;
        private TextView tv_memberName;

        public MyViewHolder(View v) {
            iv_memberColor = v.findViewById(R.id.iv_member_color);
            tv_memberName = v.findViewById(R.id.tv_memberName);
        }
    }
}