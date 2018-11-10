package com.Import.codetime;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.database.MyDiskExecutor;

import java.util.Calendar;
import java.util.TimeZone;

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
        //bug: not working
        Calendar startTime = Calendar.getInstance();
        startTime.set(2018, 11, 12, 1, 19);
        long startTimeMillis = startTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 11, 13, 1, 20);
        long endTimeMillis = endTime.getTimeInMillis();

        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTimeMillis);
        values.put(CalendarContract.Events.DTEND, endTimeMillis);
        values.put(CalendarContract.Events.TITLE, "time to CODE");
        values.put(CalendarContract.Events.DESCRIPTION, "Check out this contest:\n" + contest.getName() + "\nClick on this link and register for it: " + contest.getURL());
        values.put(CalendarContract.Events.CALENDAR_ID, 3);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);

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
