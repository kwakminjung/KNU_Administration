package com.example.knu_administration_android.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.knu_administration_android.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static String BASE_URL = "http://localhost:8080";
    private static RetrofitService retrofitAPI;
    private static RetrofitClient instance = null;

    private RetrofitClient(){

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setLenient()
                .setPrettyPrinting()
                .create();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitAPI =retrofit.create(RetrofitService.class);
    }


    public static RetrofitClient getInstance(){
        if(instance==null){
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static RetrofitService getRetrofitInterface(){
        return retrofitAPI;
    }

}