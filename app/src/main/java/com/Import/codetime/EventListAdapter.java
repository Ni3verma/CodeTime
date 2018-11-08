package com.Import.codetime;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.Import.codetime.database.ContestEntry;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private List<ContestEntry> eList;
    private Context mContext;
    private Dialog dialog;
    private ContestListFragment mFragment;
    private ContestListClickListener mListener;

    EventListAdapter(List<ContestEntry> a, Context context, ContestListFragment fragment, ContestListClickListener clickListener) {
        this.eList = a;
        this.mContext=context;
        this.mFragment=fragment;
        this.mListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contest_list_row,parent,false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContestEntry event = eList.get(position);
        holder.orgLogo.setImageResource(R.drawable.ic_launcher_background);
        holder.eventName.setText(event.getName());
        holder.orgName.setText(event.getResourceName());

        holder.startDate = event.getStartDate();
        holder.endDate = event.getEndDate();

        setAnimation(holder.itemView);
    }

    private void setAnimation(View view) {
        final long DURATION = 400;

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(DURATION);
        view.setAnimation(alphaAnimation);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(DURATION);
        view.setAnimation(scaleAnimation);

        view.animate();
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orgName, eventName;
        ImageView orgLogo;
        String startDate, endDate;
        private ContestListClickListener contestListClickListener;
        private boolean wasClicked = true;
        View.OnTouchListener touchListener=new View.OnTouchListener() {
            Handler handler=new Handler();
            Runnable longPressed=new Runnable() {
                @Override
                public void run() {
                    View dialogLayout=LayoutInflater.from(mContext).inflate(R.layout.peek_dialog,null);
                    dialog = new Dialog(mContext);
                    wasClicked = false;   //means that it was a long click and not a simple click

                    ImageView logo_dialog=dialogLayout.findViewById(R.id.logo);
                    TextView orgName_dialog=dialogLayout.findViewById(R.id.org_name);
                    TextView eventName_dialog=dialogLayout.findViewById(R.id.event_name);
                    TextView startDate_dialog = dialogLayout.findViewById(R.id.start_date);
                    TextView endDate_dialog = dialogLayout.findViewById(R.id.end_date);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(dialogLayout);
                    dialog.getWindow().setWindowAnimations(R.style.PeekAnimation);
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mFragment.removeBlur();
                        }
                    });

                    orgName_dialog.setText(orgName.getText());
                    eventName_dialog.setText(eventName.getText());
                    startDate_dialog.setText(startDate);
                    endDate_dialog.setText(endDate);

                    mFragment.blurBackground();

                    dialog.show();
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    handler.removeCallbacks(longPressed);
                    //Log.d("Nitin","ACTION UP");
                    mFragment.removeBlur();

                    hideDialog();
                    if (wasClicked) {
                        //Log.d("Nitin","it was a click");
                        contestListClickListener.onContestClick(eList.get(getAdapterPosition()).getId());
                    }
                    return false;
                }
                else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    wasClicked = true;    //potentially it may be a click
                    handler.postDelayed(longPressed,250);
                    return true;
                }

                handler.removeCallbacks(longPressed);
                return false;
            }
        };

        MyViewHolder(View itemView, ContestListClickListener clickListener) {
            super(itemView);
            orgLogo = itemView.findViewById(R.id.image_logo);
            orgName = itemView.findViewById(R.id.text_org_name);
            eventName = itemView.findViewById(R.id.text_event_name);
            contestListClickListener = clickListener;

            itemView.setOnTouchListener(touchListener); //info: click listener is implemented in here
        }
    }
    void hideDialog(){
        if (dialog != null)
            dialog.dismiss();
    }

}
