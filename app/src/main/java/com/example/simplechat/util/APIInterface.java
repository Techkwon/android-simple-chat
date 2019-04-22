package com.example.simplechat.util;

import com.example.simplechat.models.Member;
import com.example.simplechat.models.User;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {


    @FormUrlEncoded
    @POST("signin/")
    Observable<User> singIn(@Field("id")String id);

    @GET("members/")
    Observable<Member> getMembers(@Query("id") String id);

    @FormUrlEncoded
    @POST("update/")
    Observable<User> updateUserName(@Field("id")String id, @Field("newName")String newName);

}
