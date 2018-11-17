package com.Import.codetime.work;

import android.content.Context;
import android.support.annotation.NonNull;

import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.model.ApiResponse;
import com.Import.codetime.rest.RestApiClient;
import com.Import.codetime.utils.DbUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Response;

public class FreshDataWork extends Worker {
    public static final String CONTEST_TYPE_KEY = "type key";
    public static final String PREF_CHANGED_KEY = "pref changed";


    public FreshDataWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        AppDatabase appDatabase = AppDatabase.getInstance(context);

        // first clear database if pref were changed
        if (getInputData().getBoolean(PREF_CHANGED_KEY, false))
            appDatabase.ContestDao().deleteAllContests();

        // clear the the contests of this type
        final int CONTEST_TYPE = getInputData().getInt(CONTEST_TYPE_KEY, -1);
        appDatabase.ContestDao().deleteContestsByType(CONTEST_TYPE);

        // fetch fresh data
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
                + "T"
                + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String resourcesRegex = DbUtils.getFavouriteResourcesRegex(context);

        Response<ApiResponse> response = null;
        switch (CONTEST_TYPE) {
            case DbUtils.TYPE_PAST_EVENTS:
                try {
                    response = RestApiClient.getInstance().getPastContests(date, resourcesRegex, "-end").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Result.FAILURE;
                }
                break;
            case DbUtils.TYPE_ONGOING_EVENTS:
                try {
                    response = RestApiClient.getInstance().getOnGoingContests(date, date, resourcesRegex, "end").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Result.FAILURE;
                }
                break;
            case DbUtils.TYPE_FUTURE_EVENTS:
                try {
                    response = RestApiClient.getInstance().getFutureContests(date, resourcesRegex, "start").execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return Result.FAILURE;
                }
                break;
        }

        if (response == null || response.body() == null) {
            return Result.FAILURE;
        }

        List<ContestEntry> contestEntryList = DbUtils.convertToContestEntryType(
                response.body().getContests(),
                CONTEST_TYPE
        );

        // now insert freshly fetched data in database
        appDatabase.ContestDao().insertContests(contestEntryList);

        return Result.SUCCESS;
    }
}
