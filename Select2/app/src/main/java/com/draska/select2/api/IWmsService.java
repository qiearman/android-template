package com.draska.select2.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mytop on 10/12/2017.
 */
public interface IWmsService {

    @GET("api/webpo/ConsumableItem.aspx")
    Call<String> getSources(
            @Query("act") String action,
            @Query("keyword") String keyword
    );
}