package com.Import.codetime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.database.MyDiskExecutor;

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

    }

    public void shareContest(View view) {
    }

    public void openInBrowser(View view) {
    }
}
