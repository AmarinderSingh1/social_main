package com.wahhao.mfg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.R;
import com.wahhao.mfg.beans.Comments;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder>{

    List<Comments> names=new ArrayList<>();
    LayoutInflater inflater;
    Context con;
    int from;
    JSONObject langData;
    List<String> comentNames;

    public AdapterComments(Context context, List<Comments> names){
        con=context;
        inflater=LayoutInflater.from(context);
        this.names=names;
        this.from=from;
        langData=new JSONObject();
        comentNames=new ArrayList<>();
    }



    @NonNull
    @Override
    public AdapterComments.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        AdapterComments.MyViewHolder holder = new AdapterComments.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComments.MyViewHolder holder, final int position) {
        try {
            String name = names.get(position).getFull_name();
            String desc = names.get(position).getComment_text();
            holder.name.setText(name);
            holder.desc.setText(desc);
            if (names.get(position).getDocs().length > 0) {
                comentNames=new ArrayList<>();
                for (int i = 0; i < names.get(position).getDocs().length; i++) {
                    comentNames.add(names.get(position).getDocs()[i].getFile_name());
                }
                AdapterDisplayItem ada=new AdapterDisplayItem(con,comentNames);
                holder.rcImages.setLayoutManager(new LinearLayoutManager(con,LinearLayoutManager.HORIZONTAL,false));
                holder.rcImages.setAdapter(ada);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    public int getItemCount() {
        return names.size();
    }

    public List<Comments> getList(){
        return names;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,desc;
        RecyclerView rcImages;

        public MyViewHolder(View view){
            super(view);
            name=(TextView)view.findViewById(R.id.nameLabel);
            desc=(TextView)view.findViewById(R.id.desValue);
            rcImages=(RecyclerView)view.findViewById(R.id.rcCommentImg);
        }

    }
}

