package com.Import.codetime;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        //setting gradient animation for text views of each card
        TextView tv1 = view.findViewById(R.id.past_events_tv);
        TextView tv2 = view.findViewById(R.id.ongoing_events_tv);
        TextView tv3 = view.findViewById(R.id.future_events_tv);
        setTextViewAnimation(tv1);
        setTextViewAnimation(tv2);
        setTextViewAnimation(tv3);

        CardView past_cv=view.findViewById(R.id.past);
        CardView present_cv=view.findViewById(R.id.onGoing);
        CardView future_cv=view.findViewById(R.id.future);

        past_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.past_iv).setTransitionName(getString(R.string.image_type_transition));
                setFragment(ContestListFragment.PAST_KEY, (ImageView) view.findViewById(R.id.past_iv));
            }
        });
        present_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.ongoing_iv).setTransitionName(getString(R.string.image_type_transition));
                setFragment(ContestListFragment.ONGOING_KEY, (ImageView) view.findViewById(R.id.ongoing_iv));
            }
        });
        future_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.future_iv).setTransitionName(getString(R.string.image_type_transition));
                setFragment(ContestListFragment.FUTURE_KEY, (ImageView) view.findViewById(R.id.future_iv));
            }
        });
    }

    private void setFragment(String contestTypeKey, ImageView imageView) {
        FragmentChangeListener fcl = (FragmentChangeListener) getActivity();
        ContestListFragment fragment = new ContestListFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ContestListFragment.EVENT_TYPE, contestTypeKey);
        fragment.setArguments(arguments);

        fcl.openListFragment(fragment, imageView);
    }

    void setTextViewAnimation(TextView tv){
        AnimationDrawable animationDrawable = (AnimationDrawable) tv.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(800);
        animationDrawable.start();
    }
}
