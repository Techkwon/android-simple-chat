package com.example.simplechat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.simplechat.R;
import com.example.simplechat.adapters.AdapterForChat;
import com.example.simplechat.models.ChatMessage;
import com.example.simplechat.models.Member;
import com.example.simplechat.util.Constants;
import com.example.simplechat.viewmodels.ViewModelChat;


public class ChatRoomFragment extends Fragment {

    private Member member;
    private EditText et_inputMsg;
    private AdapterForChat adapter;
    private RecyclerView rv;
    private ViewModelChat viewModel;
    private Button btn_send;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_chat_room, container, false);
        initView(layout);
        textChangeListener();

        getActivity().setTitle(member.getMemberName());
        showSnackBarMessage(member.getMemberName());
        observeLiveData();

        Constants.CURRENT_ROOM = member.getMemberId(); // RED, YELLO, BLUE or Null
        return layout;
    }


    private void observeLiveData(){
        //update view for message
        viewModel.getLiveMessage().observe(this, chatMessage -> {
            adapter.notifyDataSetChanged();
            if(viewModel.getLiveMessage().getValue().size() > 0)
                rv.smoothScrollToPosition(viewModel.getLiveMessage().getValue().size() - 1);

        });

    }


    private void setDataToViewModel(){ //to be called when click the button
        String msg = et_inputMsg.getText().toString();
        if(!msg.equals("")){
            String groupType = !member.getMemberId().equals("GROUP") ? "p2p" : "groupMsg";

            viewModel.setData(new ChatMessage(
                    groupType, // p2p or groupMsg
                    Constants.userId,
                    Constants.userName,
                    !member.getMemberId().equals("GROUP") ? member.getMemberId() : Constants.GROUP_MEMBER_IDs,
                    !member.getMemberId().equals("GROUP") ? member.getMemberName() : Constants.GROUP_MEMBER_NAMEs,
                    msg
            ));
            et_inputMsg.setText("");
        }
    }

    private void initView(View v){

        ImageView iv_addPic = v.findViewById(R.id.iv_addPic);

        et_inputMsg = v.findViewById(R.id.et_input_msg);
        btn_send = v.findViewById(R.id.btn_send_msg);
        btn_send.setEnabled(false);
        btn_send.setOnClickListener(view -> setDataToViewModel());
        rv = v.findViewById(R.id.recyclerview_chat);
        initRecylerView();
    }

    private void initRecylerView(){
        viewModel = new ViewModelChat(getActivity().getApplication(), member.getMemberId());
        adapter = new AdapterForChat(viewModel.getLiveMessage().getValue(), getContext());
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

    public void setMember(Member member){ //it is to be called at MembersFragment
        this.member = member;
    }

    private void textChangeListener(){

        et_inputMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int inputLength = s.toString().length();

                if(inputLength == 0){
                    btn_send.setEnabled(false);

                } else if (inputLength > 0){
                    btn_send.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void showSnackBarMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Constants.CURRENT_ROOM = "null";
    }
}
