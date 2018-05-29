package com.Import.codetime;


import android.content.Context;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FrameLayout frameContainer;
    private ImageView image_background_blur;
    private Context mContext;


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

        mContext=getActivity();
        frameContainer=view.findViewById(R.id.frame_container);
        image_background_blur=view.findViewById(R.id.bg_blur_iv);
        recyclerView=view.findViewById(R.id.contest_list_rv);

        ArrayList<FakeEventData> eList = setUpFakeData();
        EventListAdapter adapter=new EventListAdapter(eList,mContext,ContestListFragment.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.requestDisallowInterceptTouchEvent(true);
        recyclerView.addOnItemTouchListener(listener);
    }

    //if we hold then drag our finger and then lift up....then dialog won't close
    //if we wan't this functionality then add this.
    RecyclerView.OnItemTouchListener listener=new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
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

    private ArrayList<FakeEventData> setUpFakeData() {
        ArrayList<FakeEventData> eList = new ArrayList<>();
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","Euler competition 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"codeShef","WeekCode 35"));
        eList.add(new FakeEventData(R.drawable.ic_launcher_background,"Hacker rank","nitin nitin nitin ntiitn nitin nitin nitin nitin ntiitn nitin nitin nitin nitin ntiitn nitin"));
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
