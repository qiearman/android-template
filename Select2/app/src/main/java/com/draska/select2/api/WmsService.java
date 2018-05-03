package com.draska.select2.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.draska.select2.constant.Constanta;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by mytop on 10/12/2017.
 */
public class WmsService {

    private static Retrofit.Builder mBuilder;

    private static Retrofit mRetrofit;

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {

        mBuilder = new Retrofit.Builder().baseUrl(Constanta.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create());

        mRetrofit = mBuilder.build();

        return mRetrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        mBuilder = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create());

        mRetrofit = mBuilder.build();

        return mRetrofit.create(serviceClass);
    }

    @SuppressLint("LongLogTag")
    public static <S> S createService(Class<S> serviceClass, Context context) {

        String baseUrl = Constanta.BASE_URL;

        Log.d("WmsServices createService : ", "base url = " + baseUrl);

        mBuilder = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create());

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            mBuilder.client(httpClient.build());
            mRetrofit = mBuilder.build();
        }
//        else {
//            mRetrofit = mBuilder.build();
//        }

        return mRetrofit.create(serviceClass);
    }
}
