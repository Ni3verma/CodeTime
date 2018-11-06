package com.Import.codetime;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.Import.codetime.database.ContestEntry;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private List<ContestEntry> eList;
    private Context mContext;
    private Dialog dialog;
    private ContestListFragment mFragment;

    EventListAdapter(List<ContestEntry> a, Context context, ContestListFragment fragment) {
        this.eList = a;
        this.mContext=context;
        this.mFragment=fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contest_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ContestEntry event = eList.get(position);
        holder.orgLogo.setImageResource(R.drawable.ic_launcher_background);
        holder.eventName.setText(event.getName());
        holder.orgName.setText(event.getResourceName());

        holder.startDate = event.getStartDate();
        holder.endDate = event.getEndDate();
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orgName, eventName;
        ImageView orgLogo;
        String startDate, endDate;

        MyViewHolder(View itemView) {
            super(itemView);
            orgLogo = itemView.findViewById(R.id.image_logo);
            orgName = itemView.findViewById(R.id.text_org_name);
            eventName=itemView.findViewById(R.id.text_event_name);

            itemView.setOnTouchListener(touchListener);
        }

        View.OnTouchListener touchListener=new View.OnTouchListener() {
            Handler handler=new Handler();
            Runnable longPressed=new Runnable() {
                @Override
                public void run() {
                    View dialogLayout=LayoutInflater.from(mContext).inflate(R.layout.peek_dialog,null);
                    dialog = new Dialog(mContext);

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
                    Log.d("Nitin","ACTION UP");
                    mFragment.removeBlur();

                    hideDialog();
                    return false;
                }
                else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    handler.postDelayed(longPressed,250);
                    return true;
                }

                handler.removeCallbacks(longPressed);
                return false;
            }
        };

    }
    void hideDialog(){
        if (dialog != null)
            dialog.dismiss();
    }

}
