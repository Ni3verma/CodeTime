package com.Import.codetime;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting gradient animation for text views of each card
        TextView tv1 = findViewById(R.id.past_events_tv);
        TextView tv2 = findViewById(R.id.ongoing_events_tv);
        TextView tv3 = findViewById(R.id.future_events_tv);
        setTextViewAnimation(tv1);
        setTextViewAnimation(tv2);
        setTextViewAnimation(tv3);

    }

    void setTextViewAnimation(TextView tv){
        AnimationDrawable animationDrawable = (AnimationDrawable) tv.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
    }
}
