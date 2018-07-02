package com.wahhao.mfg.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.beans.SavedProducts;
import com.wahhao.mfg.fragments.FragmentProduct;
import com.wahhao.mfg.fragments.fragment_products.FragmentSavedProduct;
import com.wahhao.mfg.model.GetCategoryList;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class AdaptorSavedProduct extends RecyclerView.Adapter<AdaptorSavedProduct.ViewHolder>{
    Context context;
    List<SavedProducts> mData;
    JSONObject dataLanguage;
    int frag=0;
    public static List<String> name;
    public static List<String> ids;
    Prefs sp;
    String head,message;
    public static List<SavedProducts> selectedList;
    public AdaptorSavedProduct(Context context, List<SavedProducts> arrayList, JSONObject dataLanguage, int i){
        this.context = context;
        this.mData = arrayList;
        this.frag=i;
        this.dataLanguage=dataLanguage;
        name=new ArrayList<>();
        ids=new ArrayList<>();
        sp=new Prefs(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save_product,
                parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
        holder.txtProduct.setText(mData.get(position).getName());
        holder.txtWhin.setText(mData.get(position).getWhin());
        holder.txtCat.setText(mData.get(position).getCategory());
        holder.txtSku.setText(mData.get(position).getSku());
        String sub=mData.get(position).getSub_category();
        if(sub==null){
            holder.txtSubcat.setText("--");
        }else {
            holder.txtSubcat.setText(sub);
        }
        holder.isSelected.setChecked(mData.get(position).getIsSelected() > 0);
            holder.txtCatLabel.setText(dataLanguage.getString("labelProdCategory"));
            holder.txtSubCatLabel.setText(dataLanguage.getString("labelProdSubCategory"));
        if (holder.isSelected.getVisibility() == View.VISIBLE) {
            if(holder.isSelected.isChecked()){
                holder.isSelected.setButtonTintList(context.getColorStateList(R.color.theme_color));
            }else{
                holder.isSelected.setButtonTintList(context.getColorStateList(R.color.btnText));
            }
            holder.isSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(holder.isSelected.isChecked()){
                    UpdateClick(position,1);
                }else{
                    UpdateClick(position,0);
                }

                }
            });
        }

        if(holder.edit.getVisibility()==View.VISIBLE){
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClick(position);
                }
            });
        }

        if(holder.delete.getVisibility()==View.VISIBLE){
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                alert(position);
                }
            });

        }


        if(holder.disLayout.getVisibility()==View.VISIBLE){
            String a=dataLanguage.getString("labelDisapprovedText");
            holder.txtDisHead.setText(a);
            a=dataLanguage.getString("labelDisapprovedReason1");
            holder.txtDisPoint1.setText(a);
            a=dataLanguage.getString("labelDisapprovedReason2");
            holder.txtDisPoint2.setText(a);

        }

        switch (frag){
            case 1:
                holder.infoImg.setVisibility(View.GONE);
                holder.txtPreview.setVisibility(View.GONE);
                holder.txtStatus.setVisibility(View.GONE);
                holder.disLayout.setVisibility(View.GONE);
                holder.txtSku.setVisibility(View.GONE);
                holder.txtSkuLable.setVisibility(View.GONE);
                break;
            case 2:
                holder.isSelected.setVisibility(View.GONE);
                holder.infoImg.setVisibility(View.VISIBLE);
                holder.txtPreview.setVisibility(View.GONE);
                holder.disLayout.setVisibility(View.GONE);
                holder.txtStatus.setVisibility(View.VISIBLE);
                holder.delLayout.setVisibility(View.GONE);
                holder.txtSku.setVisibility(View.GONE);
                holder.txtSkuLable.setVisibility(View.GONE);
                break;
            case 3:
                holder.isSelected.setVisibility(View.GONE);
                holder.infoImg.setVisibility(View.GONE);
                holder.txtPreview.setVisibility(View.VISIBLE);
                holder.txtStatus.setVisibility(View.VISIBLE);
                holder.disLayout.setVisibility(View.GONE);
                holder.delLayout.setVisibility(View.GONE);
                holder.txtSku.setVisibility(View.VISIBLE);
                holder.txtSkuLable.setVisibility(View.VISIBLE);

                break;
            case 4:
                holder.isSelected.setVisibility(View.GONE);
                holder.infoImg.setVisibility(View.GONE);
                holder.txtStatus.setVisibility(View.GONE);
                holder.txtPreview.setVisibility(View.VISIBLE);
                holder.disLayout.setVisibility(View.VISIBLE);
                holder.delLayout.setVisibility(View.GONE);
                holder.txtSku.setVisibility(View.GONE);
                holder.txtSkuLable.setVisibility(View.GONE);

                break;
            case 5:
                holder.isSelected.setVisibility(View.GONE);
                holder.infoImg.setVisibility(View.GONE);
                holder.txtStatus.setVisibility(View.VISIBLE);
                holder.txtPreview.setVisibility(View.VISIBLE);
                holder.disLayout.setVisibility(View.GONE);
                holder.delLayout.setVisibility(View.GONE);
                holder.txtSku.setVisibility(View.VISIBLE);
                holder.txtSkuLable.setVisibility(View.VISIBLE);

                break;

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            switch (mData.get(position).getStage_key()) {
                case "marketing_analysis":
                    head =dataLanguage.getString("statusUnderMarketingAnalysis");
                    message=dataLanguage.getString("descUnderMarketingAnalysis");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_lightblue);
                    break;
                case "review":
                    head =dataLanguage.getString("statusInReview");
                    message=dataLanguage.getString("descInReview");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_yellow);
                    break;
                case "approved":
                    head =dataLanguage.getString("statusReceived");
                    message=dataLanguage.getString("descReceived");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_green);
                    break;
                case "financial_analysis":
                    head =dataLanguage.getString("statusFinancialAnalysis");
                    message=dataLanguage.getString("descFinancialAnalysis");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_orange);
                    break;
                case "pending_assets":
                    head =dataLanguage.getString("statusPendingAssets");
                    message=dataLanguage.getString("descPendingAssets");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_ablue);
                    break;
                case "pending_content":
                    head =dataLanguage.getString("statusPendingContent");
                    message=dataLanguage.getString("descPendingContent");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_brown);
                    break;
                case "pending_inventory":
                    head =dataLanguage.getString("statusPendingInventory");
                    message=dataLanguage.getString("descPendingInventory");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_purple);
                    break;
                case "final_approval":
                    head =dataLanguage.getString("labelApprovedStatus");
                    message=dataLanguage.getString("descApproved");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_green);
                    if(frag==3){
                        holder.txtStatus.setText("LIVE");
                        holder.statusLay.setBackgroundResource(R.drawable.rounded_darkgreen);
                    }
                    break;
                case "disapproved":
                    head =dataLanguage.getString("labelDisapprovedTitle");
                    message=dataLanguage.getString("descDisapproved");
                    holder.txtStatus.setText(head);
                    holder.statusLay.setBackgroundResource(R.drawable.rounded_red);
                    break;
                case "received":
                    head =dataLanguage.getString("statusInTransit");
                    message=dataLanguage.getString("descInTransit");
                    holder.txtStatus.setText(head);
                    if(mData.get(position).getCompleted_stage_key().equalsIgnoreCase("not_received")) {
                        holder.statusLay.setBackgroundResource(R.drawable.rounded_grey);
                    }
                    break;

            }
        }catch (Exception e){}
        if(holder.infoImg.getVisibility()==View.VISIBLE){
            holder.infoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String head="",message="";
                    try{
                        try {

                            switch (mData.get(position).getStage_key()) {
                                case "marketing_analysis":
                                    head =dataLanguage.getString("statusUnderMarketingAnalysis");
                                    message=dataLanguage.getString("descUnderMarketingAnalysis");
                                    break;
                                case "review":
                                    head =dataLanguage.getString("statusInReview");
                                    message=dataLanguage.getString("descInReview");
                                    break;
                                case "approved":
                                    head =dataLanguage.getString("statusReceived");
                                    message=dataLanguage.getString("descReceived");
                                    break;
                                case "financial_analysis":
                                    head =dataLanguage.getString("statusFinancialAnalysis");
                                    message=dataLanguage.getString("descFinancialAnalysis");
                                    break;
                                case "pending_assets":
                                    head =dataLanguage.getString("statusPendingAssets");
                                    message=dataLanguage.getString("descPendingAssets");
                                    break;
                                case "pending_content":
                                    head =dataLanguage.getString("statusPendingContent");
                                    message=dataLanguage.getString("descPendingContent");
                                    break;
                                case "pending_inventory":
                                    head =dataLanguage.getString("statusPendingInventory");
                                    message=dataLanguage.getString("descPendingInventory");
                                    break;
                                case "final_approval":
                                    head =dataLanguage.getString("labelApprovedStatus");
                                    message=dataLanguage.getString("descApproved");
                                    break;
                                case "disapproved":
                                    head =dataLanguage.getString("labelDisapprovedTitle");
                                    message=dataLanguage.getString("descDisapproved");
                                   break;
                                case "received":
                                    head =dataLanguage.getString("statusInTransit");
                                    message=dataLanguage.getString("descInTransit");
                                    break;

                            }
                        }catch (Exception e){}
                       alertSubmit(head,message);
                    }catch (Exception e){}
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout disLayout,delLayout,statusLay;
        CheckBox isSelected;
        ImageView infoImg,edit,delete;
        TextView txtProduct,txtCat,txtCatLabel,txtSubcat,txtSubCatLabel,txtWhin,txtStatus,txtPreview,txtDisHead,txtDisPoint1,txtDisPoint2,txtSkuLable,txtSku;

        public ViewHolder(View itemView) {
            super(itemView);
            disLayout=(LinearLayout)itemView.findViewById(R.id.disLayout);
            statusLay=(LinearLayout)itemView.findViewById(R.id.statusLay);
            delLayout=(LinearLayout)itemView.findViewById(R.id.delLayout);
            isSelected = (CheckBox)itemView.findViewById(R.id.checkSelected);
            txtProduct = (TextView)itemView.findViewById(R.id.txtName);
            txtCat = (TextView)itemView.findViewById(R.id.txtCatText);
            txtCatLabel = (TextView)itemView.findViewById(R.id.txtCatLabel);
            txtSubcat = (TextView)itemView.findViewById(R.id.txtSubCatText);
            txtSubCatLabel = (TextView)itemView.findViewById(R.id.txtsubCatLabel);
            txtWhin = (TextView)itemView.findViewById(R.id.txtWhin);
            txtStatus=(TextView)itemView.findViewById(R.id.txtStatus);
            txtPreview=(TextView)itemView.findViewById(R.id.txtPreview);
            infoImg=(ImageView)itemView.findViewById(R.id.imgInfo);
            edit=(ImageView)itemView.findViewById(R.id.edit);
            delete=(ImageView)itemView.findViewById(R.id.delete);
            txtDisHead=(TextView)itemView.findViewById(R.id.disHead);
            txtDisPoint1=(TextView)itemView.findViewById(R.id.disPoint1);
            txtDisPoint2=(TextView)itemView.findViewById(R.id.disPoint2);
            txtSkuLable=(TextView)itemView.findViewById(R.id.txtSkuLabel);
            txtSku=(TextView)itemView.findViewById(R.id.txtSkuText);

        }

    }


    public  void UpdateClick(int pos, int status){
        mData.get(pos).setIsSelected(status);

       selectedList=new ArrayList();
        for(int i =0;i<mData.size();i++){
            if(mData.get(i).getIsSelected()==1){
                selectedList.add(mData.get(i));
            }
        }

        try{
            if(selectedList.size()>0){
                FragmentSavedProduct.btnSendSample.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_red));
                FragmentSavedProduct.btnSendSample.setClickable(true);
                FragmentSavedProduct.btnSendSample.setEnabled(true);
            }else{
                FragmentSavedProduct.btnSendSample.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_grey));
                FragmentSavedProduct.btnSendSample.setClickable(false);
                FragmentSavedProduct.btnSendSample.setEnabled(false);
            }
            notifyDataSetChanged();
        }catch (Exception e){}
    }

    public void myClick(int position){
        try{
            ids=new ArrayList<>();
            name=new ArrayList<>();
            FragmentProduct.viewPager.setVisibility(View.GONE);
            FragmentProduct.tabLayout.setVisibility(View.GONE);
            FragmentProduct.relAddProduct.setVisibility(View.VISIBLE);
            MainActivity.backArrow.setVisibility(View.VISIBLE);
            JSONObject language_data=new JSONObject();
            language_data = DataModel.
                    setLanguage(Prefs.with(context).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            context, "addProducts");
            String a=language_data.getString("labelProductInfo");
            MainActivity.toolTitle.setText(a);
            a=language_data.getString("labelUpdateProduct");
            FragmentProduct.saveBtn.setText(a);
            DataModel.loading_box(context,true);
            final SavedProducts sap=SharedPrefNames.savedProductList.get(position);
            FragmentProduct.etProName.setText(sap.getName());
            FragmentProduct.etHsCode.setText(sap.getHs_code());
            FragmentProduct.etManCost.setText(sap.getManufacturing_cost());
            FragmentProduct.etSp.setText(sap.getCurrent_selling_price());
            FragmentProduct.etDSp.setText(sap.getDesired_selling_price());
            FragmentProduct.etCMar.setText(sap.getCurrent_margin_per_unit());
            FragmentProduct.etAdd.setText(sap.getDescription());
            if(sap.getWorksheet().length>0) {
                FragmentProduct.worksheetName.setText(sap.getWorksheet()[0].getFName());
            }
            FragmentProduct.delList=new ArrayList<>();
            FragmentProduct.delIds=new ArrayList<>();
            for(int i=0;i<sap.getFile().length;i++){
                FragmentProduct.delList.add(sap.getFile()[i].getFName());
                FragmentProduct.delIds.add(sap.getFile()[i].getResource_id());
            }
            FragmentProduct.fragCount=10;
            SharedPrefNames.PRODUCT_ID=mData.get(position).getProduct_id();
            FragmentProduct.adapters=new AdapterUploadItem(context,FragmentProduct.delList,1);
            FragmentProduct.flyRecView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
            FragmentProduct.flyRecView.setAdapter(FragmentProduct.adapters);
            String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
            RestClientWithoutString.getApiService(context).getProductCategory(header, new Callback<GetCategoryList>() {
                @Override
                public void success(GetCategoryList getCategoryList, Response response) {
                    name.add(0,"Choose your option");
                    ids.add(0,"choose");
                    for(int i=0;i<getCategoryList.getResponse().getCat().length;i++){
                        DataModel.loading_box_stop();
                        String na=getCategoryList.getResponse().getCat()[i].getName();
                        String id=getCategoryList.getResponse().getCat()[i].getParent_ids();
                        name.add(na);
                        ids.add(id);
                    }
                    SharedPrefNames.ids=ids;
                    FragmentProduct.adap = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, name);
                    FragmentProduct.adap.setDropDownViewResource(R.layout.spinner_item);
                    FragmentProduct.spin.setAdapter(FragmentProduct.adap);

                    String productId=sap.getCategory_id();
                    int index=0;
                    if(ids.contains(productId)){
                        for(int i=0;i<ids.size();i++){
                            if(ids.get(i).equalsIgnoreCase(productId)){
                                index=i;
                                break;
                            }
                        }
                    }
                    if(index!=0){
                        FragmentProduct.spin.setSelection(index);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    DataModel.loading_box_stop();
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    //DataModel.showSnackBarError(view,json);
                }
            });



        }catch (Exception e){}

    }

    public void alert(final int position)
    {
        try {

            LayoutInflater inflater= LayoutInflater.from(context);
            View alertLayout=inflater.inflate(R.layout.alert_delete,null);
            String message=dataLanguage.getString("labelDeleteSure");
            String cancel=dataLanguage.getString("labelCancelBtn");
            String ok=dataLanguage.getString("labelOkBtn");
            String head=dataLanguage.getString("labelDeleteProduts");
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

                    DataModel.loading_box(context, true);
                    String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                    String product_id=mData.get(position).getProduct_id();
                    RestClientWithoutString.getApiService(context).deleteProduct(product_id, header, new Callback<Submit>() {
                        @Override
                        public void success(Submit submit, Response response) {
                            try {
                                alert.dismiss();
                                mData.remove(position);
                                notifyDataSetChanged();
                                DataModel.loading_box_stop();
                                Log.e("Delete "," Product deleted");
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                        DataModel.loading_box_stop();
                        Log.e("error "," "+error.getMessage());
                        alert.dismiss();
                        }
                    });
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


    public void alertSubmit(String title,String message){
        try {
            LayoutInflater inflater= LayoutInflater.from(context);
            View alertLayout=inflater.inflate(R.layout.alert_submit_details,null);
            TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
            TextView tvMessage=alertLayout.findViewById(R.id.alertMessage);
            Button okBtn=alertLayout.findViewById(R.id.alertOkBtn);
            android.app.AlertDialog.Builder alt=new android.app.AlertDialog.Builder(context);
            alt.setView(alertLayout);
            alt.setCancelable(false);
            final android.app.AlertDialog alert=alt.show();
            alert.setCanceledOnTouchOutside(false);
            tvhead.setText(title);
            tvMessage.setText(message);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
