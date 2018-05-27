package com.Import.codetime;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestListFragment extends Fragment {


    public ContestListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contest_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Events");

        ArrayList<FakeEventData> eList = setUpFakeData();
        EventListAdapter adapter=new EventListAdapter(eList);
        RecyclerView recyclerView=view.findViewById(R.id.contest_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<FakeEventData> setUpFakeData() {
        ArrayList<FakeEventData> eList = new ArrayList<>();
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","Euler competition 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"codeShef","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","nitin nitin nitin ntiitn nitin"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","Euler competition 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"codeShef","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","nitin nitin nitin ntiitn nitin"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","Euler competition 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"codeShef","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","nitin nitin nitin ntiitn nitin"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","Euler competition 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"codeShef","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","nitin nitin nitin ntiitn nitin"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","Euler competition 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"codeShef","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","nitin nitin nitin ntiitn nitin"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));

        return eList;
    }
}
