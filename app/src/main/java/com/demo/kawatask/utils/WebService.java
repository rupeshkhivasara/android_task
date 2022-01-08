package com.demo.kawatask.utils;

import com.demo.kawatask.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebService {
    @GET("api/")
    Call<UserModel> GET_User_Info(  @Query("inc") String source,@Query("results") int pageNumber);

}

