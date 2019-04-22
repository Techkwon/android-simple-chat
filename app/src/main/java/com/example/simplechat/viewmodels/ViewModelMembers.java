package com.example.simplechat.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.simplechat.SocketService;
import com.example.simplechat.models.ChatMessage;
import com.example.simplechat.models.Member;
import com.example.simplechat.models.User;
import com.example.simplechat.util.APIClient;
import com.example.simplechat.util.APIInterface;
import com.example.simplechat.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * This is shared ViewModel for <MembersFragment/> & <ChangeNameDialog/>,
 * so that MemberFragment can observe change on user name from ChangeNameDailog..
 * */

public class ViewModelMembers extends AndroidViewModel {

    private MutableLiveData<User> liveUser;
    private MutableLiveData<List<Member>> liveMemberList;
    private APIInterface apiInterface;

    public ViewModelMembers(@NonNull Application application) {
        super(application);
    }

    public void initViewModel(){
        if(liveUser == null){
            liveUser = new MutableLiveData<>();
        }

        if(liveMemberList == null){
            liveMemberList = new MutableLiveData<>();
            apiInterface = APIClient.getClient().create(APIInterface.class);
            getMembers();
        }

    }



    void getMembers(){
        Observable<Member> observable = apiInterface.getMembers(Constants.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        CompositeDisposable dp = new CompositeDisposable();
        dp.add(observable.subscribe(this::handleResponse));
    }

    private void handleResponse(Member member) throws IOException{

        List<Member.Datum> memberList = member.data;

        List<Member> members = new ArrayList<>();
        Constants.GROUP_MEMBER_IDs = "";
        Constants.GROUP_MEMBER_NAMEs = "";

        for(Member.Datum datum : memberList){
            members.add(new Member(datum.memberId, datum.memberName));
            Log.d("MembersFragment", "Received data.......memberId: " + datum.memberId + "  memberName: " + datum.memberName);

        }

        for(int i = 0; i < members.size(); i ++){
            if (i == 0) {
                Constants.GROUP_MEMBER_IDs += members.get(i).getMemberId() + ",";
                Constants.GROUP_MEMBER_NAMEs += members.get(i).getMemberName() + ",";
            } else {
                Constants.GROUP_MEMBER_IDs += members.get(i).getMemberId();
                Constants.GROUP_MEMBER_NAMEs += members.get(i).getMemberName();
            }
        }

        members.add(new Member("GROUP", "group"));
        liveMemberList.postValue(members);
    }

    public void setNewUser(User user){
        liveUser.postValue(user);
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
    }

    public LiveData<List<Member>> getLiveMember(){
        return liveMemberList;
    }

    public LiveData<User> getLiveUser(){
        return liveUser;
    }

}
