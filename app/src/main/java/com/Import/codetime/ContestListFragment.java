package com.Import.codetime;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
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
import com.Import.codetime.model.ApiResponse;
import com.Import.codetime.model.Contest;
import com.Import.codetime.rest.RestApiClient;
import com.Import.codetime.utils.DbUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        String type = arguments.getString(EVENT_TYPE);
        assert type != null;

        setAPICredentials();
        switch (type) {
            case PAST_KEY:
                imageView.setImageResource(R.drawable.past_sand_clock);
                getContestsByType(DbUtils.TYPE_PAST_EVENTS);
                break;
            case ONGOING_KEY:
                imageView.setImageResource(R.drawable.present_sand_clock);
                getContestsByType(DbUtils.TYPE_ONGOING_EVENTS);
                break;
            case FUTURE_KEY:
                imageView.setImageResource(R.drawable.future_man_stop_clock);
                getContestsByType(DbUtils.TYPE_FUTURE_EVENTS);
                break;
        }

        Objects.requireNonNull(getActivity()).setTitle(type);
    }


    private void getFutureEvents() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
                + "T"
                + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String resourcesRegex = getFavouriteResourcesRegex();

        Call<ApiResponse> response = RestApiClient.getInstance().getFutureContests(date, resourcesRegex, "start");
        response.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, "empty body");
                } else {
                    Log.d(TAG, "contests size=" + response.body().getContests().size());
                    List<Contest> contestList = response.body().getContests();
                    contestEntryList = DbUtils.convertToContestEntryType(
                            contestList,
                            DbUtils.TYPE_FUTURE_EVENTS
                    );
                    addContestsToDatabase(contestEntryList);
                    setAdapter(contestEntryList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "failure");
            }
        });
    }

    private void getOnGoingEvents() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())
                + "T"
                + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String resourcesRegex = getFavouriteResourcesRegex();

        Call<ApiResponse> response = RestApiClient.getInstance().getOnGoingContests(date, date, resourcesRegex, "end");
        response.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, "empty body");
                } else {
                    Log.d(TAG, "contests size=" + response.body().getContests().size());
                    List<Contest> contestList = response.body().getContests();
                    contestEntryList = DbUtils.convertToContestEntryType(
                            contestList,
                            DbUtils.TYPE_ONGOING_EVENTS
                    );
                    addContestsToDatabase(contestEntryList);
                    setAdapter(contestEntryList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "failure");
            }
        });
    }

    private void getPastEvents() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()) + "T00:00:01";
        String resourcesRegex = getFavouriteResourcesRegex();

        Call<ApiResponse> response = RestApiClient.getInstance().getPastContests(date, resourcesRegex, "-end");
        response.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, "empty body");
                } else {
                    Log.d(TAG, "contests size=" + response.body().getContests().size());
                    List<Contest> contestList = response.body().getContests();
                    contestEntryList = DbUtils.convertToContestEntryType(
                            contestList,
                            DbUtils.TYPE_PAST_EVENTS
                    );
                    addContestsToDatabase(contestEntryList);
                    setAdapter(contestEntryList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "failure");
            }
        });
    }

    private void addContestsToDatabase(final List<ContestEntry> contestEntryList) {
        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.ContestDao().insertContests(contestEntryList);
            }
        });
    }

    private void getContestsByType(final int type) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final boolean prefChanged = sharedPreferences.getBoolean(MainActivity.prefChangedKey, false);

        MyDiskExecutor.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (prefChanged) {
                    mDb.ContestDao().deleteAllContests();   //clear table
                    sharedPreferences.edit().putBoolean(MainActivity.prefChangedKey, false).commit();  //  make it false again
                }
                final List<ContestEntry> list = mDb.ContestDao().getContestsByType(type);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() == 0) {  // if no data in db, so fetch it from internet
                            if (type == DbUtils.TYPE_PAST_EVENTS) {
                                getPastEvents();
                            } else if (type == DbUtils.TYPE_ONGOING_EVENTS) {
                                getOnGoingEvents();
                            } else
                                getFutureEvents();
                        } else {   // we have data in db, so fetch from here
                            setAdapter(list);
                        }
                    }
                });
            }
        });
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

    private String getFavouriteResourcesRegex() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Set<String> keySet = sharedPreferences.getAll().keySet();
        StringBuilder resourceRegex = new StringBuilder();
        for (String key : keySet) {
            if (sharedPreferences.getBoolean(key, true)) {
                resourceRegex.append(key).append("|");
            }
        }
        if (resourceRegex.length() > 0)
            resourceRegex.deleteCharAt(resourceRegex.length() - 1);   //delete last extra pipe symbol
        Log.d("Nitin", "resurce regex=" + String.valueOf(resourceRegex));

        return resourceRegex.toString();
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
        adapter = new EventListAdapter(list, mContext, ContestListFragment.this, clickListener);
        recyclerView.setAdapter(adapter);
    }

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
