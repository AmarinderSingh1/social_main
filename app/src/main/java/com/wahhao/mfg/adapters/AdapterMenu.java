package com.wahhao.mfg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.R;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MyViewHolder>{
    private LayoutInflater inflater;
    private Context context;
    private List<String> headings;
    public AdapterMenu(Context context, List<String> headings){
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.headings=headings;
    }

    @NonNull
    @Override
    public AdapterMenu.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_menu, parent, false);
        AdapterMenu.MyViewHolder holder = new AdapterMenu.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMenu.MyViewHolder holder, int position) {
        String heading=headings.get(position);
        holder.tvHeading.setText(heading);
        switch (position){
            case 0:
                holder.imgV1.setImageResource(R.drawable.menu_1);
                break;
            case 1:
                holder.imgV1.setImageResource(R.drawable.menu_2);
                break;
            case 2:
                holder.imgV1.setImageResource(R.drawable.menu_3);
                break;
            case 3:
                holder.imgV1.setImageResource(R.drawable.menu_4);
                break;
            case 4:
                holder.imgV1.setImageResource(R.drawable.menu_5);
                break;
            case 5:
                holder.imgV1.setImageResource(R.drawable.menu_6);
                break;
            case 6:
                holder.imgV1.setImageResource(R.drawable.menu_7);
                break;
            case 7:
                holder.imgV1.setImageResource(R.drawable.menu_8);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgV1;
        TextView tvHeading;


        public MyViewHolder(View itemView) {
            super(itemView);
            imgV1=(ImageView)itemView.findViewById(R.id.imgViewIcon);
            tvHeading=(TextView)itemView.findViewById(R.id.textMenu);
        }
    }

}

