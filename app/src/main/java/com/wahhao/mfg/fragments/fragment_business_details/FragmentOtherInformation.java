package com.wahhao.mfg.fragments.fragment_business_details;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterUploadItem;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.fragments.FragmentOnboarding;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;
import com.wahhao.mfg.utils.SingleUploadBroadcastReceiver;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class FragmentOtherInformation extends BaseFragment  {

    JSONObject language_data;
    View view;
    TextInputLayout inputAMC,inputAT,inputAES,inputOSAP,inputExp;
    public static EditText etAMC,etAT,etAES,etOSAP,etExp;
    Button approveBtn;
    TextView tvuploadDoc,tvwarning,tvForamt;
    String amc,at,aes,osap,exp;
    public static String upl="";
    String access;
    public static ImageView upload;
    Prefs sp;
    public static RecyclerView horRecView;
    public static AdapterUploadItem adapters;

    public static List<String> list;
    public static List<Integer> ids;

/*
* spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, CountryName));
*
* */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other_info, container, false);
        init(view);
        return view;
    }



    void init(final View view){
        sp=new Prefs(getActivity());
        list=new ArrayList<>();
        ids=new ArrayList<>();
        inputAMC=(TextInputLayout)view.findViewById(R.id.amcInput);
        inputAT=(TextInputLayout)view.findViewById(R.id.atInput);
        inputAES=(TextInputLayout)view.findViewById(R.id.aesInput);
        inputOSAP=(TextInputLayout)view.findViewById(R.id.osapInput);
        inputExp=(TextInputLayout)view.findViewById(R.id.expInput);
        etAMC=(EditText)view.findViewById(R.id.editAMC);
        etAT=(EditText)view.findViewById(R.id.editAT);
        etAES=(EditText)view.findViewById(R.id.editAES);
        etOSAP=(EditText)view.findViewById(R.id.editOSAP);
        etExp=(EditText)view.findViewById(R.id.editExp);
        approveBtn=(Button)view.findViewById(R.id.approveBtn);
        tvuploadDoc=(TextView)view.findViewById(R.id.textUplaodDoc);
        tvwarning=(TextView)view.findViewById(R.id.textWarning);
        tvForamt=(TextView)view.findViewById(R.id.textFormat);
        upload=(ImageView)view.findViewById(R.id.upload);
        horRecView=(RecyclerView)view.findViewById(R.id.horRexView);
        /*list.add("abc");
        list.add("abc");
        list.add("abc");*/
        horRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        adapters=new AdapterUploadItem(getActivity(),list,2);
        horRecView.setAdapter(adapters);

        if(ActivityBusinessDetails.amc!=null){
            if(ActivityBusinessDetails.amc.length()>0)
            {
                etAMC.setText(ActivityBusinessDetails.amc);
                etAES.setText(ActivityBusinessDetails.aes);
                etAT.setText(ActivityBusinessDetails.at);
                etExp.setText(ActivityBusinessDetails.exp);
                etOSAP.setText(ActivityBusinessDetails.osap);
            }
        }



        language_data=new JSONObject();
        loadData();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            upl="";
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else {
                    intent.setType("*/*");
                    getActivity().startActivityForResult(intent, 1);
                }
            }
        });
        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                String a="";
                amc = etAMC.getText().toString();
                at = etAT.getText().toString();
                aes = etAES.getText().toString();
                osap = etOSAP.getText().toString();
                exp = etExp.getText().toString();

                if (amc.length() == 0) {
                    a=language_data.getString("labelErrorCapacity");
                    DataModel.showSnackBar(view,a);
                } else if (at.length() == 0) {
                    a=language_data.getString("labelErrorTurnover");
                    DataModel.showSnackBar(view,a);

                } else if (aes.length() == 0) {
                    a=language_data.getString("labelErrorExportSale");
                    DataModel.showSnackBar(view,a);
                } /*else if (osap.length() == 0) {
                    a=language_data.getString("labelErrorOtherInfo");
                    DataModel.showSnackBar(view,a);
                }*/ else if (exp.length() == 0) {
                    a=language_data.getString("labelErrorExperience");
                    DataModel.showSnackBar(view,a);
                }else if (horRecView.getAdapter().getItemCount() == 0) {
                    a=language_data.getString("labelErrorDocuments");
                    DataModel.showSnackBar(view,a);
                }else if(adapters.getItemCount()==0) {
                    a=language_data.getString("labelResourcesError");
                    DataModel.showSnackBar(view,a);
                } else if (DataModel.Internetcheck(getActivity())) {
                    DataModel.loading_box(getActivity(), true);
                    access = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                    JSONObject ob=new JSONObject();
                    ob.put("man_capacity", amc);
                    ob.put("annual_turnover", at);
                    ob.put("exports", aes);
                    ob.put("exp_in_us", exp);
                    ob.put("other_info", osap);
                    JSONArray obj=new JSONArray();
                    for(int i=0;i<ids.size();i++) {
                        JSONObject oj2 = new JSONObject();
                        oj2.put("id", ids.get(i));
                        oj2.put("name", "document");
                        obj.put(oj2);
                    }
                    ob.put("resource",obj);

                    RestClientWithoutString.getApiService(getActivity()).userOtherInfo(access,new TypedString(ob.toString()), new Callback<Submit>() {
                        @Override
                        public void success(Submit submit, Response response) {
                            DataModel.loading_box_stop();
                            sp.save(SharedPrefNames.COMP_STAGE_KEY,"other_info");
                            FragmentOnboarding.r1.setBackgroundResource(R.drawable.roundgreen);
                            FragmentOnboarding.l1.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
                            FragmentOnboarding.r2.setBackgroundResource(R.drawable.roundyellow);
                            FragmentOnboarding.l2.setBackgroundColor(getResources().getColor(R.color.onBoardYellow));
                            FragmentOnboarding.l3.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
                            FragmentOnboarding.l4.setBackgroundColor(getResources().getColor(R.color.onBoardYellow));
                            FragmentOnboarding.img1.setImageResource(R.drawable.number1_grenn);
                            FragmentOnboarding.img2.setImageResource(R.drawable.number2yellow);
                            FragmentOnboarding.businessDetails.setClickable(false);
                            FragmentOnboarding.signContract.setClickable(true);
                            FragmentOnboarding.imv1.setVisibility(View.GONE);
                            alertSubmit();
                        }
                        @SuppressLint("NewApi")
                        @Override
                        public void failure(RetrofitError error) {
                        DataModel.loading_box_stop();
                            String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                            DataModel.showSnackBarError(view,json);
                        }
                    });

                } else {
                    a=language_data.getString("InternetMessage");
                    DataModel.showSnackBar(view,a);
                }
            } catch (Exception e){
                e.printStackTrace();

            }
            }
        });
    }

    void loadData(){
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "onboardBusinessDetails");
            String a=language_data.getString("labelAnnualCapacity");
            inputAMC.setHint(a);
            a=language_data.getString("labelAnnualTurnover");
            inputAT.setHint(a);
            a=language_data.getString("labelAnnualSales");
            inputAES.setHint(a);
            a=language_data.getString("labelExperience");
            inputExp.setHint(a);
            a=language_data.getString("labelOnlineStores");
            inputOSAP.setHint(a);
            a=language_data.getString("labelUpploaDocumets");
            tvuploadDoc.setText(a);
            a=language_data.getString("labelBusinessCheck");
            tvwarning.setText(a);
            a=language_data.getString("labelSubmitBtn");
            approveBtn.setText(a);
            a=language_data.getString("lableAllowed");
            tvForamt.setText(a+tvForamt.getText().toString());
            //lableAllowed exp.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,arr));


        }catch (Exception e){

        }
        }

    public void alertSubmit(){
            try {
                LayoutInflater inflater= getLayoutInflater();
                View alertLayout=inflater.inflate(R.layout.alert_submit_details,null);
                TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
                TextView tvMessage=alertLayout.findViewById(R.id.alertMessage);
                Button okBtn=alertLayout.findViewById(R.id.alertOkBtn);
                AlertDialog.Builder alt=new AlertDialog.Builder(getActivity());
                alt.setView(alertLayout);
                alt.setCancelable(false);
                final AlertDialog alert=alt.show();
                alert.setCanceledOnTouchOutside(false);
                String a=language_data.getString("labelBusinessModalHeading");
                tvhead.setText(a);
                a=language_data.getString("labelBusinessModalText");
                tvMessage.setText(a);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

}