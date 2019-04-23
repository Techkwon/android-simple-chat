package com.example.simplechat.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplechat.MainActivity;
import com.example.simplechat.R;
import com.example.simplechat.SocketService;
import com.example.simplechat.adapters.AdapterForMembers;
import com.example.simplechat.models.ChatMessage;
import com.example.simplechat.models.Member;
import com.example.simplechat.util.Constants;
import com.example.simplechat.viewmodels.ViewModelMembers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Members list shows up in this fragment.
 * Choose a member or Group for chat
 *
 * MembersFragment shares <ViewModelMembers/> with ChangeNameDialog.
 * */

public class MembersFragment extends Fragment {

    private List<Member> members;
    private AdapterForMembers adapter;
    private TextView tv_userName;
    private ProgressBar progressBar;
    private ViewModelMembers viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_members, container, false);
        initView(layout);
        setViewModel();
        observeDataChange();
        return layout;
    }


    private void setViewModel(){
        if (viewModel == null){
            viewModel = ViewModelProviders.of(getActivity()).get(ViewModelMembers.class);
            viewModel.initViewModel();
        }
    }

    private void observeDataChange(){
        viewModel.getLiveMember().observe(this, membersData -> { //update member list
            progressBar.setVisibility(View.GONE);
            members.addAll(membersData);
            adapter.notifyDataSetChanged();
        });

        viewModel.getLiveUser().observe(this, userData -> { //update user name
            tv_userName.setText(userData.getUserName());
        });
    }


    void initView(View v){
        getActivity().setTitle("Members");
        ImageView iv_userColor = v.findViewById(R.id.iv_user_color);
        iv_userColor.setImageDrawable(getResources().getDrawable(Constants.userColor));
        tv_userName = v.findViewById(R.id.tv_user_name);
        tv_userName.setOnClickListener(view -> showDialog());
        tv_userName.setText(Constants.userName);
        members = new ArrayList<>();
        progressBar = v.findViewById(R.id.progressBarMember);

        ListView lv = v.findViewById(R.id.listview);
        adapter = new AdapterForMembers(members, getContext());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(((parent, view, position, id) -> {
            moveFragment(members.get(position));
        }));

        Button btn_signOut = v.findViewById(R.id.btn_signOut);
        btn_signOut.setOnClickListener(view -> signOut());
    }


    private void signOut(){

        new Thread(() -> {
            try {
                SocketService.oos.writeObject(new ChatMessage("sign out", Constants.userId, "", "", "", ""));
                SocketService.oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        backToMain();

    }


    private void moveFragment(Member member){
        ChatRoomFragment fragment = new ChatRoomFragment();
        fragment.setMember(member);
        final String tag = fragment.getClass().toString();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit();
    }

    private void showDialog(){
        ChangeNameDialog fragment = new ChangeNameDialog();
        fragment.setOldName(Constants.userName);
        fragment.show(getFragmentManager(), ChangeNameDialog.TAG);
    }

    private void backToMain() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
