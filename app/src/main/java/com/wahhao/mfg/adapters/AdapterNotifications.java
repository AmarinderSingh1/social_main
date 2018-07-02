package com.wahhao.mfg.adapters;

import android.content.Context;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.MyViewHolder> {

    LayoutInflater inflater;
    List<String> headings=new ArrayList<>();
    List<String> values=new ArrayList<>();
    String heading="",data="";

    public AdapterNotifications(Context context,List<String> headings,List<String> values){
    inflater=LayoutInflater.from(context);
    this.headings=headings;
    this.values=values;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        AdapterNotifications.MyViewHolder holder = new AdapterNotifications.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        heading=headings.get(position);
        data=values.get(position);
        holder.textHeading.setText(heading);
        holder.textData.setText(data);
        if(heading.contains("Money"))
        {
            holder.imgIcon.setImageResource(R.drawable.wallet_icon);
        }else if(heading.contains("Approved"))
        {
            holder.imgIcon.setImageResource(R.drawable.approved_icon);
        }else if(heading.contains("Out")||heading.contains("Stock"))
        {
            holder.imgIcon.setImageResource(R.drawable.outofstock_icon);
        }

    }

    @Override
    public int getItemCount() {
        return headings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textHeading,textData;
        ImageView imgIcon;
        MyViewHolder(View itemView){
            super(itemView);
            textHeading=(TextView)itemView.findViewById(R.id.textHeadings);
            textData=(TextView)itemView.findViewById(R.id.textData);
            imgIcon=(ImageView)itemView.findViewById(R.id.imgIcon);
        }
    }
}
