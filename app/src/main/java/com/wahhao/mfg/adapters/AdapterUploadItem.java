package com.wahhao.mfg.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.ActivityServiceRequest;
import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.FragmentProduct;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentOtherInformation;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterUploadItem  extends RecyclerView.Adapter<AdapterUploadItem.MyViewHolder> {


    List<String> names=new ArrayList<>();
    LayoutInflater inflater;
    Context con;
    int from;
    JSONObject langData;

    public AdapterUploadItem(Context context, List<String> names,int from){
        con=context;
        inflater=LayoutInflater.from(context);
        this.names=names;
        this.from=from;
        langData=new JSONObject();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_upload, parent, false);
        AdapterUploadItem.MyViewHolder holder = new AdapterUploadItem.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        try {
            String name = names.get(position);
            holder.name.setText(name);
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDelete(position);
                }
            });
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
        }catch (Exception e){
            e.printStackTrace();
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
        }

    }


    AlertDialog alt=null;

    public void alertDelete(final int position){
        try {
            String a="";

            langData= DataModel.
                    setLanguage(Prefs.with(con).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            con, "addProducts");

            a=langData.getString("labelDelDisclaimer");
            final AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setMessage(a);
            builder.setCancelable(false);
            a=langData.getString("labelYes");
            builder.setPositiveButton(a, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    names.remove(position);
                    if(FragmentProduct.delIds.size()>0){
                        FragmentProduct.delIds.remove(position);
                    }
                    if (from == 1) {
                        FragmentProduct.flyRecView.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false));
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        FragmentProduct.flyRecView.setAdapter(adapters);
                    } else if (from == 2) {
                        FragmentOtherInformation.horRecView.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false));
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        FragmentOtherInformation.horRecView.setAdapter(adapters);
                    } else if (from == 3) {
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        ActivityBusinessDetails.rcContract.setAdapter(adapters);
                    } else if (from == 4) {
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        ActivityBusinessDetails.rcSign.setAdapter(adapters);
                    } else if (from == 5) {
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        ActivityBusinessDetails.rcSeal.setAdapter(adapters);
                    }else if (from == 6) {
                        ActivityServiceRequest.rcAttachment.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false));
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        ActivityServiceRequest.rcAttachment.setAdapter(adapters);
                    }else if (from == 7) {
                        ActivityServiceRequest.rcAttachExtra.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false));
                        AdapterUploadItem adapters = new AdapterUploadItem(con, names, from);
                        ActivityServiceRequest.rcAttachExtra.setAdapter(adapters);
                    }
                }
            });
            a=langData.getString("labelCancel");
            builder.setNegativeButton(a, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alt.dismiss();
                }
            });
            alt = builder.show();
            alt.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(con.getResources().getColor(R.color.theme_color));
            alt.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(con.getResources().getColor(R.color.theme_color));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
