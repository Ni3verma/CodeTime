package com.Import.codetime;


import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.Import.codetime.database.AppDatabase;
import com.Import.codetime.database.ContestEntry;
import com.Import.codetime.database.MyDiskExecutor;
import com.Import.codetime.rest.RestApiClient;
import com.Import.codetime.utils.DbUtils;
import com.Import.codetime.work.FreshDataWork;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FrameLayout frameContainer;
    private ImageView image_background_blur;
    private Context mContext;
    private List<ContestEntry> contestEntryList;
    private EventListAdapter adapter;

    public static final String PAST_KEY = "Past Events";
    public static final String ONGOING_KEY = "Ongoing Events";
    public static final String FUTURE_KEY = "Future Events";
    public static final String EVENT_TYPE = "type";
    private static final String TAG = "Nitin";

    private AppDatabase mDb;


    public ContestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contest_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext=getActivity();
        mDb = AppDatabase.getInstance(mContext);
        frameContainer=view.findViewById(R.id.frame_container);
        image_background_blur=view.findViewById(R.id.bg_blur_iv);
        recyclerView=view.findViewById(R.id.contest_list_rv);
        ImageView imageView = view.findViewById(R.id.image_type);
        contestEntryList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.requestDisallowInterceptTouchEvent(true);
        recyclerView.addOnItemTouchListener(listener);

        Bundle arguments = getArguments();
        assert arguments != null;
        final String type = arguments.getString(EVENT_TYPE);
        int contestTypeInt = -1;
        assert type != null;

        setAPICredentials();

        switch (type) {
            case PAST_KEY:
                contestTypeInt = DbUtils.TYPE_PAST_EVENTS;
                imageView.setImageResource(R.drawable.past_sand_clock);
                break;
            case ONGOING_KEY:
                contestTypeInt = DbUtils.TYPE_ONGOING_EVENTS;
                imageView.setImageResource(R.drawable.present_sand_clock);
                break;
            case FUTURE_KEY:
                contestTypeInt = DbUtils.TYPE_FUTURE_EVENTS;
                imageView.setImageResource(R.drawable.future_man_stop_clock);
                break;
        }

        getContestsByType(contestTypeInt);

        FloatingActionButton fab = view.findViewById(R.id.refresh_fab);
        final int finalContestTypeInt = contestTypeInt;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(finalContestTypeInt);
            }
        });
        Objects.requireNonNull(getActivity()).setTitle(type);
    }

    private void refreshData(final int finalContestTypeInt) {
        Data data = new Data.Builder().putInt(FreshDataWork.CONTEST_TYPE_KEY, finalContestTypeInt).build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(FreshDataWork.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance().enqueueUniqueWork("type" + finalContestTypeInt, ExistingWorkPolicy.KEEP, request);

        WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId())
                .observe(Objects.requireNonNull(getActivity()), new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().name().equals(WorkInfo.State.FAILED.name())) {
                            Log.d(TAG, "state -> " + workInfo.getState().name());
                            Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.contest_list_rv), "Check internet connection", Snackbar.LENGTH_SHORT).show();
                        } else if (workInfo != null && workInfo.getState().isFinished()) {
                            Log.d(TAG, "data refreshed");
                            updateList(finalContestTypeInt);
                        }
                    }
                });
    }

    private void updateList(final int type) {
        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<ContestEntry> list = mDb.ContestDao().getContestsByType(type);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter(list);
                    }
                });
            }
        });
    }

    private void setAdapter(List<ContestEntry> list) {
        ContestListClickListener clickListener = new ContestListClickListener() {
            @Override
            public void onContestClick(int id) {
                // open detail activity
                Intent intent = new Intent(getActivity(), ContestDetailActivity.class);
                intent.putExtra(ContestDetailActivity.ID_EXTRA_KEY, id);
                startActivity(intent);
            }
        };
        if (list.size() == 0)
            recyclerView.setBackground(getResources().getDrawable(R.drawable.empty_view_rv));
        adapter = new EventListAdapter(list, mContext, ContestListFragment.this, clickListener);
        recyclerView.setAdapter(adapter);
    }

    private void getContestsByType(final int type) {
        final boolean wasPrefChanged = MainActivity.isPrefChanged;
        if (wasPrefChanged)
            MainActivity.isPrefChanged = false;  //  make it false again

        Data data = new Data.Builder().putInt(FreshDataWork.CONTEST_TYPE_KEY, type).putBoolean(FreshDataWork.PREF_CHANGED_KEY, wasPrefChanged).build();

        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(FreshDataWork.class)
                .setInputData(data)
                .build();

        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<ContestEntry> list = mDb.ContestDao().getContestsByType(type);

                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() == 0 || wasPrefChanged) {  // if no data in db or pref was changed, fetch it from internet
                            WorkManager.getInstance().enqueueUniqueWork("ctype" + type, ExistingWorkPolicy.KEEP, request);

                            WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId())
                                    .observe(Objects.requireNonNull(getActivity()), new Observer<WorkInfo>() {
                                        @Override
                                        public void onChanged(@Nullable WorkInfo workInfo) {
                                            if (workInfo != null && workInfo.getState().name().equals(WorkInfo.State.FAILED.name())) {
                                                Log.d(TAG, "state -> " + workInfo.getState().name());
                                                Snackbar.make(Objects.requireNonNull(getView()).findViewById(R.id.contest_list_rv), "Check internet connection", Snackbar.LENGTH_SHORT).show();
                                            } else if (workInfo != null && workInfo.getState().isFinished()) {
                                                Log.d(TAG, "data added");
                                                updateList(type);
                                            }
                                        }
                                    });
                        } else {   // we have data in db, so fetch from here
                            setAdapter(list);
                        }
                    }
                });
            }
        });
    }

    private void setAPICredentials() {
        try {
            String name = getResources().getString(R.string.username);
            String key = getResources().getString(R.string.key);
            RestApiClient.setAuthParam(name, key);
        } catch (Resources.NotFoundException ex) {
            throw new RuntimeException("please provide username and APIkey");
        }
    }

    //if we hold then drag our finger and then lift up....then dialog won't close
    //if we wan't this functionality then add this.
    RecyclerView.OnItemTouchListener listener=new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            Log.d(TAG,"action="+e.getAction());
            if(e.getAction() == MotionEvent.ACTION_UP){
                EventListAdapter adapter=(EventListAdapter) rv.getAdapter();
                removeBlur();

                adapter.hideDialog();
                rv.requestDisallowInterceptTouchEvent(false);
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    public Bitmap blurBitmap(Bitmap bitmap) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(mContext);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    public void blurBackground(){
        frameContainer.setDrawingCacheEnabled(true);
        frameContainer.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        frameContainer.buildDrawingCache();

        if (frameContainer.getDrawingCache() == null)
            return;

        Bitmap snapshot = Bitmap.createBitmap(frameContainer.getDrawingCache());
        frameContainer.setDrawingCacheEnabled(false);
        frameContainer.destroyDrawingCache();

        BitmapDrawable blurredBackground = new BitmapDrawable(mContext.getResources(),blurBitmap(snapshot));
        image_background_blur.setImageDrawable(blurredBackground);

        recyclerView.setVisibility(View.GONE);
    }

    public void removeBlur(){
        recyclerView.setVisibility(View.VISIBLE);
    }
}
