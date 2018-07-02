package com.wahhao.mfg.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wahhao.mfg.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.MyViewHolder>{

    private LayoutInflater inflater;
    private Context context;
    List<String> namesList=new ArrayList<>();
    List<Integer> valuesList=new ArrayList<>();
    String name="";
    int value=0;

    public AdapterOrders(Context context,List<String> namesList,List<Integer> valuesList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.namesList=namesList;
        this.valuesList=valuesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_orders, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        name=namesList.get(position);
        value=valuesList.get(position);
        switch (position){
             case 0:
                holder.clayout.setBackgroundColor(context.getResources().getColor(R.color.os_1));
                holder.imgV.setImageResource(R.drawable.orders_one);
                break;
            case 1:
                holder.clayout.setBackgroundColor(context.getResources().getColor(R.color.os_2));
                holder.imgV.setImageResource(R.drawable.orders_two);
                break;
            case 2:
                holder.clayout.setBackgroundColor(context.getResources().getColor(R.color.os_3));
                holder.imgV.setImageResource(R.drawable.orders_three);
                holder.rLayout.setBackgroundResource(R.drawable.rounded_white);
                break;
            case 3:
                holder.clayout.setBackgroundColor(context.getResources().getColor(R.color.os_4));
                holder.imgV.setImageResource(R.drawable.orders_four);
                break;
        }
        holder.text1.setText(value+"");
        holder.text2.setText(name);
        holder.rLayout.setBackgroundResource(R.drawable.rounded_white);
        holder.rLayout.setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView text1,text2;
        ImageView imgV;
        LinearLayout clayout;
        RelativeLayout rLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            clayout=(LinearLayout)itemView.findViewById(R.id.background);
            rLayout=(RelativeLayout)itemView.findViewById(R.id.back);
            text1=(TextView)itemView.findViewById(R.id.textOne);
            text2=(TextView)itemView.findViewById(R.id.textTwo);
            imgV=(ImageView)itemView.findViewById(R.id.iv);
        }
    }
}