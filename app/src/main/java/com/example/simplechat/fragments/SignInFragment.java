package com.example.simplechat.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import com.example.simplechat.R;
import com.example.simplechat.SocketService;
import com.example.simplechat.util.Constants;
import com.example.simplechat.viewmodels.ViewModelSignIn;

/**
 * This Fragment is where you can sign in.
 * you can choose a color as your Id among three colors: RED, YELLOW and BLUE.
 * Then you can chat with the other users who use remaining colors.
 *
 * */

public class SignInFragment extends Fragment {

    private Button btn_singin;
    private Resources res;
    private String selectedId;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        initView(layout);
        return layout;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setId(int id){
        int mId = 0;
        int drawable = 0;
        switch (id){
            case R.string.id_red:
                mId = res.getColor(R.color.id_Red);
                drawable = R.drawable.circle_red;
                selectedId = res.getString(R.string.id_red);
                break;
            case R.string.id_yello:
                mId = res.getColor(R.color.id_Yellow);
                drawable = R.drawable.circle_yello;
                selectedId = res.getString(R.string.id_yello);
                break;

            case R.string.id_blue:
                mId = res.getColor(R.color.id_Blue);
                drawable = R.drawable.circle_blue;
                selectedId = res.getString(R.string.id_blue);
                break;
        }

        btn_singin.setVisibility(View.VISIBLE);
        btn_singin.setBackgroundColor(mId);
        Constants.userColor = drawable;
    }



    private void setViewModel(String userId){
        ViewModelSignIn viewmodel = ViewModelProviders.of(getActivity()).get(ViewModelSignIn.class);
        viewmodel.initViewModel(userId);
        viewmodel.getLiveUser().observe(this, mUser -> { //observe data change on MutatbleDat<User>
            progressBar.setVisibility(View.INVISIBLE);
            moveFragment();
        });
    }


    private void moveFragment(){
        Fragment fragment = new MembersFragment();
        final String tag = fragment.getClass().toString();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(android.R.id.content, fragment, tag)
                .commit();
        showSnackBarMessage("sign in successfully!");

        Intent service = new Intent(getContext(), SocketService.class);
        service.putExtra("userId", Constants.userId);
        service.putExtra("userName", Constants.userName);
        getActivity().startService(service);
    }


    private void initView(View v){
        getActivity().setTitle("Sign In");

        res = getResources();
        selectedId = "";

        Button btn_red = v.findViewById(R.id.btn_red);
        btn_red.setOnClickListener(view -> setId(R.string.id_red));

        Button btn_yellow = v.findViewById(R.id.btn_yello);
        btn_yellow.setOnClickListener(view -> setId(R.string.id_yello));

        Button btn_blue = v.findViewById(R.id.btn_blue);
        btn_blue.setOnClickListener(view -> setId(R.string.id_blue));

        progressBar = v.findViewById(R.id.progressBar);

        btn_singin = v.findViewById(R.id.btn_signIn);
        btn_singin.setVisibility(View.INVISIBLE);
        btn_singin.setOnClickListener(view -> {
            setViewModel(selectedId);
            btn_singin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        });

    }

    private void showSnackBarMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }
    }

}
