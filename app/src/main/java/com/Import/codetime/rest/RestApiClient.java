package com.Import.codetime.rest;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.Import.codetime.rest.RestApiConstants.API_BASE_URL;

/**
 * Created by Nitin verma on 03/11/2018
 */
public class RestApiClient {
    private static RestApiService restApiService = null;
    private static String mUserName = null;
    private static String mKey = null;

    private RestApiClient() {
    }

    public static void setAuthParam(String userName, String key) {
        mUserName = userName;
        mKey = key;
    }

    public static RestApiService getInstance() {
        if (restApiService == null) {
            // add api key in header of every request but first check if user has provided valid username and key. If username and key are not set then instance will not be made
            try {
                if (mUserName == null || mKey == null) {
                    throw new RuntimeException("set username/key by calling RestApiClient.setAuthParam method and pass in required parameters.");
                }
            } catch (RuntimeException ex) {
                Log.e("Error", ex.getMessage());
            }

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Authorization", "ApiKey " + mUserName + ":" + mKey)
                                    .build();
                            return chain.proceed(request);
                        }
                    }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restApiService = retrofit.create(RestApiService.class);
        }
        return restApiService;
    }
}
