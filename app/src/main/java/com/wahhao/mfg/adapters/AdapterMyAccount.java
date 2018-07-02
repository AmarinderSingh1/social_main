package com.wahhao.mfg.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.FragmentProfile;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentComnapyInfo;
import com.wahhao.mfg.model.GetCity;
import com.wahhao.mfg.model.GetDistrict;
import com.wahhao.mfg.model.GetProfile;
import com.wahhao.mfg.model.GetProvince;
import com.wahhao.mfg.model.GetSeller;
import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class AdapterMyAccount extends RecyclerView.Adapter<AdapterMyAccount.MyViewHolder>{
    private LayoutInflater inflater;
    private Context context;
    List<String> headings;
    List<String> texts;
    String heading="";
    String value="";
    Prefs sp;



    public AdapterMyAccount(Context context, List<String> headings, List<String> values){
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.headings=headings;
        this.texts=values;
        sp=new Prefs(context);
    }

    @NonNull
    @Override
    public AdapterMyAccount.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_myaccount, parent, false);
        AdapterMyAccount.MyViewHolder holder = new AdapterMyAccount.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMyAccount.MyViewHolder holder, final int position) {
        heading=headings.get(position);
        value=texts.get(position);
                holder.tvHeading.setText(heading);
                holder.tvText.setText(value);
                    switch (position) {
                        case 0:
                            holder.imgV1.setImageResource(R.drawable.profile_icon);
                            break;
                        case 1:
                            holder.imgV1.setImageResource(R.drawable.c_info_icon);
                            break;
                        case 2:
                            holder.imgV1.setImageResource(R.drawable.bank_icon);
                            break;
                        case 3:
                            holder.imgV1.setImageResource(R.drawable.pickup_icon);
                            break;
                }

                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            switch (position) {
                                case 0:
                                    try {
                                        DataModel.loading_box(context,true);
                                        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                        RestClientWithoutString.getApiService(context).getProfile(header, new Callback<GetProfile>() {
                                            @Override
                                            public void success(GetProfile gp, Response response) {
                                                DataModel.loading_box_stop();
                                                FragmentProfile.etName.setText(gp.getResponse().getFull_name().toString());
                                                FragmentProfile.etEmail.setText(gp.getResponse().getCompany_email());
                                                FragmentProfile.etphone.setText(gp.getResponse().getRegistered_mobile());
                                                int code=Integer.parseInt(gp.getResponse().getRegistered_mobile_area_code());
                                                if(code==86){
                                                    FragmentProfile.code.setSelection(0);
                                                }if(code==91){
                                                    FragmentProfile.code.setSelection(1);
                                                }
                                                String gender=gp.getResponse().getGender();
                                                if(gender!=null) {
                                                    gender = gender.toLowerCase();
                                                    if (gender.equalsIgnoreCase("male")) {
                                                        FragmentProfile.gen="male";
                                                        FragmentProfile.male.setImageResource(R.drawable.men_dark);
                                                    } else {
                                                        FragmentProfile.gen="female";
                                                        FragmentProfile.female.setImageResource(R.drawable.women_dark);
                                                    }
                                                }
                                                String dob=gp.getResponse().getDob();
                                                if(dob!=null){
                                                    FragmentProfile.etBday.setText(dob);
                                                }else{
                                                    FragmentProfile.etBday.setText("");
                                                }
                                                String img=gp.getResponse().getProfile_pic_name();
                                                if(img!=null){
                                                    sp.save(SharedPrefNames.PROFILE_PIC,img);
                                                    Picasso.get().load(img)
                                                            .into(FragmentProfile.profileUpload);
                                                }
                                                FragmentProfile.etName.clearFocus();
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                DataModel.loading_box_stop();
                                            }
                                        });

                                        FragmentProfile.base.setVisibility(View.GONE);
                                        FragmentProfile.profile.setVisibility(View.VISIBLE);
                                        MainActivity.backArrow.setVisibility(View.VISIBLE);
                                        String a = FragmentProfile.language_data.getString("labelMyProfileTitle");
                                        MainActivity.toolTitle.setText(a);
                                    }catch (Exception e){}
                                    break;
                                case 1:
                                    getSpinner();
                                    FragmentProfile.base.setVisibility(View.GONE);
                                    FragmentProfile.profile.setVisibility(View.GONE);
                                    FragmentProfile.compInfo.setVisibility(View.VISIBLE);
                                    MainActivity.backArrow.setVisibility(View.VISIBLE);
                                    String a = FragmentProfile.language_data.getString("labelCompanyTitle");
                                    MainActivity.toolTitle.setText(a);
                                    break;
                            }
                        }catch (Exception e){}

                    }
                });

    }

    @Override
    public int getItemCount() {
            return headings.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgV1,imgV2;
        TextView tvHeading,tvText;
        LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemLayout=(LinearLayout)itemView.findViewById(R.id.itemLayout);
            imgV1=(ImageView)itemView.findViewById(R.id.imgView);
            tvHeading=(TextView)itemView.findViewById(R.id.tvHeading);
            tvText=(TextView)itemView.findViewById(R.id.tvtext);

        }
    }


    public void getSpinner(){
        try{
            final String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
            String sellerId = sp.getString(SharedPrefNames.SELLER_ID);
            RestClientWithoutString.getApiService(context).getSeller(sellerId, header, new Callback<GetSeller>() {
                @Override
                public void success(final GetSeller getSeller, Response response) {
                    try {
                        DataModel.loading_box_stop();
                        String cName = getSeller.getResponse().getDetails().getUser().getC_name();
                        String cStreet = getSeller.getResponse().getDetails().getUser().getStreet();
                        String cPostalCode = getSeller.getResponse().getDetails().getUser().getPostal_code();
                        String cBRN = getSeller.getResponse().getDetails().getUser().getBusiness_registration_number();
                        final String cProvince = getSeller.getResponse().getDetails().getUser().getProvince();
                        String cDistrict = getSeller.getResponse().getDetails().getUser().getDistrict();
                        String cCity = getSeller.getResponse().getDetails().getUser().getCity();
                        if (cName != null && cBRN != null && cStreet != null && cPostalCode != null) {
                            FragmentProfile.etCompName.setText(cName);
                            FragmentProfile.etStNum.setText(cStreet);
                            FragmentProfile.etBRN.setText(cBRN);
                            FragmentProfile.etPCode.setText(cPostalCode);
                        }
                       RestClientWithoutString.getApiService(context).getProvinces(header, new Callback<GetProvince>() {
                            @Override
                            public void success(GetProvince getProvince, Response response) {
                                try {
                                    FragmentProfile.myId = 0;
                                    FragmentProfile.provinces = new ArrayList<>();
                                    FragmentProfile.provincesId = new int[getProvince.getResponse().getProvince().length];
                                    FragmentProfile.provinces.add("Choose your option");
                                    for (int i = 0; i < getProvince.getResponse().getProvince().length; i++) {
                                        FragmentProfile.provinces.add(getProvince.getResponse().getProvince()[i].getProvince_name());
                                        FragmentProfile.provincesId[i] = getProvince.getResponse().getProvince()[i].getProvince_id();
                                    }
                                    SharedPrefNames.provinces = FragmentProfile.provinces;
                                    FragmentProfile.adapPro = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, FragmentProfile.provinces);
                                    FragmentProfile.adapPro.setDropDownViewResource(R.layout.spinner_item);
                                    FragmentProfile.province.setAdapter(FragmentProfile.adapPro);
                                    if (cProvince != null) {
                                        FragmentProfile.cProvince = cProvince;
                                        FragmentProfile.pos = Integer.parseInt(FragmentProfile.cProvince);
                                        FragmentProfile.myId = FragmentProfile.pos;
                                        for (int i = 0; i < FragmentProfile.provincesId.length; i++) {
                                            if (FragmentProfile.pos == FragmentProfile.provincesId[i]) {
                                                FragmentProfile.pos = i + 1;
                                                break;
                                            }
                                        }
                                        FragmentProfile.change = 0;
                                        FragmentProfile.province.setSelection(FragmentProfile.pos);
                                        //ActivityBusinessDetails.cProvince=null;
                                    }
                                    FragmentProfile.province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            try {
                                                FragmentProfile.change++;
                                                FragmentProfile.cProvince = FragmentProfile.province.getSelectedItem().toString();
                                                if (!FragmentProfile.cProvince.equalsIgnoreCase("Choose your option")) {
                                                    DataModel.loading_box(context, true);
                                                    String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                                    FragmentProfile.Pid = FragmentProfile.provincesId[position - 1];
                                                    RestClientWithoutString.getApiService(context).getDistrict(FragmentProfile.Pid + "", header, new Callback<GetDistrict>() {
                                                        @Override
                                                        public void success(GetDistrict getDistrict, Response response) {
                                                            try {
                                                                DataModel.loading_box_stop();
                                                                FragmentProfile.dist = new ArrayList<String>();
                                                                FragmentProfile.distId = new int[getDistrict.getResponse().getDistrict().length];
                                                                FragmentProfile.dist.add("Choose your option");
                                                                for (int i = 0; i < getDistrict.getResponse().getDistrict().length; i++) {
                                                                    FragmentProfile.dist.add(getDistrict.getResponse().getDistrict()[i].getDistrict_name());
                                                                    FragmentProfile.distId[i] = getDistrict.getResponse().getDistrict()[i].getDistrict_id();
                                                                }
                                                                FragmentProfile.adapDis = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, FragmentProfile.dist);
                                                                FragmentProfile.adapDis.setDropDownViewResource(R.layout.spinner_item);
                                                                FragmentProfile.district.setAdapter(FragmentProfile.adapDis);
                                                                FragmentProfile.adapCity = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, FragmentProfile.ITEMS);
                                                                FragmentProfile.adapCity.setDropDownViewResource(R.layout.spinner_item);
                                                                FragmentProfile.city.setAdapter(FragmentProfile.adapCity);
                                                                if (FragmentProfile.myId != 0 && FragmentProfile.change == 1) {
                                                                    for (int i = 0; i < FragmentProfile.distId.length; i++) {
                                                                        if (FragmentProfile.myId == FragmentProfile.distId[i]) {
                                                                            FragmentProfile.pos = i + 1;
                                                                            break;
                                                                        }
                                                                    }
                                                                    FragmentProfile.district.setSelection(FragmentProfile.pos);
                                                                }
                                                                FragmentProfile.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                        FragmentProfile.cDistrict = FragmentProfile.district.getSelectedItem().toString();
                                                                        if (!FragmentProfile.cDistrict.equalsIgnoreCase("Choose your option")) {
                                                                            DataModel.loading_box(context, true);
                                                                            String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                                                            int Pid = FragmentProfile.distId[position - 1];
                                                                            RestClientWithoutString.getApiService(context).getCity(Pid + "", header, new Callback<GetCity>() {
                                                                                @Override
                                                                                public void success(GetCity getCity, Response response) {
                                                                                    try {
                                                                                        DataModel.loading_box_stop();
                                                                                        FragmentProfile.cities = new ArrayList<String>();
                                                                                        FragmentProfile.cities.add("Choose your option");
                                                                                        FragmentProfile.citId = new int[getCity.getResponse().getCity().length];
                                                                                        for (int i = 0; i < getCity.getResponse().getCity().length; i++) {
                                                                                            FragmentProfile.cities.add(getCity.getResponse().getCity()[i].getCity_name());
                                                                                            FragmentProfile.citId[i] = getCity.getResponse().getCity()[i].getCity_id();
                                                                                        }
                                                                                        FragmentProfile.adapCity = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, FragmentProfile.cities);
                                                                                        FragmentProfile.adapCity.setDropDownViewResource(R.layout.spinner_item);
                                                                                        FragmentProfile.city.setAdapter(FragmentProfile.adapCity);
                                                                                        if (FragmentProfile.myId != 0 && FragmentProfile.change == 1) {
                                                                                            for (int i = 0; i < FragmentProfile.citId.length; i++) {
                                                                                                if (FragmentProfile.myId == FragmentProfile.citId[i]) {
                                                                                                    FragmentProfile.pos = i + 1;
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                            FragmentProfile.city.setSelection(FragmentProfile.pos);
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void failure(RetrofitError error) {
                                                                                    DataModel.loading_box_stop();
                                                                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                                    }
                                                                });
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void failure(RetrofitError error) {
                                                            DataModel.loading_box_stop();
                                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });


                                    FragmentProfile.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                //DataModel.showSnackBarError(view, json);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                        @SuppressLint("NewApi")
                        @Override
                        public void failure(RetrofitError error) {
                            DataModel.loading_box_stop();
                            String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                            //DataModel.showSnackBarError(view,json);
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
        }
    }

