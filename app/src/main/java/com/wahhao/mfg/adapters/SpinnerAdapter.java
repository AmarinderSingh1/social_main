package com.wahhao.mfg.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.R;
import com.wahhao.mfg.beans.ItemData;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<ItemData> {
    int groupid;
    Context context;
    ArrayList<ItemData> list;
    LayoutInflater inflater;
    public SpinnerAdapter(Context context, int groupid, int id, ArrayList<ItemData>
            list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
        this.context=context;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        textView.setTextColor(context.getResources().getColor(R.color.theme_color));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);


        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        textView.setTextColor(context.getResources().getColor(R.color.btnText));
        return itemView;

    }
}
