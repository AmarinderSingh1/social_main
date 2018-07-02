package com.wahhao.mfg.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterMyAccount;
import com.wahhao.mfg.adapters.SpinnerAdapter;
import com.wahhao.mfg.beans.ItemData;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.fragments.fragment_products.FragmentSavedProduct;
import com.wahhao.mfg.model.GetProfile;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;
import com.wahhao.mfg.utils.SingleUploadBroadcastReceiver;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;


public class FragmentProfile extends BaseFragment implements SingleUploadBroadcastReceiver.Delegate {


    public static int profileId=0;
    JSONObject lang_data;

    public String[] codes;
    int fragCount;
    private View view;
    private Toolbar toolbar;
    private RecyclerView rcView;
    private AdapterMyAccount adapter;
    public static JSONObject language_data;
    public static JSONObject language_data1;
    public static JSONObject lang_data_comp;
    public static JSONObject lang_data_bank;
    public static JSONObject lang_data_pickUp;

    public static String gen="";
    List<String> headings;
    List<String> values;
    TextView tvAccount,tvStatus,tvOnboarding;
    public static TextInputLayout name,email,phone,birthday;
    public static EditText etName,etEmail,etBday,etphone,focus;
    public static TextView genderHeading,profileHeading,cCode;
    public static ImageView male,female;
    public static CircleImageView profileUpload;
    public static Button saveBtn;
    public static Spinner code;
    public static LinearLayout base;
    public static CoordinatorLayout profile,compInfo;
    Calendar myCalendar = Calendar.getInstance();
    Prefs sp;
    String pName,pBday,pUpload;


    TextInputLayout inputCName,inputState,inputStNum,inputPCode,inputBRN;
    public static EditText etCompName,etStNum,etPCode,etBRN;
    public static Button continueBtn;
    public static Spinner province,district,city;
    public static TextView tvProvince,tvDistrict,tvStreet;
    public static ArrayAdapter<String> adapPro,adapDis,adapCity;
    public static String cName,cStreet,cPostalCode,cBRN,cProvince,cDistrict,cCity,access,header;
    int provId=0,disId=0,cityId=0;
    public static List<String> dist;
    public static List<String> cities;
    public static int [] distId,citId;
    CoordinatorLayout clayout;
    public static int change=0;
    public static final String[] ITEMS = {"Choose your option"};
    public static List<String> provinces;
    public static int[] provincesId;
    public static int pos=0,myId=0;
    public static  int Pid=0;




