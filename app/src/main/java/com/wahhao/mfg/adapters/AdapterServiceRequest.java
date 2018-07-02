package com.wahhao.mfg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuyenmonkey.mkloader.model.Line;
import com.wahhao.mfg.ActivityServiceRequest;
import com.wahhao.mfg.R;
import com.wahhao.mfg.beans.Comments;
import com.wahhao.mfg.beans.Ticket;
import com.wahhao.mfg.model.GetSerReqList;
import com.wahhao.mfg.model.GetTicketFull;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AdapterServiceRequest extends RecyclerView.Adapter<AdapterServiceRequest.MyViewHolder> {

    public List<Ticket> mData;
    public static Ticket list;
    public Context con;
    public Prefs sp;
    public JSONObject langData;
    public List<Comments> comentList;

    public AdapterServiceRequest(Context context,List<Ticket> list,JSONObject lang){
        this.con=context;
        this.mData=list;
        sp=new Prefs(context);
        this.langData=lang;
        this.list=new Ticket();
        this.comentList=new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(con).inflate(R.layout.item_service_request, parent, false);
        AdapterServiceRequest.MyViewHolder holder = new AdapterServiceRequest.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try{
            holder.tvType.setText(langData.getString("labelRequestType")+":");
            holder.tvReq.setText(mData.get(position).getQuery_type_name());
            holder.tvTitle.setText(mData.get(position).getTicket_title());
            holder.tvMessage.setText(mData.get(position).getTicket_desc());
            String str=mData.get(position).getRequest_interval();
            holder.tvTime.setText(str+" "+langData.getString("labelAgo"));
            holder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        //DataModel.loading_box(con,true);
                        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                        SharedPrefNames.TICKET_ID=mData.get(position).getTicket_id();
                        RestClientWithoutString.getApiService(con).getTicket(mData.get(position).getTicket_id(), header, new Callback<GetTicketFull>() {
                            @Override
                            public void success(GetTicketFull getSerReqList, Response response) {
                            try {
                                list=getSerReqList.getResponse().getTickets();
                                if(list!=null) {
                                    List<String> names=new ArrayList<>();
                                    ActivityServiceRequest.tvSRNVal.setText(list.getTicket_code());
                                    ActivityServiceRequest.tvReqTypeVal.setText(list.getQuery_type_name());
                                    ActivityServiceRequest.tvSubjectVal.setText(list.getTicket_title());
                                    ActivityServiceRequest.tvReqDateVal.setText(list.getCreated_on());
                                    ActivityServiceRequest.tvRaisedVal.setText(list.getRequester_name());
                                    ActivityServiceRequest.tvCallNumVal.setText(list.getPhone());
                                    ActivityServiceRequest.tvReqStatVal.setText(list.getTicket_status());
                                    ActivityServiceRequest.tvDesVal.setText(list.getTicket_desc());
                                    if(list.getDocuments().length>0){
                                        for(int i=0;i<list.getDocuments().length;i++) {
                                            names.add(list.getDocuments()[i].getFile_name());
                                        }
                                        AdapterDisplayItem adap=new AdapterDisplayItem(con,names);
                                        ActivityServiceRequest.rcUploaded.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false));
                                        ActivityServiceRequest.rcUploaded.setAdapter(adap);
                                        ActivityServiceRequest.linUploadFiles.setVisibility(View.VISIBLE);
                                    }else{
                                        ActivityServiceRequest.linUploadFiles.setVisibility(View.GONE);
                                    }
                                    if(list.getComments().length>0){
                                        comentList=new ArrayList<>();
                                        SharedPrefNames.commentsList=comentList;
                                        for(int i=0;i<list.getComments().length;i++) {
                                            comentList.add(i,list.getComments()[i]);
                                        }
                                        AdapterComments newAdap=new AdapterComments(con,comentList);
                                        ActivityServiceRequest.rcComments.setLayoutManager(new LinearLayoutManager(con));
                                        ActivityServiceRequest.rcComments.setAdapter(newAdap);
                                        ActivityServiceRequest.rcComments.setNestedScrollingEnabled(false);
                                        ActivityServiceRequest.linComents.setVisibility(View.VISIBLE);
                                    }else{
                                        ActivityServiceRequest.linComents.setVisibility(View.GONE);
                                    }
                                    ActivityServiceRequest.back.setVisibility(View.VISIBLE);
                                    ActivityServiceRequest.relLayout.setVisibility(View.GONE);
                                    ActivityServiceRequest.commentLay.setVisibility(View.VISIBLE);
                                    String status=list.getTicket_status().toLowerCase();
                                    if(status.equalsIgnoreCase("resolved")){
                                        ActivityServiceRequest.linAddComment.setVisibility(View.GONE);
                                    }else{
                                        ActivityServiceRequest.linAddComment.setVisibility(View.VISIBLE);
                                    }
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                            Log.e("Heelo "," dekho");
                            }
                        });
                        }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvType,tvReq,tvTitle,tvMessage,tvTime;
        LinearLayout back;
        MyViewHolder(View view){
            super(view);
            back=(LinearLayout)view.findViewById(R.id.itemLay);
            tvType=(TextView)view.findViewById(R.id.textReqLabel);
            tvReq=(TextView)view.findViewById(R.id.textReqVal);
            tvTitle=(TextView)view.findViewById(R.id.textHeading);
            tvMessage=(TextView)view.findViewById(R.id.textData);
            tvTime=(TextView)view.findViewById(R.id.textTime);

        }
    }

}
