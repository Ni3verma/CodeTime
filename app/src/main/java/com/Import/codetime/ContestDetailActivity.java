package com.Import.codetime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ContestDetailActivity extends AppCompatActivity {
    public static final String ID_EXTRA_KEY = "idKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_detail);

        int id = getIntent().getIntExtra(ID_EXTRA_KEY, -1);
    }
}
