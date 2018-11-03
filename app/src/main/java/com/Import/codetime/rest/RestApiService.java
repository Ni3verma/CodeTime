package com.Import.codetime.rest;

import com.Import.codetime.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApiService {
    @GET("contest")
    Call<ApiResponse> getAllContests();
}
