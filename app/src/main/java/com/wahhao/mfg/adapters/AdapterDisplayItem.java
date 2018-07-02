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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterDisplayItem  extends RecyclerView.Adapter<AdapterDisplayItem.MyViewHolder>{

    List<String> names=new ArrayList<>();
    LayoutInflater inflater;
    Context con;
    int from;
    JSONObject langData;

    public AdapterDisplayItem(Context context, List<String> names){
        con=context;
        inflater=LayoutInflater.from(context);
        this.names=names;
        this.from=from;
        langData=new JSONObject();
    }



    @NonNull
    @Override
    public AdapterDisplayItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_upload, parent, false);
        AdapterDisplayItem.MyViewHolder holder = new AdapterDisplayItem.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDisplayItem.MyViewHolder holder, final int position) {
        String name=names.get(position);
        holder.name.setText(name);
        if (name.length() > 0) {
            String extentions = name.substring(name.length() - 3, name.length());
            extentions = extentions.toLowerCase();
            switch (extentions) {
                case "jpg":
                    holder.img.setImageResource(R.drawable.icon_img);
                    break;
                case "png":
                    holder.img.setImageResource(R.drawable.icon_img);
                    break;
                case "pdf":
                    holder.img.setImageResource(R.drawable.icon_pdf);
                    break;
                case "doc":
                    holder.img.setImageResource(R.drawable.icon_doc);
                    break;
                case "xls":
                    holder.img.setImageResource(R.drawable.icon_xls);
                    break;
                case "ocx":
                    holder.img.setImageResource(R.drawable.icon_img);
                    break;
                case "lsx":
                    holder.img.setImageResource(R.drawable.icon_xls);
                    break;
                case "peg":
                    holder.img.setImageResource(R.drawable.icon_img);
                    break;
                case "ppt":
                    holder.img.setImageResource(R.drawable.icon_ppt);
                    break;
            }
        }

    }
    @Override
    public int getItemCount() {
        return names.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView cancel,img;
        TextView name;

        public MyViewHolder(View view){
            super(view);
            img=(ImageView)view.findViewById(R.id.upl);
            cancel=(ImageView)view.findViewById(R.id.cancel);
            name=(TextView)view.findViewById(R.id.fileName);
            cancel.setVisibility(View.GONE);
        }

    }
    }
