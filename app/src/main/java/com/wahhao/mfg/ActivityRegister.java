package com.wahhao.mfg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wahhao.mfg.adapters.SpinnerAdapter;
import com.wahhao.mfg.beans.ItemData;
import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.BaseActivity;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class ActivityRegister extends BaseActivity {

    private Toolbar mTopToolbar;
    Button registerBtn,chatBtn;
    EditText editName,editEmail,editPhone;
    String name,email,phoneNum,cCode,check;
    JSONObject language_data;
    TextView textRegister,textCoCode,already,login;
    TextInputLayout nameInput,emailInput,phoneInput;
    CoordinatorLayout corLayout;
    ImageView backArrow;
    Spinner coCode;
    String[] codes={"China (+86)","India (+91)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        try {
            mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(mTopToolbar);
            corLayout=(CoordinatorLayout)findViewById(R.id.corLayout);
            registerBtn = (Button) findViewById(R.id.registerBtn);
            chatBtn = (Button) findViewById(R.id.chatBtn);
            editName = (EditText) findViewById(R.id.editName);
            editEmail = (EditText) findViewById(R.id.editEmail);
            editPhone = (EditText) findViewById(R.id.editPhone);
            nameInput=(TextInputLayout)findViewById(R.id.nameInput);
            emailInput=(TextInputLayout)findViewById(R.id.emailInput);
            phoneInput=(TextInputLayout)findViewById(R.id.phoneInput);
            textRegister=(TextView)findViewById(R.id.textRegister);
            backArrow=(ImageView)findViewById(R.id.navBackBtn);

            already=(TextView)findViewById(R.id.already);
            login=(TextView)findViewById(R.id.textLogin);

            textCoCode=(TextView)findViewById(R.id.textCoCode);
            coCode=(Spinner)findViewById(R.id.spinCtCode);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            coCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try{if(position==0){
                        cCode="86";
                        SharedPrefNames.PHONE_LENGTH=11;
                        InputFilter[] FilterArray = new InputFilter[1];
                        FilterArray[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                        editPhone.setFilters(FilterArray);

                        check = editPhone.getText().toString();
                        if (check.length() != SharedPrefNames.PHONE_LENGTH&&check.length()!=0) {
                            String a=language_data.getString("labelPhoneNumberError");
                            DataModel.showSnackBar(corLayout, a);
                        }
                    }else if(position==1){
                        cCode="91";
                        SharedPrefNames.PHONE_LENGTH=10;
                        InputFilter[] FilterArray = new InputFilter[1];
                        FilterArray[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                        editPhone.setFilters(FilterArray);
                        check = editPhone.getText().toString();
                        if (check.length() != SharedPrefNames.PHONE_LENGTH&&check.length()!=0) {
                            String a=language_data.getString("labelPhoneNumberError");
                            DataModel.showSnackBar(corLayout, a);
                        }
                    }}catch (Exception e){}
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            language_data=new JSONObject();
            editPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        registerClick(editPhone);
                    }
                    return false;
                }
            });
            backArrow.setVisibility(View.GONE);
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            loadData();
            ArrayList<ItemData> list=new ArrayList<>();
            list.add(new ItemData("+86",R.drawable.china_flag));
            list.add(new ItemData("+91",R.drawable.india_flag));
            SpinnerAdapter adapter=new SpinnerAdapter(getApplicationContext(), R.layout.spinner_img_item,R.id.txt,list);
            coCode.setAdapter(adapter);

        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("Error "," "+e.getMessage());
        }
    }

    public void onBackPressed(){
        finish();
       overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void loadData() {
        try {
            codes=new String[2];
            language_data= DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE,0),
                            this,"register");
            String a=language_data.getString("labelName");
            nameInput.setHint(a);
            a=language_data.getString("labelEmail");
            emailInput.setHint(a);
            a=language_data.getString("regionLable");
            textCoCode.setText(a);
            a = language_data.getString("chinaCode");
            codes[0]=a;
            a = language_data.getString("indiaCode");
            codes[1]=a;
            a=language_data.getString("labelPhoneNumber");
            phoneInput.setHint(a);
            a=language_data.getString("labelRegisterButton");
            registerBtn.setText(a);
            a=language_data.getString("labelRegisterVia");
            textRegister.setText(a);
            a=language_data.getString("labelWechat");
            chatBtn.setText(a);
            a=language_data.getString("labelAlreadyAccount");
            already.setText(a);
            a=language_data.getString("labelLoginHere");
            login.setText(a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void registerClick(View view){
       try {
           String a="";
           name=editName.getText().toString();
           email=editEmail.getText().toString();
           phoneNum=editPhone.getText().toString();
           //cCode=editCode.getText().toString();
           boolean matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

           if(name.length()==0&&email.length()==0&&phoneNum.length()==0&&cCode.length()==0){
               a=language_data.getString("labelNameError");
               DataModel.showSnackBar(corLayout,a);
           }else if(name.length()<4){
               a=language_data.getString("labelNameError");
               DataModel.showSnackBar(corLayout,a);
           }else if(!matcher)
           {
               a=language_data.getString("labelEmailError");
               DataModel.showSnackBar(corLayout,a);
           }else if(phoneNum.length()<SharedPrefNames.PHONE_LENGTH){
               a=language_data.getString("labelPhoneNumberError");
               DataModel.showSnackBar(corLayout,a);
           }else if (DataModel.Internetcheck(this)) {
               //Api code for sending OTP
               DataModel.loading_box(this,true);

               JSONObject ob=new JSONObject();
               try{
                   ob.put("device_token", "aaa");
                   ob.put("device_type", "android");
                   ob.put("device_id", "android");
                   ob.put("app_version", "101");
                   ob.put("secret_key", DataModel.secrate_key);
                   ob.put("registered_mobile_area_code", cCode);
                   ob.put("registered_mobile", phoneNum);
                   ob.put("seller_name", name);
                   ob.put("middle_name", "");
                   ob.put("last_name", "");
                   ob.put("c_email", email);


               RestClientWithoutString.getApiService(this).
                       useRegister(DataModel.secrate_key, new TypedString(ob.toString()),
                       new retrofit.Callback<basic_response>() {
                           @Override
                           public void success(basic_response s, retrofit.client.Response response) {
                               DataModel.loading_box_stop();
                               try {
                                   if (s.getStatus()) {
                                       JSONObject ob = new JSONObject();
                                       ob.put("device_token", "aaa");
                                       ob.put("device_type", "android");
                                       ob.put("device_id", "android");
                                       ob.put("app_version", "101");
                                       ob.put("secret_key", DataModel.secrate_key);
                                       ob.put("registered_mobile_area_code", cCode);
                                       ob.put("registered_mobile", phoneNum);

                                        RestClientWithoutString.getApiService(getApplicationContext()).userAuth(DataModel.secrate_key, new TypedString(ob.toString()), new Callback<basic_response>() {
                                            @Override
                                            public void success(basic_response response, Response response2) {
                                                if(response.getStatus()){
                                                    Intent intent = new Intent(getApplicationContext(), ActivityOtp.class);
                                                    intent.putExtra("phone", editPhone.getText().toString());
                                                    intent.putExtra("country",cCode);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                }
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                                DataModel.showSnackBarError(corLayout,json);
                                            }
                                        });
                                   }
                               } catch (Exception e) {

                               }
                           }

                           @SuppressLint("NewApi")
                           @Override
                           public void failure(RetrofitError error) {
                               DataModel.loading_box_stop();
                               try {
                                   String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                   DataModel.showSnackBarError(corLayout,json);
                               }catch (Exception e){}
                           }

                       });
               }catch (Exception e){}
           } else {
               a=language_data.getString("InternetMessage");
               DataModel.showSnackBar(corLayout,a);
           }


       }catch (Exception e){
           Log.e("Error "," "+e.getMessage());
       }

    }

    public void weChat(View view){

    }
}
