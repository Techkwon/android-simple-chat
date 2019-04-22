package com.example.simplechat.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Member {

    public Member(String memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }

    private String memberId;

    private String memberName;

    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    @SerializedName("data")
    public List<Datum> data = null;


    public class Datum {
        @SerializedName("memberId")
        public String memberId;

        @SerializedName("memberName")
        public String memberName;
    }
}
