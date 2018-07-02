package com.wahhao.mfg.fragments.fragment_business_details;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.SpinnerAdapter;
import com.wahhao.mfg.beans.ItemData;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class FragmentAgentDetails  extends BaseFragment {

    JSONObject language_data;
    View view;
    TextView heading,code;
    TextInputLayout inputAgentName,inputAgentPhone;
    public static EditText etagentName,etagentPhone;
    Button continueBtn,skipBtn;
    String agentName="",agentCCode="",agentPhone="",header="",check="";
    Prefs sp;
    Spinner coCode;
    String[] codes={"China (+86)","India (+91)"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agent_details, container, false);
        init(view);
        return view;
    }

    void init(final View view){
        sp=new Prefs(getActivity());
        heading=(TextView)view.findViewById(R.id.textAgentHead);
        inputAgentName=(TextInputLayout)view.findViewById(R.id.nameInput);
        inputAgentPhone=(TextInputLayout)view.findViewById(R.id.phoneInput);
        etagentName=(EditText)view.findViewById(R.id.editAgentName);
        etagentPhone=(EditText)view.findViewById(R.id.editAgentPhone);
        continueBtn=(Button)view.findViewById(R.id.continueBtn);
        skipBtn=(Button)view.findViewById(R.id.skipBtn);
        code=(TextView)view.findViewById(R.id.textCoCode);
        coCode=(Spinner)view.findViewById(R.id.spinCtCode);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                   imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                   agentName = "";
                   agentPhone = "";
                   //agentCCode = etcountryCode.getText().toString();
                   JSONObject ob=new JSONObject();
                       ob.put("reg_agent", agentName);
                       ob.put("reg_agent_phone", agentPhone);
                       ob.put("reg_agent_phone_code", agentCCode);

                   if (DataModel.Internetcheck(getActivity())) {
                       DataModel.loading_box(getActivity(), true);
                       header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                       RestClientWithoutString.getApiService(getActivity()).userAgentDetails(header, new TypedString(ob.toString()), new Callback<Submit>() {
                           @Override
                           public void success(Submit submit, Response response) {
                               DataModel.loading_box_stop();
                               ActivityBusinessDetails.viewPager.setCurrentItem(2);
                           }

                           @Override
                           public void failure(RetrofitError error) {
                               DataModel.loading_box_stop();
                               String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                               DataModel.showSnackBarError(view,json);
                           }
                       });

                   } else {
                       String a=language_data.getString("InternetMessage");
                       DataModel.showSnackBar(view,a);
                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             try {
                 String a="";
                 agentName = etagentName.getText().toString();
                 //agnetCCode = etcountryCode.getText().toString();
                 agentPhone = etagentPhone.getText().toString();
                 InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                 if (agentName.length() == 0 && agentPhone.length() == 0 || agentPhone.length() == SharedPrefNames.PHONE_LENGTH&& agentName.length() > 0) {
                     if (DataModel.Internetcheck(getActivity())) {
                         JSONObject ob=new JSONObject();
                         ob.put("reg_agent", agentName);
                         ob.put("reg_agent_phone", agentPhone);
                         ob.put("reg_agent_phone_code", agentCCode);
                         DataModel.loading_box(getActivity(), true);
                         header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                         RestClientWithoutString.getApiService(getActivity()).userAgentDetails(header,new TypedString(ob.toString()) , new Callback<Submit>() {
                             @Override
                             public void success(Submit submit, Response response) {
                                 DataModel.loading_box_stop();
                                 ActivityBusinessDetails.viewPager.setCurrentItem(2);
                             }
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
                 }  else if (agentName.contains("<") || agentName.contains(">")) {
                     a=language_data.getString("labelErrorAgentName");
                     DataModel.showSnackBar(view,a);
                 }else if (agentPhone.length() < 11) {
                     a=language_data.getString("labelErrorPhoneNum");
                     DataModel.showSnackBar(view,a);
                 }
             }catch (Exception e){}

            }
        });
        loadData();
        ArrayList<ItemData> list=new ArrayList<>();
        list.add(new ItemData("+86",R.drawable.china_flag));
        list.add(new ItemData("+91",R.drawable.india_flag));
        SpinnerAdapter adapter=new SpinnerAdapter(getContext(), R.layout.spinner_img_item,R.id.txt,list);

        coCode.setAdapter(adapter);
        coCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    switch (position) {
                        case 0:
                            agentCCode = "86";
                            SharedPrefNames.PHONE_LENGTH=11;
                            InputFilter[] FilterArray = new InputFilter[1];
                            FilterArray[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                            etagentPhone.setFilters(FilterArray);
                            check = etagentPhone.getText().toString();
                            if (check.length() != SharedPrefNames.PHONE_LENGTH && check.length() != 0) {
                                String a = language_data.getString("labelPhoneNumberError");
                                DataModel.showSnackBar(view, a);
                            }
                            break;
                        case 1:
                            agentCCode = "91";
                            SharedPrefNames.PHONE_LENGTH=10;
                            InputFilter[] FilterArray1 = new InputFilter[1];
                            FilterArray1[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                            etagentPhone.setFilters(FilterArray1);
                            check = etagentPhone.getText().toString();
                            if (check.length() != SharedPrefNames.PHONE_LENGTH && check.length() != 0) {
                                String a = language_data.getString("labelPhoneNumberError");
                                DataModel.showSnackBar(view, a);
                            }
                            break;
                    }
                }catch (Exception e){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void loadData()
    {
        try {
            language_data=new JSONObject();
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "onboardBusinessDetails");
            codes=new String[]{language_data.getString("chinaCode"),
                    language_data.getString("indiaCode")};
            String a=language_data.getString("labelAgentRegHeading");
            heading.setText(a);
            a=language_data.getString("labelAgentName");
            inputAgentName.setHint(a);
            a=language_data.getString("labelPhoneNum");
            inputAgentPhone.setHint(a);
            a=language_data.getString("labelCountryCode");
            code.setText(a);
            a=language_data.getString("labelContinueBtn");
            continueBtn.setText(a);

        }catch (Exception e){

        }
    }

}
