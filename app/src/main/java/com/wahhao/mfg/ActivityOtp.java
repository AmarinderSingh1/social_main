package com.wahhao.mfg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.BaseActivity;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class ActivityOtp extends BaseActivity {

    private Toolbar mTopToolbar;
    EditText editOTP;
    Button submitBtn;
    TextView textResendOtp;
    String sncak, otp;
    CoordinatorLayout cLayout;
    JSONObject language_data;
    String phone,code;
    Prefs sp;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
    }

    public void init() {
        sp=new Prefs(this);
        phone = getIntent().getStringExtra("phone");
        code=getIntent().getStringExtra("country");
        cLayout = (CoordinatorLayout) findViewById(R.id.coParent);
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        editOTP = (EditText) findViewById(R.id.editOtp);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        textResendOtp = (TextView) findViewById(R.id.textOtp);
        language_data = new JSONObject();
        editOTP.clearFocus();
        editOTP.setClickable(true);
        backArrow=(ImageView)findViewById(R.id.navBackBtn);
        editOTP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    submit(editOTP);
                }
                return false;
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        loadData();
    }

    public void loadData() {
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "verification");
            String a = language_data.getString("labelEnterOTP");
            editOTP.setHint(a);
            a = language_data.getString("labelSubmitButton");
            submitBtn.setText(a);
            a = language_data.getString("labelResendOTP");
            textResendOtp.setText(a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public void submit(View view) {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            String a="";
            otp = editOTP.getText().toString();
            if (otp.length() < 6) {
                a=language_data.getString("labelOTPError");
                DataModel.showSnackBar(cLayout,a);
            } else if (!DataModel.Internetcheck(this)) {
                a=language_data.getString("InternetMessage");
                DataModel.showSnackBar(cLayout,a);
                //Api code here
            } else {
                JSONObject ob = new JSONObject();
                try {

                    ob.put("device_token", "aaa");
                    ob.put("device_type", "android");
                    ob.put("device_id", "android");
                    ob.put("app_version", "101");
                    ob.put("secret_key", DataModel.secrate_key);
                    ob.put("registered_mobile_area_code", code);
                    ob.put("registered_mobile", phone);
                    ob.put("one_time_password", otp);

                } catch (Exception e) {
                }

                DataModel.loading_box(this, true);
                RestClientWithoutString.getApiService(this).userOtp(DataModel.secrate_key,
                        new TypedString(ob.toString()), new Callback<basic_response>() {
                            @Override
                            public void success(basic_response b, Response response) {
                                String accessToken=b.getResponse().getUserInfo().getAccessToken();
                                int stageKey=b.getResponse().getUserInfo().getContractResourceID();
                                String compKey=b.getResponse().getUserInfo().getComp_stage_key();
                                int sellerId=b.getResponse().getUserInfo().getSellerId();
                                String name=b.getResponse().getUserInfo().getFullName();
                                String pic=b.getResponse().getUserInfo().getProfilePic();
                                boolean status=false;
                                if(stageKey==0){
                                    status=false;
                                }
                                else{
                                    status=true;
                                }
                                sp.save(SharedPrefNames.ACCESS_TOKEN,accessToken);
                                sp.save(SharedPrefNames.CONTRACT_ID,stageKey);
                                sp.save(SharedPrefNames.HAS_SIGNED_CONTRACT,status);
                                sp.save(SharedPrefNames.COMP_STAGE_KEY,compKey);
                                sp.save(SharedPrefNames.SELLER_ID,sellerId+"");
                                sp.save(SharedPrefNames.FULL_NAME,name);
                                sp.save(SharedPrefNames.PROFILE_PIC,pic);
                                sp.save(SharedPrefNames.C_CODE,code);
                                sp.save(SharedPrefNames.PHONE_NUMBER,phone);
                                DataModel.loading_box_stop();
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                finish();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                DataModel.loading_box_stop();
                                try {
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    DataModel.showSnackBarError(cLayout,json);
                                    editOTP.setText("");
                                } catch (Exception e) {
                                }
                            }
                        });
            }
        } catch (Exception e) {
            Log.e("Error ", " " + e.getMessage());
        }
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressLint("NewApi")
    public void resendOtp(View view) {

        JSONObject ob = new JSONObject();
        try {

            ob.put("device_token", "aaa");
            ob.put("device_type", "android");
            ob.put("device_id", "android");
            ob.put("app_version", "101");
            ob.put("secret_key", DataModel.secrate_key);
            ob.put("registered_mobile_area_code", code);
            ob.put("registered_mobile", phone);

        } catch (Exception e) {
        }

        DataModel.loading_box(this, true);
        RestClientWithoutString.getApiService(this).userAuth(DataModel.secrate_key,
                new TypedString(ob.toString()), new Callback<basic_response>() {
                    @Override
                    public void success(basic_response basic_response, Response response) {
                        DataModel.loading_box_stop();
                        try {
                            String a=basic_response.getResponse().getMessage();
                            DataModel.showSnackBar(cLayout,a);
                        } catch (Exception e) {
                        }
                    }
                        @Override
                        public void failure(RetrofitError error) {
                            DataModel.loading_box_stop();
                            try {
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                DataModel.showSnackBarError(cLayout,json);
                            } catch (Exception e) {
                            }}
                });
                }
}
