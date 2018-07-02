package com.wahhao.mfg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class ActivityLogin extends BaseActivity {

    private Toolbar mTopToolbar;
    Spinner spinCode;
    String title="",phoneNumber="",cCode="",check="";
    AlertDialog.Builder alert;
    EditText phone;
    Spinner reg;
    TextInputLayout phoneInput;
    TextView textRegion,textRegister,textRegis;
    Button loginBtn;
    JSONObject language_data;
    CoordinatorLayout coordinatorLayout;
    String [] codes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Prefs.with(this).save(SharedPrefNames.SELCTED_LANGUAGE,1);
        init();

    }

    void init(){
        try {
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coorParent);
            mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(mTopToolbar);
            phone = (EditText) findViewById(R.id.editPhone);
            reg = (Spinner) findViewById(R.id.spinCtCode);
            textRegion = (TextView) findViewById(R.id.textCoCode);
            phoneInput = (TextInputLayout) findViewById(R.id.phoneInput);
            textRegister = (TextView) findViewById(R.id.textRegister);
            textRegis = (TextView) findViewById(R.id.textRegis);
            loginBtn = (Button) findViewById(R.id.loginBtn);
            phone.clearFocus();
            language_data = new JSONObject();
            phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        Login(phone);
                    }
                    return false;
                }
            });
            loadData();
            ArrayList<ItemData> list = new ArrayList<>();
            list.add(new ItemData("+86", R.drawable.china_flag));
            list.add(new ItemData("+91", R.drawable.india_flag));
            SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_img_item, R.id.txt, list);
            reg.setAdapter(adapter);
            reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        switch (position) {
                            case 0:
                                cCode = "86";
                                SharedPrefNames.PHONE_LENGTH = 11;
                                InputFilter[] FilterArray = new InputFilter[1];
                                FilterArray[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                                phone.setFilters(FilterArray);
                                check = phone.getText().toString();
                                if (check.length() != SharedPrefNames.PHONE_LENGTH && check.length() != 0) {
                                    String a = language_data.getString("labelPhoneNumberError");
                                    DataModel.showSnackBar(coordinatorLayout, a);
                                }
                                break;
                            case 1:
                                cCode = "91";
                                SharedPrefNames.PHONE_LENGTH = 10;
                                InputFilter[] FilterArray1 = new InputFilter[1];
                                FilterArray1[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                                phone.setFilters(FilterArray1);
                                check = phone.getText().toString();
                                if (check.length() != SharedPrefNames.PHONE_LENGTH && check.length() != 0) {
                                    String a = language_data.getString("labelPhoneNumberError");
                                    DataModel.showSnackBar(coordinatorLayout, a);
                                }
                                break;
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (getIntent().hasExtra("logout")) {
            } else {
                setAlert();
            }

            coordinatorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @SuppressLint("NewApi")
    public void Login(View v){

        try{
            phoneNumber=phone.getText().toString();
            if(phoneNumber.length()<SharedPrefNames.PHONE_LENGTH){
                String a=language_data.getString("labelPhoneNumberError");
                DataModel.showSnackBar(coordinatorLayout,a);
            }
            else if(DataModel.Internetcheck(this)){
                JSONObject ob = new JSONObject();
                ob.put("device_token", "aaa");
                ob.put("device_type", "android");
                ob.put("device_id", "android");
                ob.put("app_version", "101");
                ob.put("secret_key", DataModel.secrate_key);
                ob.put("registered_mobile_area_code", cCode);
                ob.put("registered_mobile", phoneNumber);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                loginUser(ob);
            }else{
                String a=language_data.getString("InternetMessage");
                DataModel.showSnackBar(coordinatorLayout,a);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onResume(){
        super.onResume();

    }


    void loginUser(JSONObject ob){

        DataModel.loading_box(this,true);

        RestClientWithoutString.getApiService(this).userAuth(DataModel.secrate_key, new TypedString(ob.toString()),
                new retrofit.Callback<basic_response>() {
            @Override
            public void success(basic_response s, retrofit.client.Response response) {
                DataModel.loading_box_stop();
                if(s.getStatus()){
                    Toast.makeText(getApplicationContext(),s.getResponse().getMessage(),Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),ActivityOtp.class);
                    intent.putExtra("phone",phoneNumber);
                    intent.putExtra("country",cCode);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    //finish();
                }
            }
             @SuppressLint("NewApi")
            @Override
            public void failure(RetrofitError error) {
                DataModel.loading_box_stop();
                try {
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    DataModel.showSnackBarError(coordinatorLayout,json);
                }catch (Exception e){}
            }
        });


    }

    void setAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage("Set Your Language");
        builder.setCancelable(false);
        builder.setPositiveButton("中文", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Prefs.with(getApplicationContext()).save(SharedPrefNames.SELCTED_LANGUAGE,2);
                loadData();
            }
        });
        builder.setNegativeButton("English", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Prefs.with(getApplicationContext()).save(SharedPrefNames.SELCTED_LANGUAGE,1);
                loadData();
            }
        });
        final AlertDialog alt=builder.show();
        alt.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.theme_color));
        alt.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(this.getResources().getColor(R.color.theme_color));
        alt.show();
    }

    public void loadData() {
        try {

           language_data= DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE,0),
                    this,"login");

            codes=new String[]{language_data.getString("chinaCode"),language_data.getString("indiaCode")};
            String a=language_data.getString("labelPhoneNumber");
            phoneInput.setHint(a);
            a=language_data.getString("regionLable");
            textRegion.setText(a);
           /* a=language_data.getString("labelPhone");
            textPhone.setText(a);*/
            a=language_data.getString("labelNoAccount");
            textRegister.setText(a);
            a=language_data.getString("labelRegisterNow");
            textRegis.setText(a);
            a=language_data.getString("login");
            loginBtn.setText(a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void clearSel(View view){
       phone.setText("");
    }

    public void register(View view){
        Intent intent=new Intent(this,ActivityRegister.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
