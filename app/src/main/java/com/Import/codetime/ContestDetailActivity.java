package com.Import.codetime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.database.MyDiskExecutor;

import java.util.GregorianCalendar;

public class ContestDetailActivity extends AppCompatActivity {
    public static final String ID_EXTRA_KEY = "idKey";
    ContestEntry contest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_detail);

        final int id = getIntent().getIntExtra(ID_EXTRA_KEY, -1);

        final AppDatabase appDatabase = AppDatabase.getInstance(this);
        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                contest = appDatabase.ContestDao().getContestById(id);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView name = findViewById(R.id.name);
                        TextView startDate = findViewById(R.id.start_date);
                        TextView endDate = findViewById(R.id.end_date);

                        name.setText(contest.getName());
                        startDate.setText("Start date\n" + contest.getStartDate());
                        endDate.setText("End date\n" + contest.getEndDate());
                    }
                });
            }
        });
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
        startActivity(intent);
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