    private final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        return view;
    }


    void init(final View view){


        MainActivity.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    FragmentProfile.base.setVisibility(View.VISIBLE);
                    FragmentProfile.profile.setVisibility(View.GONE);
                    FragmentProfile.compInfo.setVisibility(View.GONE);
                    MainActivity.backArrow.setVisibility(View.GONE);
                    MainActivity.toolTitle.setText(language_data.getString("labelAccountTitle"));
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }catch (Exception e){}
            }
        });
        sp=new Prefs(getActivity());
        base=view.findViewById(R.id.baseLayout);
        profile=view.findViewById(R.id.myProfileLayout);
        compInfo=view.findViewById(R.id.coFragInfo);
        rcView=(RecyclerView)view.findViewById(R.id.rcView);
        LinearLayoutManager lManager=new LinearLayoutManager(getActivity());
        language_data=new JSONObject();
        language_data1=new JSONObject();
        headings=new ArrayList<>();
        values=new ArrayList<>();
        tvAccount=(TextView)view.findViewById(R.id.textAccount);
        tvStatus=(TextView)view.findViewById(R.id.textStatus);
        tvOnboarding=(TextView)view.findViewById(R.id.textonboarding);

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

        inputCName=(TextInputLayout)view.findViewById(R.id.nameUserInput);
        inputStNum=(TextInputLayout)view.findViewById(R.id.snumInput);
        inputPCode=(TextInputLayout)view.findViewById(R.id.pcodeInput);
        inputBRN=(TextInputLayout)view.findViewById(R.id.brnumInput);
        etCompName=(EditText)view.findViewById(R.id.editComName);
        etStNum=(EditText)view.findViewById(R.id.editStNum);
        etPCode=(EditText)view.findViewById(R.id.editPostalCode);
        etBRN=(EditText)view.findViewById(R.id.editBRNum);
        continueBtn=(Button)view.findViewById(R.id.conBtn);



        name=(TextInputLayout)view.findViewById(R.id.nameInput);
        email=(TextInputLayout)view.findViewById(R.id.emailInput);
        birthday=(TextInputLayout)view.findViewById(R.id.bDayInput);
        phone=(TextInputLayout)view.findViewById(R.id.phoneInput);

        genderHeading=view.findViewById(R.id.textGender);
        profileHeading=view.findViewById(R.id.textProfilePic);
        cCode=view.findViewById(R.id.textCoCode);

        focus=view.findViewById(R.id.focus);
        male=view.findViewById(R.id.genMale);
        female=view.findViewById(R.id.genFemale);
        profileUpload=view.findViewById(R.id.profile_image);

        saveBtn=view.findViewById(R.id.saveBtn);

        etName=view.findViewById(R.id.editName);
        etEmail=view.findViewById(R.id.editMail);
        etBday=view.findViewById(R.id.editBday);
        etphone=view.findViewById(R.id.editPhone);

        etEmail.setEnabled(false);
        etphone.setEnabled(false);
        code=view.findViewById(R.id.spinCtCode);

        adapter=new AdapterMyAccount(getActivity(),headings,values);
        rcView.setLayoutManager(lManager);
        rcView.setAdapter(adapter);

        try {
            loadData();
        }catch (Exception e){
            e.printStackTrace();
        }


        ArrayList<ItemData> list=new ArrayList<>();
        list.add(new ItemData("+86",R.drawable.china_flag));
        list.add(new ItemData("+91",R.drawable.india_flag));
        SpinnerAdapter adap=new SpinnerAdapter(getActivity(), R.layout.spinner_img_item,R.id.txt,list);

        code.setAdapter(adap);
        code.setEnabled(false);
        code.setClickable(false);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gen="male";
                male.setImageResource(R.drawable.men_dark);
                female.setImageResource(R.drawable.women_light);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gen="female";
                female.setImageResource(R.drawable.women_dark);
                male.setImageResource(R.drawable.men_light);
            }
        });

        profileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                 if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
                }else {
                    intent.setType("*/*");
                    startActivityForResult(intent, 1);
                }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etBday.setText(sdf.format(myCalendar.getTime()));
                etBday.setClickable(false);
                etBday.clearFocus();
                focus.requestFocus();
            }

        };
        etBday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (hasFocus) {
                        final Calendar c = Calendar.getInstance();
                        int mYear, mMonth, mDay, mHour, mMinute;

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme,
                                date, mYear, mMonth, mDay);
                        long time = System.currentTimeMillis();
                        datePickerDialog.getDatePicker().setMaxDate(time);
                        datePickerDialog.show();
                        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    etBday.clearFocus();
                                    focus.requestFocus();
                                }
                            }
                        });

                        //etBday.setEnabled(false);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etBday.getWindowToken(), 0);
                    }

                }catch (Exception e){}
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    pName = etName.getText().toString();
                    pBday = etBday.getText().toString();
                    String a = "";
                    if (pName.length() == 0) {
                        a = language_data1.getString("errorName");
                        DataModel.showSnackBar(view,a);
                    }/*else if(gen.length()==0){
                        a = language_data1.getString("errorGender");
                        DataModel.showSnackBar(view,a);
                    }else if(pBday.length()==0){
                        a = language_data1.getString("errorDOB");
                        DataModel.showSnackBar(view,a);
                    }else if(pBday.matches("")){
                        a = language_data1.getString("errorDOB");
                        DataModel.showSnackBar(view,a);
                    }*/else if(DataModel.Internetcheck(getActivity())){
                        JSONObject obj=new JSONObject();
                        obj.put("full_name",pName);
                        obj.put("gender",gen);
                        obj.put("dob",pBday);

                        Log.e(" id ",""+profileId);

                        JSONArray ob=new JSONArray();

                            if(profileId!=0) {
                                JSONObject ob1 = new JSONObject();
                                ob1.put("id", profileId);
                                ob1.put("name", "profile");
                                ob.put(ob1);
                            }
                        obj.put("resource", ob);

                        DataModel.loading_box(getActivity(),true);
                        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                        RestClientWithoutString.getApiService(getActivity()).saveProfile(header, new TypedString(obj.toString()), new Callback<basic_response>() {
                            @Override
                            public void success(basic_response response, Response response2) {
                               try {
                                   String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                   RestClientWithoutString.getApiService(getActivity()).getProfile(header, new Callback<GetProfile>() {
                                       @Override
                                       public void success(GetProfile gp, Response response) {
                                          try {
                                              DataModel.loading_box_stop();
                                              FragmentProfile.etName.setText(gp.getResponse().getFull_name().toString());
                                              MainActivity.menuTitle.setText(gp.getResponse().getFull_name().toString());
                                              FragmentProfile.etEmail.setText(gp.getResponse().getCompany_email());
                                              FragmentProfile.etphone.setText(gp.getResponse().getRegistered_mobile());
                                              int code = Integer.parseInt(gp.getResponse().getRegistered_mobile_area_code());
                                              if (code == 86) {
                                                  FragmentProfile.code.setSelection(0);
                                              }
                                              if (code == 91) {
                                                  FragmentProfile.code.setSelection(1);
                                              }
                                              String gender = gp.getResponse().getGender();
                                              if (gender != null) {
                                                  gender = gender.toLowerCase();
                                                  if (gender.equalsIgnoreCase("male")) {
                                                      FragmentProfile.gen = "male";
                                                      FragmentProfile.male.setImageResource(R.drawable.men_dark);
                                                  } else {
                                                      FragmentProfile.gen = "female";
                                                      FragmentProfile.female.setImageResource(R.drawable.women_dark);
                                                  }
                                              }
                                              String dob = gp.getResponse().getDob();
                                              if (dob != null) {
                                                  FragmentProfile.etBday.setText(dob);
                                              } else {
                                                  FragmentProfile.etBday.setText("");
                                              }
                                              String img = gp.getResponse().getProfile_pic_name();
                                              Log.e("profile pic ","name "+img);
                                              if (img != null) {
                                                  sp.save(SharedPrefNames.PROFILE_PIC, img);
                                                  Picasso.get().load(img).into(FragmentProfile.profileUpload);
                                                  Picasso.get().load(img).into(MainActivity.profilePic);
                                              }else{
                                                  FragmentProfile.profileUpload.setImageResource(R.drawable.profile_btn);
                                                  MainActivity.profilePic.setImageResource(R.drawable.pro_icon);
                                              }
                                              FragmentProfile.etName.clearFocus();
                                              profileId=0;
                                              String s=language_data1.getString("labelProfileUpdated");
                                              alert(s);
                                          }catch (Exception e){
                                              e.printStackTrace();
                                          }
                                       }

                                       @Override
                                       public void failure(RetrofitError error) {
                                           DataModel.loading_box_stop();
                                       }
                                   });



                               }catch (Exception e){}
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                DataModel.loading_box_stop();
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                DataModel.showSnackBarError(view,json);
                            }
                        });

                    }else{
                        a = language_data1.getString("InternetMessage");
                        DataModel.showSnackBar(view,a);
                    }
                }catch (Exception e){}
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String a="";
                    cName=etCompName.getText().toString();
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
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if(cName.length()==0){
                        a=lang_data_comp.getString("labelErrorCompanyName");
                        DataModel.showSnackBar(view,a);
                    }else if(cProvince.equalsIgnoreCase("Choose your option")){
                        a=lang_data_comp.getString("labelErrorProvince");
                        DataModel.showSnackBar(view,a);
                    }else if(cDistrict.equalsIgnoreCase("Choose your option")){
                        a=lang_data_comp.getString("labelErrorDistrict");
                        DataModel.showSnackBar(view,a);
                    }else if(cCity.equalsIgnoreCase("Choose your option")){
                        a=lang_data_comp.getString("labelErrorCity");
                        DataModel.showSnackBar(view,a);
                    }else if(cStreet.equalsIgnoreCase("")){
                        a=lang_data_comp.getString("labelErrorStreetNum");
                        DataModel.showSnackBar(view,a);
                    }else if(cPostalCode.equalsIgnoreCase("Choose your option")){
                        a=lang_data_comp.getString("labelErrorPostalCode");
                        DataModel.showSnackBar(view,a);
                    }else if(cPostalCode.length()<6){
                        a=lang_data_comp.getString("labelErrorPostalCode1");
                        DataModel.showSnackBar(view,a);
                    }else if(cBRN.equalsIgnoreCase("")){
                        a=lang_data_comp.getString("labelErrorBusinessRegNum");
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
                                   try {
                                       DataModel.loading_box_stop();
                                       myId = 0;
                                       pos = 0;
                                       change = 0;
                                       String s = lang_data_comp.getString("labelCompanyUpdated");
                                       alert(s);
                                   }catch (Exception e){}
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    DataModel.loading_box_stop();
                                }
                            });
                        }catch (Exception e){e.printStackTrace();}
                    }else {
                        a=lang_data_comp.getString("InternetMessage");
                        DataModel.showSnackBar(view,a);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }


    public void alert(String message){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton(language_data1.getString("labelOkBtn"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   try{ FragmentProfile.base.setVisibility(View.VISIBLE);
                    FragmentProfile.profile.setVisibility(View.GONE);
                    FragmentProfile.compInfo.setVisibility(View.GONE);
                    MainActivity.backArrow.setVisibility(View.GONE);
                    MainActivity.toolTitle.setText(language_data.getString("labelAccountTitle"));
                }catch (Exception e){}
                }
            });
            final AlertDialog alt = builder.show();
            alt.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.theme_color));
            alt.show();
        }catch (Exception e){}
    }

    void loadData(){
        try {
            language_data =new JSONObject();
            language_data1=new JSONObject();
            lang_data_comp=new JSONObject();
            String a="";
                language_data = DataModel.
                        setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                getActivity(), "myAccount");
                language_data1= DataModel.
                        setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                getActivity(), "myAccountProfile");
                lang_data_comp= DataModel.
                        setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                getActivity(), "myAccountCompanyInfo");
                codes=new String[]{language_data1.getString("labelRegionVal"),language_data1.getString("labelRegionIndiaVal")};
                a = language_data.getString("labelProfileTitle");
                headings.add(a);
                a = language_data.getString("labelCompanyTitle");
                headings.add(a);
                a = language_data.getString("labelBankTitle");
                headings.add(a);
                a = language_data.getString("labelAddressTitle");
                headings.add(a);
                a = language_data.getString("labelProfileText");
                values.add(a);
                a = language_data.getString("labelCompanyText");
                values.add(a);
                a = language_data.getString("labelBankText");
                values.add(a);
                a = language_data.getString("labelAddressText");
                values.add(a);
                a = language_data.getString("labelManageAcc");
                tvAccount.setText(a);
                a = language_data.getString("labelStatus");
                tvStatus.setText(a + " : ");

                boolean ab=sp.getBoolean(SharedPrefNames.HAS_SIGNED_CONTRACT,false);
                if(ab){
                    a = language_data.getString("labelOnboarded");
                }else{
                    a = language_data.getString("labelOnboarding");
                }
                tvOnboarding.setText(a);

                a = language_data.getString("labelAccountTitle");
                MainActivity.toolTitle.setText(a);

            a = language_data1.getString("labelName");
            name.setHint(a);
            a = language_data1.getString("labelEmail");
            email.setHint(a);
            a = language_data1.getString("labelBirthday");
            birthday.setHint(a);
            a = language_data1.getString("labelPhhoneNum");
            phone.setHint(a);
            a = language_data1.getString("labelCountryCode");
            cCode.setText(a);
            a = language_data1.getString("labelGender");
            genderHeading.setText(a);
            a = language_data1.getString("labelProfilePic");
            profileHeading.setText(a);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri uri = null;
        String path="";
        if(requestCode==1&&resultCode==getActivity().RESULT_OK){
            if (resultData != null) {
                try{
                    uri = resultData.getData();
                    path=getPath(uri);
                    FragmentProduct.fragCount=3;
                    String name=path.substring(path.lastIndexOf("/")+1,path.length());
                    if(name.endsWith("jpeg")||name.endsWith("jpg")||name.endsWith("png"))
                    {
                        profileUpload.setImageURI(uri);
                        uploadProfilePic(path,"sellers/resource");
                        DataModel.loading_box(getActivity(),true);
                    }else{
                        try {

                            JSONObject obj=new JSONObject();
                            obj=DataModel.
                                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                            getActivity(), "onboard");
                            String a = obj.getString("labelErrorFileFormat");
                            DataModel.showSnackBar(view, a);
                        }catch (Exception e){

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        if(resultCode==getActivity().RESULT_CANCELED){
            FragmentProduct.fragCount=3;
        }

    }
    @SuppressLint("NewApi")
    public String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();

        Log.d("", DatabaseUtils.dumpCursorToString(cursor));

        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();
        if(picturePath==null) {
            try {
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cur = getActivity().getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);
                String filePath = "";
                int col = cur.getColumnIndex(column[0]);
                if (cur.moveToFirst()) {
                    filePath = cur.getString(col);
                }
                cur.close();
                picturePath = filePath;
            }catch (Exception e){
                picturePath=uri.getPath();
            }
        }
        return picturePath;
    }

    private void uploadProfilePic(String uri,String str){
        try {
            String path=uri;
            Log.e("Error ",""+path);
            String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);
            MultipartUploadRequest multipartUploadRequest=   new MultipartUploadRequest(getActivity(), uploadId, "http://apidb.nvish.com/public/"+str)
                    .addFileToUpload(path, "resource") //Adding file
                    .addHeader("Auth-Identifier",header);
            multipartUploadRequest.startUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
        try{
            String a=lang_data_comp.getString("labelCompanyName");
            inputCName.setHint(a);
            a=lang_data_comp.getString("labelProvince");
            tvProvince.setText(a);
            a=lang_data_comp.getString("labelDistrict");
            tvDistrict.setText(a);
            a=lang_data_comp.getString("labelCity");
            tvStreet.setText(a);
            a=lang_data_comp.getString("labelStreetNum");
            inputStNum.setHint(a);
            a=lang_data_comp.getString("labelPostalCode");
            inputPCode.setHint(a);
            a=lang_data_comp.getString("labelBussinessRegister");
            inputBRN.setHint(a);
            a=lang_data_comp.getString("labelSaveBtn");
            continueBtn.setText(a);


        }catch (Exception e){}


    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getActivity());
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {
        Log.e("Upload error ",""+exception.getMessage());
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        Log.e("Server",serverResponseBody+"");
        String s = new String(serverResponseBody);
        try {

            JSONObject obj = new JSONObject(s);
            if (obj.getInt("code")==200) {
                JSONObject jsonObject = obj.getJSONObject("response");
                profileId=jsonObject.getInt("resource_id");
                DataModel.loading_box_stop();
            }else
            {
                DataModel.loading_box_stop();
                JSONObject jsonObject = obj.getJSONObject("response");
                String a=jsonObject.getString("message");
                DataModel.showSnackBar(view,a);
            }

        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void onCancelled() {

    }

}
