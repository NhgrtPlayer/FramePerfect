package com.corp.detective.webservice;

import com.corp.detective.webservice.responses.AirWebServiceResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface AirWebService {
    @FormUrlEncoded
    @POST("games.php")
    Call<AirWebServiceResponse> getGames(@Field("method") String method);

    @FormUrlEncoded
    @POST("characters.php")
    Call<AirWebServiceResponse> getCharacters(@Field("method") String method);

    @FormUrlEncoded
    @POST("moves.php")
    Call<AirWebServiceResponse> getMoves(@Field("method") String method);
}
