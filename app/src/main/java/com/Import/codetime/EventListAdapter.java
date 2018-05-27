package com.Import.codetime;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private ArrayList<FakeEventData> eList;

    EventListAdapter(ArrayList<FakeEventData> a){
        this.eList = a;
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
        FakeEventData event = eList.get(position);
        holder.orgLogo.setImageResource(R.drawable.ic_launcher_background);
        holder.eventName.setText(event.eventName);
        holder.orgName.setText(event.orgName);
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView orgName,eventName;
        public ImageView orgLogo;
        public MyViewHolder(View itemView) {
            super(itemView);
            orgLogo = itemView.findViewById(R.id.image_logo);
            orgName = itemView.findViewById(R.id.text_org_name);
            eventName=itemView.findViewById(R.id.text_event_name);
        }
    }
}
