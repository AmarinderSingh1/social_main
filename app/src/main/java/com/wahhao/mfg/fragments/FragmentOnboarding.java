package com.wahhao.mfg.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterMyAccount;
import com.wahhao.mfg.model.OnboardResponse;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class FragmentOnboarding extends BaseFragment {



    int fragCount;
    private View view;
    public static View l1,l2,l3,l4,r1,r2,r3;
    public static ImageView img1,img2,img3,imv1,imv2,imv3;
    private Toolbar toolbar;
    private RecyclerView rcView;
    private AdapterMyAccount adapter;
    JSONObject language_data;
    List<String> headings;
    List<String> values;
    Prefs sp;
    public static TextView tvBInfo,tvIReview,tvSContract,tvheading1,tvheading2,tvheading3,tvvalue1,tvvalue2,tvvalue3,textLast;
    public static LinearLayout businessDetails,waitingApproval,signContract;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        init(view);
        return view;
    }


    void init(View view){
        sp=new Prefs(getActivity());
        getDetails();
        tvheading1=(TextView)view.findViewById(R.id.tvHeading1);
        tvheading2=(TextView)view.findViewById(R.id.tvHeading2);
        tvheading3=(TextView)view.findViewById(R.id.tvHeading3);
        tvvalue1=(TextView)view.findViewById(R.id.textvalue1);
        tvvalue2=(TextView)view.findViewById(R.id.textvalue2);
        tvvalue3=(TextView)view.findViewById(R.id.textvalue3);
        textLast=(TextView)view.findViewById(R.id.lastText);
        businessDetails=(LinearLayout)view.findViewById(R.id.businessDetails);
        waitingApproval=(LinearLayout)view.findViewById(R.id.waitingApproval);
        signContract=(LinearLayout)view.findViewById(R.id.signContract);
        signContract.setClickable(false);
        tvBInfo=(TextView)view.findViewById(R.id.textBInfo);
        tvIReview=(TextView)view.findViewById(R.id.textIReview);
        tvSContract=(TextView)view.findViewById(R.id.textSContract);
        l1=(View)view.findViewById(R.id.viewL1);
        l2=(View)view.findViewById(R.id.viewL2);
        l3=(View)view.findViewById(R.id.viewL3);
        l4=(View)view.findViewById(R.id.viewL4);
        r1=(View)view.findViewById(R.id.viewR1);
        r2=(View)view.findViewById(R.id.viewR2);
        r3=(View)view.findViewById(R.id.viewR3);
        img1=(ImageView)view.findViewById(R.id.imgView1);
        img2=(ImageView)view.findViewById(R.id.imgView2);
        img3=(ImageView)view.findViewById(R.id.imgView3);
        imv1=(ImageView)view.findViewById(R.id.imgV);
        imv2=(ImageView)view.findViewById(R.id.imgV1);
        loadData();
        String comp=sp.getString(SharedPrefNames.COMP_STAGE_KEY);
        if(comp.equalsIgnoreCase("register")||comp.equalsIgnoreCase("company_info")||comp.equalsIgnoreCase("agent_details")) {
            tvheading2.setTextColor(getResources().getColor(R.color.light_black));
            tvheading3.setTextColor(getResources().getColor(R.color.light_black));
        }else if(comp.equalsIgnoreCase("other_info")){
            r1.setBackgroundResource(R.drawable.roundgreen);
            l1.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
            r2.setBackgroundResource(R.drawable.roundyellow);
            l2.setBackgroundColor(getResources().getColor(R.color.onBoardYellow));
            l3.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
            l4.setBackgroundColor(getResources().getColor(R.color.onBoardYellow));
            img1.setImageResource(R.drawable.number1_grenn);
            img2.setImageResource(R.drawable.number2yellow);
            businessDetails.setClickable(false);
            signContract.setClickable(false);
            imv1.setVisibility(View.GONE);
            tvheading3.setTextColor(getResources().getColor(R.color.light_black));
        }else if(comp.equalsIgnoreCase("approved")) {
            r1.setBackgroundResource(R.drawable.roundgreen);
            l1.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
            r2.setBackgroundResource(R.drawable.roundgreen);
            l2.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
            l3.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
            l4.setBackgroundColor(getResources().getColor(R.color.onBoardGreen));
            img1.setImageResource(R.drawable.number1_grenn);
            img2.setImageResource(R.drawable.number2_green);
            img3.setImageResource(R.drawable.number3_dark_grey);
            businessDetails.setClickable(false);
            signContract.setClickable(true);
            imv1.setVisibility(View.GONE);
        }

    }


    void loadData(){
        try {

            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "onboard");
            String a = language_data.getString("labelBusinessInfo");
            tvheading1.setText(a);
            a = language_data.getString("labelWaitingAppoval");
            tvheading2.setText(a);
            a = language_data.getString("labelSignContract");
            tvheading3.setText(a);
            a = language_data.getString("labelBusinessText");
            tvvalue2.setText(a);
            a = language_data.getString("labelWaitingText");
            tvvalue1.setText(a);
            a = language_data.getString("labelSignText");
            tvvalue3.setText(a);
            a = language_data.getString("labelBusinessInfo");
            tvBInfo.setText(a);
            a = language_data.getString("labelInReview");
            tvIReview.setText(a);
            a = language_data.getString("labelSignContract");
            tvSContract.setText(a);
            a = language_data.getString("labelInfotext");
            textLast.setText(a);
            a = language_data.getString("labelOnboardTitle");
            MainActivity.toolTitle.setText(a);

        }catch (Exception e){

        }
    }


    public void getDetails() {


        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
        RestClientWithoutString.getApiService(getActivity()).getDropDown(header, new Callback<OnboardResponse>() {
            @Override
            public void success(OnboardResponse onboardResponse, Response response) {
                //Toast.makeText(ActivityBusinessDetails.this, "Success", Toast.LENGTH_SHORT).show();
                String stageKey = onboardResponse.response.getCompleted_stage_key();
                boolean hasContract = onboardResponse.response.isHas_signed_contract();
                sp.save(SharedPrefNames.COMP_STAGE_KEY, stageKey);
                sp.save(SharedPrefNames.HAS_SIGNED_CONTRACT, hasContract);
            }

            @Override
            public void failure(RetrofitError error) {
                DataModel.loading_box_stop();
                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                DataModel.showSnackBarError(view, json);
            }
        });
    }



}