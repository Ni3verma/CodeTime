package com.Import.codetime;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.database.FavouriteEntry;
import com.Import.codetime.database.MyDiskExecutor;
import com.Import.codetime.model.Contest;
import com.Import.codetime.rest.RestApiClient;
import com.Import.codetime.utils.DbUtils;
import com.Import.codetime.widget.WidgetUpdateService;
import com.squareup.picasso.Picasso;

import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContestDetailActivity extends AppCompatActivity {
    public static final String ID_EXTRA_KEY = "contestIdKey";
    ContestEntry contest;
    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_detail);

        final int id = getIntent().getIntExtra(ID_EXTRA_KEY, -1);

        appDatabase = AppDatabase.getInstance(this);
        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                contest = appDatabase.ContestDao().getContestById(id);

                if (contest == null) {
                    //doesn't exist in database, so fetch details from internet. This case may occur when user clicks on list item of app widget
                    try {
                        String name = getResources().getString(R.string.username);
                        String key = getResources().getString(R.string.key);
                        RestApiClient.setAuthParam(name, key);
                    } catch (Resources.NotFoundException ex) {
                        throw new RuntimeException("please provide username and APIkey");
                    }
                    RestApiClient.getInstance().getContestById(id).enqueue(new Callback<Contest>() {
                        @Override
                        public void onResponse(@NonNull Call<Contest> call, @NonNull Response<Contest> response) {
                            Contest temp = response.body();
                            if (temp != null) {

                                contest = new ContestEntry(
                                        temp.getEvent(),
                                        temp.getHref(),
                                        temp.getId(),
                                        DbUtils.getFormattedDate(temp.getStart()),
                                        DbUtils.getFormattedDate(temp.getEnd()),
                                        temp.getResource().getName(),
                                        -1);
                            }

                            initViews();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Contest> call, @NonNull Throwable t) {
                            Snackbar.make(findViewById(R.id.parent_layout), "check internet connection", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViews();
                    }
                });
            }
        });
    }

    private void initViews() {
        TextView name = findViewById(R.id.name);
        TextView startDate = findViewById(R.id.start_date);
        TextView endDate = findViewById(R.id.end_date);
        ImageView poster = findViewById(R.id.poster);

        if (contest != null) {
            name.setText(contest.getName());
            startDate.setText(String.format("Start date\n%s", contest.getStartDate()));
            endDate.setText(String.format("End date\n%s", contest.getEndDate()));
            Picasso.get().load(getPosterResouceId(contest.getResourceName())).into(poster);
        }
    }

    private int getPosterResouceId(String res) {
        res = res.split("com")[0];
        int id;
        switch (res) {
            case "codechef.":
                id = R.drawable.codechef_poster;
                break;
            case "codeforces.":
                id = R.drawable.codeforces_poster;
                break;
            case "hackerearth.":
                id = R.drawable.hackerearth_poster;
                break;
            case "hackerrank.":
                id = R.drawable.hackerrank_poster;
                break;
            case "topcoder.":
                id = R.drawable.topcoder_poster;
                break;
            default:
                id = R.drawable.ic_launcher_background;
        }
        return id;
    }

    public void addToCalender(View view) {
        //optimize: try to add a notification also, see documentation on androidDev.com
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, "time to CODE");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Check out this contest:\n" + contest.getName() + "\nClick on this link and register for it: " + contest.getURL());

        String start[] = contest.getStartDate().split("\n");
        String startDate[] = start[0].split("-");
        String startTime[] = start[1].split(":");
        GregorianCalendar gregorianCalendar = new GregorianCalendar(
                Integer.parseInt(startDate[0]),
                Integer.parseInt(startDate[1]) - 1,   //month is zero based
                Integer.parseInt(startDate[2]),
                Integer.parseInt(startTime[0]),
                Integer.parseInt(startTime[1].split(" ")[0])
        );
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, gregorianCalendar.getTimeInMillis());

        String end[] = contest.getEndDate().split("\n");
        String endDate[] = end[0].split("-");
        String endTime[] = end[1].split(":");
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar(
                Integer.parseInt(endDate[0]),
                Integer.parseInt(endDate[1]) - 1,
                Integer.parseInt(endDate[2]),
                Integer.parseInt(endTime[0]),
                Integer.parseInt(endTime[1].split(" ")[0])
        );
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, gregorianCalendar2.getTimeInMillis());

        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        saveToDatabase();

        startActivity(intent);
    }

    private void saveToDatabase() {
        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.FavouriteDao().insertFavouriteContest(new FavouriteEntry(
                        contest.getContestId(),
                        contest.getName(),
                        contest.getResourceName()));

                WidgetUpdateService.startActionUpdateAppWidget(getApplicationContext());
            }
        });
    }

    public void shareContest(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Time to CODE !!");
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this contest:\n" + contest.getName() + "\nClick on this link and register for it: " + contest.getURL());
        startActivity(Intent.createChooser(intent, "sharing is caring"));
    }

    public void openInBrowser(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(contest.getURL()));
        startActivity(intent);
    }
}
