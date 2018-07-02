package com.wahhao.mfg.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.R;
import com.wahhao.mfg.beans.SampleBean;
import com.wahhao.mfg.beans.SavedProducts;
import com.wahhao.mfg.fragments.FragmentProduct;
import com.wahhao.mfg.fragments.fragment_products.FragmentSavedProduct;
import com.wahhao.mfg.model.Submit;
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

public class AdapterSendSample  extends RecyclerView.Adapter<AdapterSendSample.MyViewHolder> {

    public static List<SampleBean> sampleList;
    List<Integer> sum;
    List<Integer> mylist;
    public static int totQ=0,totW=0;
    public static List<SavedProducts> list;
    Context context;
    LayoutInflater inflater;
    JSONObject language_data;
    public AdapterSendSample(Context context, List<SavedProducts> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
        mylist=new ArrayList<>();
        language_data=new JSONObject();
        sampleList=new ArrayList<>();
        sum=new ArrayList<>();
        for(int i=0;i<getItemCount();i++){
            sum.add(i,0);
        }
        try{
            language_data= DataModel.
                    setLanguage(Prefs.with(context).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            context, "sendSamples");
        }
        catch (Exception e){}
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_send_sample, parent, false);
        AdapterSendSample.MyViewHolder holder = new AdapterSendSample.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        try {
            if(list.size()>0){
                FragmentProduct.continueBtn.setBackgroundResource(R.drawable.rounded_black);
                FragmentProduct.continueBtn.setEnabled(true);
            }

            final SampleBean sb=new SampleBean();
            sb.setUnitQuantity(holder.unit1.getText().toString());
            sb.setUnitWeight(holder.unit2.getText().toString());
            sb.setId(list.get(position).getProduct_id());
            sampleList.add(position,sb);
            String a = language_data.getString("labelUnits");
            holder.units1.setHint(a);
            a = language_data.getString("labelUnits");
            holder.units1.setHint(a);
            a = language_data.getString("labelQuantity");
            holder.quantity.setHint(a);
            a = language_data.getString("labelWeight");
            holder.weight.setHint(a);
            a = language_data.getString("labelShippingCompanyName");
            holder.compantName.setHint(a);
            a = language_data.getString("labelTrackingCode");
            holder.trackingCode.setHint(a);
            a = language_data.getString("labelMandatoryFields");
            holder.mandatory.setText(a);


            holder.proName.setText(list.get(position).getName());
            holder.proWhin.setText(list.get(position).getWhin());


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  alert(position);
                }
            });

            holder.etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                       if (!s.toString().equals("")) {
                           sum.set(position,0);
                            int l=0;
                            int c = Integer.parseInt(s.toString());
                            c=c+l;
                            int f = ((9 / 5) * c) ;
                           sum.set(position,f);
                            FragmentProduct.txtQuantityValue.setText(String.valueOf(f));

                       } else {
                            FragmentProduct.txtQuantityValue.setText("0");
                        }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(holder.etQuantity.getText().toString().length()>0) {
                        int i = Integer.parseInt(holder.etQuantity.getText().toString());
                        mylist.add(position,i);
                        sb.setQuantity(i+"");
                        sampleList.set(position,sb);
                        Log.e(" value ", "" + i);
                    }
                }
            });

            holder.etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(holder.etQuantity.getText().toString().length()>0) {
                        int i = Integer.parseInt(holder.etQuantity.getText().toString());
                        mylist.add(position,i);
                        sb.setQuantity(i+"");
                        sampleList.set(position,sb);
                        Log.e(" value ", "" + i);
                    }
                }
            });

            holder.etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(holder.etWeight.getText().toString().length()>0) {
                        int i = Integer.parseInt(holder.etWeight.getText().toString());
                        mylist.add(position,i);
                        sb.setWeight(i+"");
                        sampleList.set(position,sb);
                        Log.e(" value "+position, " space " + i);

                    }
                }
            });

            holder.etWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(holder.etWeight.getText().toString().length()>0) {
                        int i = Integer.parseInt(holder.etWeight.getText().toString());
                        mylist.add(position,i);
                        sb.setWeight(i+"");
                        sampleList.set(position,sb);
                        Log.e(" value "+position, " space " + i);

                    }
                }
            });

            holder.etCompanyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(holder.etCompanyName.getText().toString().length()>0) {
                        String i =holder.etCompanyName.getText().toString();
                        sb.setShippingName(i+"");
                        sampleList.set(position,sb);
                        Log.e(" value "+position, " space " + i);

                    }
                }
            });

            holder.etCompanyName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(holder.etCompanyName.getText().toString().length()>0) {
                        String i =holder.etCompanyName.getText().toString();
                        sb.setShippingName(i+"");
                        sampleList.set(position,sb);
                        Log.e(" value "+position, " space " + i);

                    }
                }
            });

            holder.etTrackingCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        if (holder.etTrackingCode.getText().toString().length() > 0) {
                            String i = holder.etTrackingCode.getText().toString();
                            sb.setTrackingCode(i + "");
                            Log.e(" value " + position, " space " + i);
                            sampleList.set(position, sb);
                        }
                    }
                    else{
                        if (holder.etTrackingCode.getText().toString().length() > 0) {
                            String i = holder.etTrackingCode.getText().toString();
                            sb.setTrackingCode(i + "");
                            Log.e(" value " + position, " space " + i);
                            sampleList.set(position, sb);
                        }
                    }
                }
            });

            holder.etTrackingCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (holder.etTrackingCode.getText().toString().length() > 0) {
                        String i = holder.etTrackingCode.getText().toString();
                        sb.setTrackingCode(i + "");
                        Log.e(" value " + position, " space " + i);
                        sampleList.set(position, sb);
                    }
                }
            });

            holder.etWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().equals("")) {
                            int c = Integer.parseInt(s.toString());
                            int f = ((9 / 5) * c) ;
                            f=FragmentProduct.weightSum+f;
                            FragmentProduct.txtWeightValue.setText(String.valueOf(f));
                        } else {
                            FragmentProduct.txtWeightValue.setText("0");
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextInputLayout units1,quantity,units2,weight,compantName,trackingCode;
        public EditText unit1,unit2,etQuantity,etWeight,etCompanyName,etTrackingCode;
        public TextView proName,proWhin,mandatory;
        public ImageView delete;

        public MyViewHolder(View view){
            super(view);
            units1=(TextInputLayout)view.findViewById(R.id.unitInput);
            units2=(TextInputLayout)view.findViewById(R.id.unitInput1);
            quantity=(TextInputLayout)view.findViewById(R.id.quantityInput);
            weight=(TextInputLayout)view.findViewById(R.id.weightInput);
            compantName=(TextInputLayout)view.findViewById(R.id.shipInput);
            trackingCode=(TextInputLayout)view.findViewById(R.id.tcodeInput);

            unit1=(EditText)view.findViewById(R.id.editUnit);
            unit2=(EditText)view.findViewById(R.id.editUnit1);
            etQuantity=(EditText)view.findViewById(R.id.editQuantity);
            etWeight=(EditText)view.findViewById(R.id.editWeight);
            etCompanyName=(EditText)view.findViewById(R.id.editShip);
            etTrackingCode=(EditText)view.findViewById(R.id.editTcode);

            proName=(TextView)view.findViewById(R.id.tvProName);
            proWhin=(TextView)view.findViewById(R.id.tvProWhin);
            mandatory=(TextView)view.findViewById(R.id.tvMandat);

            delete=(ImageView)view.findViewById(R.id.sampDel);

        }
    }


    public void alert(final int position)
    {
        try {

            LayoutInflater inflater= LayoutInflater.from(context);
            View alertLayout=inflater.inflate(R.layout.alert_delete,null);
            String message=language_data.getString("labeldeleteAlertText");
            String cancel=language_data.getString("labelCancelBtn");
            String ok=language_data.getString("labelOK");
            String head=language_data.getString("labeldeleteAlert");
            TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
            TextView tvMessage=alertLayout.findViewById(R.id.alertMessage);
            Button okBtn=alertLayout.findViewById(R.id.alertOkBtn);
            Button cancelBtn=alertLayout.findViewById(R.id.alertCancelBtn);

            android.app.AlertDialog.Builder alt=new android.app.AlertDialog.Builder(context);
            alt.setView(alertLayout);
            alt.setCancelable(false);
            final android.app.AlertDialog alert=alt.show();
            alert.setCanceledOnTouchOutside(false);
            tvhead.setText(head);
            tvMessage.setText(message);
            okBtn.setText(ok);
            cancelBtn.setText(cancel);

            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    if(list.size()==0){
                        AdaptorSavedProduct.selectedList.clear();
                        FragmentProduct.continueBtn.setBackgroundResource(R.drawable.rounded_grey);
                        FragmentProduct.continueBtn.setEnabled(false);
                        FragmentProduct.txtQuantityValue.setText("0");
                        FragmentProduct.txtWeightValue.setText("0");
                    }
                    else{
                        FragmentProduct.continueBtn.setBackgroundResource(R.drawable.rounded_black);
                        FragmentProduct.continueBtn.setEnabled(true);
                    }
                    notifyDataSetChanged();
                    alert.dismiss();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        }catch (Exception e){}
    }

}
