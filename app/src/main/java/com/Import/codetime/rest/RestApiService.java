package com.Import.codetime.rest;

import com.Import.codetime.model.ApiResponse;
import com.Import.codetime.model.Contest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {
    @GET("contest")
    Call<ApiResponse> getPastContests(
            @Query(RestApiConstants.END_DATE_LT_QUERY_PARAM) String endDate,
            @Query(RestApiConstants.RESOURCE_NAME_REGEX_QUERY_PARAM) String resourceRegex,
            @Query(RestApiConstants.ORDER_BY_QUERY_PARAM) String orderBy
    );

    @GET("contest")
    Call<ApiResponse> getOnGoingContests(
            @Query(RestApiConstants.START_DATE_LT_QUERY_PARAM) String startDate,
            @Query(RestApiConstants.END_DATE_GT_QUERY_PARAM) String endDate,
            @Query(RestApiConstants.RESOURCE_NAME_REGEX_QUERY_PARAM) String resourceRegex,
            @Query(RestApiConstants.ORDER_BY_QUERY_PARAM) String orderBy
    );

    @GET("contest")
    Call<ApiResponse> getFutureContests(
            @Query(RestApiConstants.START_DATE_GT_QUERY_PARAM) String date,
            @Query(RestApiConstants.RESOURCE_NAME_REGEX_QUERY_PARAM) String resourceRegex,
            @Query(RestApiConstants.ORDER_BY_QUERY_PARAM) String orderBy
    );

    @GET("contest/{contestId}")
    Call<Contest> getContestById(
            @Path("contestId") int id
    );
}
