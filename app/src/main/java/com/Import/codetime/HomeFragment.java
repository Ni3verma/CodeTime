package com.Import.codetime;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        //setting gradient animation for text views of each card
        TextView tv1 = view.findViewById(R.id.past_events_tv);
        TextView tv2 = view.findViewById(R.id.ongoing_events_tv);
        TextView tv3 = view.findViewById(R.id.future_events_tv);
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
