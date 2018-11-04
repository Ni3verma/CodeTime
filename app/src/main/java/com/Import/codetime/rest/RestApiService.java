package com.Import.codetime.rest;

import com.Import.codetime.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiService {
    @GET("contest")
    Call<ApiResponse> getAllContests();

    @GET("contest")
    Call<ApiResponse> getPastContests(
            @Query(RestApiConstants.END_DATE_LT_QUERY_PARAM) String endDate,
            @Query(RestApiConstants.RESOURCE_NAME_REGEX_QUERY_PARAM) String resourceRegex,
            @Query(RestApiConstants.ORDER_BY_QUERY_PARAM) String orderBy
    );
}
