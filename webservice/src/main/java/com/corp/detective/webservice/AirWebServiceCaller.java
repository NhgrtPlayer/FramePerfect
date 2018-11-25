package com.corp.detective.webservice;

import com.corp.detective.database.entities.Game;
import com.corp.detective.database.entities.Character;
import com.corp.detective.database.entities.Move;
import com.corp.detective.webservice.responses.AirWebServiceResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AirWebServiceCaller {
    AirWebServiceHandler mAirWebServiceHandler;
    // retrofit object
    Retrofit retrofit;
    // base URL of the web service
    //TODO: Change base URL
    private final String baseUrl = "http://10.24.6.179/";

    // constructor
    public AirWebServiceCaller(AirWebServiceHandler airWebServiceHandler){
        this.mAirWebServiceHandler = airWebServiceHandler;

        OkHttpClient client = new OkHttpClient();

        // for debuggint use HttpInterceptor
        //client.interceptors().add(new HttpInterceptor());

        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }
    // get all records from a web service
    public void getAll(String method, final Type entityType){
        Call<AirWebServiceResponse> call = null;

        AirWebService serviceCaller = retrofit.create(AirWebService.class);
        if (entityType == Game.class){
            call = serviceCaller.getGames(method);
        } else if (entityType == Character.class){
            call = serviceCaller.getCharacters(method);
        } else if (entityType == Move.class){
            call = serviceCaller.getMoves(method);
        }

        if(call != null){
            call.enqueue(new Callback<AirWebServiceResponse>() {
                @Override
                public void onResponse(Response<AirWebServiceResponse> response, Retrofit retrofit) {
                    try {
                        if(response.isSuccess()){
                            if(entityType == Game.class){
                                System.out.println("game");
                                handleGames(response);
                            } else if(entityType == Character.class){
                                System.out.println("character");
                                handleCharacters(response);
                            } else if(entityType == Move.class){
                                System.out.println("move");
                                handleMoves(response);
                            } else
                            {
                                System.out.println("Unrecognized class");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }


    }

    private void handleGames(Response<AirWebServiceResponse> response) {
        Gson gson = new Gson();
        Game[] gameItems = gson.fromJson(response.body().getItems(), Game[].class);
        if(mAirWebServiceHandler != null){
            mAirWebServiceHandler.onDataArrived(Arrays.asList(gameItems), true, response.body().getTimeStamp());
        }
    }

    private void handleCharacters(Response<AirWebServiceResponse> response) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // response JSON format
                .create();
        Character[] characterItems = gson.fromJson(response.body().getItems(), Character[].class);
        if(mAirWebServiceHandler != null){
            mAirWebServiceHandler.onDataArrived(Arrays.asList(characterItems), true, response.body().getTimeStamp());
        }
    }

    private void handleMoves(Response<AirWebServiceResponse> response) {
        Gson gson = new Gson();
        Move[] moveItems = gson.fromJson(response.body().getItems(), Move[].class);
        if(mAirWebServiceHandler != null){
            mAirWebServiceHandler.onDataArrived(Arrays.asList(moveItems), true, response.body().getTimeStamp());
        }
    }
}

