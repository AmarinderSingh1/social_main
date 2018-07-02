package com.wahhao.mfg.fragments.fragment_business_details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.fragments.FragmentProduct;
import com.wahhao.mfg.model.GetCity;
import com.wahhao.mfg.model.GetDistrict;
import com.wahhao.mfg.model.GetProvince;
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
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;


public class FragmentComnapyInfo extends BaseFragment {

    JSONObject language_data;
    View view;
    TextInputLayout inputCName,inputState,inputStNum,inputPCode,inputBRN;
    public static EditText etName,etStNum,etPCode,etBRN;
    Button continueBtn;
    public Spinner province,district,city;
    TextView tvProvince,tvDistrict,tvStreet;
    public static ArrayAdapter<String> adapPro,adapDis,adapCity;
    String cName,cStreet,cPostalCode,cBRN,cProvince,cDistrict,cCity,access,header;
    int provId=0,disId=0,cityId=0;
    Prefs sp;
    List<String> dist;
    List<String> cities;
    int [] distId,citId;
    CoordinatorLayout clayout;
    public static int change=0;
    private static final String[] ITEMS = {"Choose your option"};
    public static List<String> provinces;
    public static int[] provincesId;
    public static int pos=0,myId=0;
    int Pid=0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_com_info, container, false);
        init(view);
        return view;
    }

    void init(final View view){
        sp=new Prefs(getActivity());
        pos=0;
        clayout=(CoordinatorLayout)view.findViewById(R.id.coFragInfo);
        adapPro = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, ITEMS);
        adapPro.setDropDownViewResource(R.layout.spinner_item);
        adapDis= new ArrayAdapter<>(getActivity(), R.layout.spinner_item, ITEMS);
        adapCity= new ArrayAdapter<>(getActivity(), R.layout.spinner_item, ITEMS);
        province=view.findViewById(R.id.spinner1);
        district=view.findViewById(R.id.spinner2);
        city=view.findViewById(R.id.spinner3);

        province.setAdapter(adapPro);
        district.setAdapter(adapDis);
        city.setAdapter(adapCity);
        tvProvince=(TextView)view.findViewById(R.id.textProvince);
        tvDistrict=(TextView)view.findViewById(R.id.textDistrict);
        tvStreet=(TextView)view.findViewById(R.id.textCity);

        inputCName=(TextInputLayout)view.findViewById(R.id.nameInput);
        inputStNum=(TextInputLayout)view.findViewById(R.id.snumInput);
        inputPCode=(TextInputLayout)view.findViewById(R.id.pcodeInput);
        inputBRN=(TextInputLayout)view.findViewById(R.id.brnumInput);
        etName=(EditText)view.findViewById(R.id.editComName);
        etStNum=(EditText)view.findViewById(R.id.editStNum);
        etPCode=(EditText)view.findViewById(R.id.editPostalCode);
        etBRN=(EditText)view.findViewById(R.id.editBRNum);
        continueBtn=(Button)view.findViewById(R.id.conBtn);
        language_data=new JSONObject();
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           try{
               String a="";
                cName=etName.getText().toString();
                if(province.getSelectedItemPosition()!=0) {
                    provId = provincesId[province.getSelectedItemPosition() - 1];
                }else{
                    provId=0;
                }
               if(district.getSelectedItemPosition()!=0) {
                   disId = distId[district.getSelectedItemPosition() - 1];
               }else{
                    disId=0;
               }
               if(city.getSelectedItemPosition()!=0) {
                   cityId = citId[city.getSelectedItemPosition() - 1];
               }else{
                    cityId=0;
               }
                cStreet=etStNum.getText().toString();
                cPostalCode=etPCode.getText().toString();
                cBRN=etBRN.getText().toString();
                cProvince=province.getSelectedItem().toString();
                cDistrict=district.getSelectedItem().toString();
                cCity=city.getSelectedItem().toString();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(clayout.getWindowToken(), 0);
                if(cName.length()==0){
                    a=language_data.getString("labelErrorCompanyName");
                    DataModel.showSnackBar(view,a);
                }else if(cProvince.equalsIgnoreCase("Choose your option")){
                    a=language_data.getString("labelErrorProvince");
                    DataModel.showSnackBar(view,a);
                }else if(cDistrict.equalsIgnoreCase("Choose your option")){
                    a=language_data.getString("labelErrorDistrict");
                    DataModel.showSnackBar(view,a);
                }else if(cCity.equalsIgnoreCase("Choose your option")){
                    a=language_data.getString("labelErrorCity");
                    DataModel.showSnackBar(view,a);
                }else if(cStreet.equalsIgnoreCase("")){
                    a=language_data.getString("labelErrorStreetNum");
                    DataModel.showSnackBar(view,a);
                }else if(cPostalCode.equalsIgnoreCase("Choose your option")){
                    a=language_data.getString("labelErrorPostalCode");
                    DataModel.showSnackBar(view,a);
                }else if(cPostalCode.length()<6){
                    a=language_data.getString("labelErrorPostalCode1");
                    DataModel.showSnackBar(view,a);
                }else if(cBRN.equalsIgnoreCase("")){
                    a=language_data.getString("labelErrorBusinessRegNum");
                    DataModel.showSnackBar(view,a);
                }else if(DataModel.Internetcheck(getActivity())){
                    //Api Code
                    JSONObject ob=new JSONObject();
                    try{
                        ob.put("c_name", cName);
                        ob.put("c_province", provId);
                        ob.put("c_city", cityId);
                        ob.put("c_district", disId);
                        ob.put("c_street", cStreet);
                        ob.put("c_postal_code", cPostalCode);
                        ob.put("b_reg_number", cBRN);

                    DataModel.loading_box(getActivity(),true);
                    access=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                    RestClientWithoutString.getApiService(getActivity()).userCompInfo(access,new TypedString(ob.toString()), new Callback<Submit>() {
                        @Override
                        public void success(Submit submit, Response response) {
                            DataModel.loading_box_stop();
                            myId=0;
                            pos=0;
                            change=0;
                            ActivityBusinessDetails.cProvince=Pid+"";
                            ActivityBusinessDetails.viewPager.setCurrentItem(1);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            DataModel.loading_box_stop();
                        }
                    });
                    }catch (Exception e){e.printStackTrace();}
                }else {
                    a=language_data.getString("InternetMessage");
                    DataModel.showSnackBar(view,a);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            }
        });

        getSpinner();
        loadData();
    }

    void loadData(){
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "onboardBusinessDetails");
            String a=language_data.getString("labelComapnyName");
            inputCName.setHint(a);
            a=language_data.getString("labelProvince");
            tvProvince.setText(a);
            a=language_data.getString("labelDistrict");
            tvDistrict.setText(a);
            a=language_data.getString("labelCity");
            tvStreet.setText(a);
            a=language_data.getString("labelStreetNum");
            inputStNum.setHint(a);
            a=language_data.getString("labelPostalCode");
            inputPCode.setHint(a);
            a=language_data.getString("labelBusinessRegNum");
            inputBRN.setHint(a);
            a=language_data.getString("labelContinueBtn");
            continueBtn.setText(a);
        }catch (Exception e){

        }
    }

    public void getSpinner(){
       try{
        header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
        RestClientWithoutString.getApiService(getActivity()).getProvinces(header, new Callback<GetProvince>() {
            @Override
            public void success(GetProvince getProvince, Response response) {
                try {
                    myId=0;
                    provinces=new ArrayList<>();
                    provincesId = new int[getProvince.getResponse().getProvince().length];
                    provinces.add("Choose your option");
                    for (int i = 0; i < getProvince.getResponse().getProvince().length; i++) {
                        provinces.add(getProvince.getResponse().getProvince()[i].getProvince_name());
                        provincesId[i] = getProvince.getResponse().getProvince()[i].getProvince_id();
                    }
                    SharedPrefNames.provinces=provinces;
                    adapPro = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, provinces);
                    adapPro.setDropDownViewResource(R.layout.spinner_item);
                    province.setAdapter(adapPro);
                       if (ActivityBusinessDetails.cProvince != null) {
                            cProvince = ActivityBusinessDetails.cProvince;
                            pos = Integer.parseInt(cProvince);
                            myId = pos;
                            for (int i = 0; i < provincesId.length; i++) {
                                if (pos == provincesId[i]) {
                                    pos = i + 1;
                                    break;
                                }
                            }
                            change = 0;
                            province.setSelection(pos);
                           //ActivityBusinessDetails.cProvince=null;
                        }
                    province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                change++;
                                cProvince = province.getSelectedItem().toString();
                                if (!cProvince.equalsIgnoreCase("Choose your option")) {
                                    DataModel.loading_box(getActivity(), true);
                                    String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                    Pid = provincesId[position - 1];
                                    RestClientWithoutString.getApiService(getActivity()).getDistrict(Pid + "", header, new Callback<GetDistrict>() {
                                        @Override
                                        public void success(GetDistrict getDistrict, Response response) {
                                            try {
                                                DataModel.loading_box_stop();
                                                dist = new ArrayList<String>();
                                                distId = new int[getDistrict.getResponse().getDistrict().length];
                                                dist.add("Choose your option");
                                                for (int i = 0; i < getDistrict.getResponse().getDistrict().length; i++) {
                                                    dist.add(getDistrict.getResponse().getDistrict()[i].getDistrict_name());
                                                    distId[i] = getDistrict.getResponse().getDistrict()[i].getDistrict_id();
                                                }
                                                adapDis = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dist);
                                                adapDis.setDropDownViewResource(R.layout.spinner_item);
                                                district.setAdapter(adapDis);
                                                adapCity = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
                                                adapCity.setDropDownViewResource(R.layout.spinner_item);
                                                city.setAdapter(adapCity);
                                                if (myId!=0&&change==1) {
                                                    for (int i = 0; i < distId.length; i++) {
                                                        if (myId == distId[i]) {
                                                            pos = i+1;
                                                            break;
                                                        }
                                                    }
                                                    district.setSelection(pos);
                                                }
                                                district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        cDistrict=district.getSelectedItem().toString();
                                                        if(!cDistrict.equalsIgnoreCase("Choose your option")) {
                                                            DataModel.loading_box(getActivity(), true);
                                                            String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                                            int Pid = distId[position - 1];
                                                            RestClientWithoutString.getApiService(getActivity()).getCity(Pid + "", header, new Callback<GetCity>() {
                                                                @Override
                                                                public void success(GetCity getCity, Response response) {
                                                                    try{ DataModel.loading_box_stop();
                                                                        cities = new ArrayList<String>();
                                                                        cities.add("Choose your option");
                                                                        citId=new int[getCity.getResponse().getCity().length];
                                                                        for (int i = 0; i < getCity.getResponse().getCity().length; i++) {
                                                                            cities.add(getCity.getResponse().getCity()[i].getCity_name());
                                                                            citId[i]=getCity.getResponse().getCity()[i].getCity_id();
                                                                        }
                                                                        adapCity = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
                                                                        adapCity.setDropDownViewResource(R.layout.spinner_item);
                                                                        city.setAdapter(adapCity);
                                                                        if(myId!=0&&change==1){
                                                                            for(int i=0;i<citId.length;i++) {
                                                                                if(myId==citId[i]){
                                                                                    pos=i+1;
                                                                                    break;
                                                                                }
                                                                            }
                                                                            city.setSelection(pos);
                                                                        }
                                                                    }catch (Exception e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                @Override
                                                                public void failure(RetrofitError error) {
                                                                    DataModel.loading_box_stop();
                                                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            DataModel.loading_box_stop();
                                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (Exception e) {
                }
            }

            @SuppressLint("NewApi")
            @Override
            public void failure(RetrofitError error) {
                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                DataModel.showSnackBarError(view, json);
            }
        });
    }catch (Exception e){
        e.printStackTrace();
    }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
           getSpinner();
        }
    }

}
