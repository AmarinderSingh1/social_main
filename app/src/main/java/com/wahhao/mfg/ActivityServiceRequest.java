package com.wahhao.mfg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.model.Line;
import com.wahhao.mfg.adapters.AdapterComments;
import com.wahhao.mfg.adapters.AdapterMenu;
import com.wahhao.mfg.adapters.AdapterNotifications;
import com.wahhao.mfg.adapters.AdapterSendSample;
import com.wahhao.mfg.adapters.AdapterServiceRequest;
import com.wahhao.mfg.adapters.AdapterUploadItem;
import com.wahhao.mfg.adapters.SpinnerAdapter;
import com.wahhao.mfg.beans.Comments;
import com.wahhao.mfg.beans.ItemData;
import com.wahhao.mfg.beans.Ticket;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentAgentDetails;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentComnapyInfo;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentOtherInformation;
import com.wahhao.mfg.model.ComentResponse;
import com.wahhao.mfg.model.GetQueryTypes;
import com.wahhao.mfg.model.GetSeller;
import com.wahhao.mfg.model.GetSerReqList;
import com.wahhao.mfg.model.GetTicket;
import com.wahhao.mfg.model.GetTicketFull;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.BaseActivity;
import com.wahhao.mfg.utils.DataModel;
import com.wahhao.mfg.utils.FragNavController;
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
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class ActivityServiceRequest extends BaseActivity implements SingleUploadBroadcastReceiver.Delegate {

    public static AdapterServiceRequest adapters;
    List<Ticket> list;
    List<String> listType,names;
    List<Integer> listIds;
    Prefs sp;
    public static int tabPos;
    public static ViewPager viewPager;
    int fragCount;
    private View view;
    JSONObject language_data,langData,langDataSerReq;
    LayoutInflater inflater;
    View alertLayout;
    AlertDialog.Builder alt;
    TextView title;
    public static ImageView back;
    Button accept,noBtn;
    CheckBox check;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    public static RecyclerView rcMenu,rcSerReq,rcAttachment,rcComments,rcAttachExtra,rcUploaded;
   List<String> menu;
    private AdapterUploadItem adapter1;
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();
    public static int count=0;
    private int[] mTabIconsNonSelected = {
            R.drawable.home,
            R.drawable.product,
            R.drawable.notification,
            R.drawable.account};
  private int[] mTabIconsSelected = {
            R.drawable.home2,
            R.drawable.product2,
            R.drawable.notification2,
            R.drawable.account2};
    public static List<Integer> ids;
    //@BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;
    ImageView drawerIcon;
    TextView menuName;
    String [] tabTitles;
    public static CircleImageView proImg;
    public static FrameLayout frameLayout;
    public static RelativeLayout noData,relLayout;
    public static LinearLayout searchLay;
    public static LinearLayout linLay,linComents,linUploadFiles,linAddComment;
    public static CoordinatorLayout addReqLay,commentLay;
    public static TextView spinHead,region,attachments,allowed;
    public static TextView tvReqType,tvSRN,tvSubject,tvReqDate,tvRaised,tvCallNum,tvReqStat,tvReqTypeVal,tvSRNVal,tvSubjectVal,tvReqDateVal,tvRaisedVal,tvCallNumVal,tvReqStatVal;
    public static TextView tvDes,tvAddComent,tvallowed,tvAttachment,tvDesVal,tvUpload;
    public static Button submitBtn;
    public Button createReqBtn;
    public TextInputLayout subject,description,phone;
    public static ImageView uploadAttach,uploadExtra;
    public Spinner spinType,spinFlag;
    public EditText etSubject,etDes,etPhone,etComment;
    String cCode,sub,desc,type,phoneNum;
    public ImageView imgFlag;
    public TextView language;
    public static int lang;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_request);

        ButterKnife.bind(this);
        frameLayout=findViewById(R.id.container);
            view=getLayoutInflater().inflate(R.layout.activity_service_request,null);
            frameLayout.addView(view);
        initTab();
        loadData();

    }
    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    private void initTab() {
        try{

        langData=new JSONObject();
        langData=DataModel.
                setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                        this, "rightNavigation");
            tabTitles=new String[]{
                    langData.getString("labelHome"),
                    langData.getString("labelProducts"), langData.getString("labelNotifications"),
                    langData.getString("labelMyAccount")};
        }catch (Exception e){}

        bottomTabLayout=(TabLayout)findViewById(R.id.bottom_tab_layout);
        if (bottomTabLayout != null) {
            for (int i = 0; i < mTabIconsNonSelected.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(getTabView(i,0));
            }
        }

    }
    private View getTabView(int position,int check) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(R.color.lighter_black));
            title.setText(tabTitles[position]);
            icon.setImageDrawable(DataModel.setDrawableSelectorNone(this,
                    mTabIconsNonSelected[position],
                    mTabIconsSelected[position]));

        if(position==0){
            icon.setImageResource(mTabIconsNonSelected[0]);
        }
        return view;
    }

    @SuppressLint("NewApi")
    void loadData() {
        try {
            ids = new ArrayList<>();
            sp = new Prefs(this);
            names=new ArrayList<>();
            bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    v.setBackgroundColor(getResources().getColor(R.color.theme_color));
                    ImageView icon = (ImageView) v.findViewById(R.id.tab_icon);
                    TextView tv = (TextView) v.findViewById(R.id.title);
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tabPos = tab.getPosition();
                    if(tabPos==0){
                        icon.setImageResource(mTabIconsSelected[0]);
                    }
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    sp.save(SharedPrefNames.TAB_CLICK, tabPos);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    TextView tv = (TextView) v.findViewById(R.id.title);
                    tv.setTextColor(getResources().getColor(R.color.lighter_black));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    v.setBackgroundColor(getResources().getColor(R.color.theme_color));
                    TextView tv = (TextView) v.findViewById(R.id.title);
                    ImageView icon = (ImageView) v.findViewById(R.id.tab_icon);
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tabPos = tab.getPosition();
                    if(tabPos==0){
                        icon.setImageResource(mTabIconsSelected[0]);
                    }
                    sp.save(SharedPrefNames.TAB_CLICK, tabPos);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
            noData=(RelativeLayout)findViewById(R.id.relNoProd);
            searchLay=(LinearLayout)findViewById(R.id.searchLay);
            addReqLay=(CoordinatorLayout) findViewById(R.id.addReqLay);
            relLayout=(RelativeLayout)findViewById(R.id.relLayout);
            commentLay=(CoordinatorLayout) findViewById(R.id.commentCorLay);
            linComents=(LinearLayout)findViewById(R.id.commentLayout);
            linUploadFiles=(LinearLayout)findViewById(R.id.linUpload);
            linAddComment=(LinearLayout)findViewById(R.id.linComment);

            spinHead=(TextView)findViewById(R.id.textReqType);
            region=(TextView)findViewById(R.id.textCoCode);
            attachments=(TextView)findViewById(R.id.textUplaodDoc);
            allowed=(TextView)findViewById(R.id.textFormat);
            subject=(TextInputLayout)findViewById(R.id.subInput);
            description=(TextInputLayout)findViewById(R.id.desInput);
            phone=(TextInputLayout)findViewById(R.id.phoneInput);
            spinType=(Spinner)findViewById(R.id.spinnerReqType);
            uploadAttach=(ImageView)findViewById(R.id.upload);
            createReqBtn=(Button)findViewById(R.id.createReqBtn);
            etSubject=(EditText)findViewById(R.id.editSubject);
            etDes=(EditText)findViewById(R.id.editDes);
            etPhone=(EditText)findViewById(R.id.editPhone);
            spinFlag=(Spinner)findViewById(R.id.spinCtCode);



            tvSRN=(TextView)findViewById(R.id.textSRN);
            tvSRNVal=(TextView)findViewById(R.id.textSRNVal);
            tvReqType=(TextView)findViewById(R.id.textReqTyp);
            tvReqTypeVal=(TextView)findViewById(R.id.textReqTypeVal);
            tvSubject=(TextView)findViewById(R.id.textSubject);
            tvSubjectVal=(TextView)findViewById(R.id.textSubjectVal);
            tvReqDate=(TextView)findViewById(R.id.textReqDate);
            tvReqDateVal=(TextView)findViewById(R.id.textReqDateVal);
            tvRaised=(TextView)findViewById(R.id.textRaise);
            tvRaisedVal=(TextView)findViewById(R.id.textRaiseVal);
            tvCallNum=(TextView)findViewById(R.id.textCallNum);
            tvCallNumVal=(TextView)findViewById(R.id.textCallNumVal);
            tvReqStat=(TextView)findViewById(R.id.textReqStat);
            tvReqStatVal=(TextView)findViewById(R.id.textReqStatVal);

            tvDes=(TextView)findViewById(R.id.descriptionLabel);
            tvDesVal=(TextView)findViewById(R.id.desData);
            tvAddComent=(TextView)findViewById(R.id.addCommentLabel);
            tvallowed=(TextView)findViewById(R.id.textAllow);
            tvAttachment=(TextView)findViewById(R.id.attachLabel);
            tvUpload=(TextView)findViewById(R.id.uploadLabel);

            uploadExtra=(ImageView)findViewById(R.id.uploadExtra);

            submitBtn=(Button)findViewById(R.id.subBtn);

            etComment=(EditText)findViewById(R.id.editComment);

            rcUploaded=(RecyclerView)findViewById(R.id.recUploadedFiles);
            rcAttachExtra=(RecyclerView)findViewById(R.id.attExtraRecView);
            rcComments=(RecyclerView)findViewById(R.id.recComments);

            noBtn=findViewById(R.id.btnaddProd2);
            noBtn.setVisibility(View.GONE);
            proImg = findViewById(R.id.profile_image);
            title = (TextView) findViewById(R.id.title);
            back=(ImageView)findViewById(R.id.navBackBtn);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            rcMenu = (RecyclerView) findViewById(R.id.rcViewMenu);
            GridLayoutManager lManager = new GridLayoutManager(this, 2);
            rcMenu.setLayoutManager(lManager);
            Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
            Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
            rcMenu.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider, 2));
            rcMenu.setAdapter(MainActivity.adapter);
            rcSerReq=(RecyclerView)findViewById(R.id.serReqRec);
            rcAttachment=(RecyclerView)findViewById(R.id.attRecView);
            accept=(Button)findViewById(R.id.createBtn);

            drawerIcon = (ImageView) findViewById(R.id.navBtn);
            menuName = (TextView) findViewById(R.id.menuTitle);

            imgFlag=(ImageView)findViewById(R.id.imgFlag);
            language=(TextView)findViewById(R.id.textLanguage);


            String url = sp.getString(SharedPrefNames.PROFILE_PIC);
            if (url != null) {
                if (url.length() > 0) {
                    Picasso.get().load(url)
                            .into(proImg);
                } else {
                    proImg.setImageResource(R.drawable.pro_icon);
                }
            }
            drawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            uploadAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {
                        intent.setType("*/*");
                        startActivityForResult(intent, 1);
                    }
                }
            });

            uploadExtra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {
                        intent.setType("file/*");
                        startActivityForResult(intent, 2);
                    }
                }
            });
            String name = sp.getString(SharedPrefNames.FULL_NAME);
            if (name.length() > 0) {
                menuName.setText(name);
            }

            loadContent();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    void loadContent(){
        try{
            language_data=new JSONObject();
            language_data = DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "serviceRequest");
            langDataSerReq= DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "createServiceRequest");

            String a=language_data.getString("labelServiceRequestTitle");
            title.setText(a);
            a=language_data.getString("labelCreateServiceButton");
            accept.setText(a);
            a=language_data.getString("labelServiceRequestNumberTitle");
            tvSRN.setText(a);
            a=language_data.getString("labelRequestType");
            tvReqType.setText(a);
            a=language_data.getString("labelSubjectTitle");
            tvSubject.setText(a);
            a=language_data.getString("labelRequestDateTitle");
            tvReqDate.setText(a);
            a=language_data.getString("labelRaisedByTitle");
            tvRaised.setText(a);
            a=language_data.getString("labelCallbackNumberTitle");
            tvCallNum.setText(a);
            a=language_data.getString("labelRequestStatusTitle");
            tvReqStat.setText(a);
            a=language_data.getString("labelIssueDescriptionTitle");
            tvDes.setText(a);
            a=language_data.getString("labelAttachments");
            tvAttachment.setText(a);
            a=language_data.getString("lableAllowed");
            tvallowed.setText(a+" "+tvallowed.getText().toString());
            a=language_data.getString("labelSubmitButton");
            submitBtn.setText(a);
            lang=Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0);
            JSONObject l_data=new JSONObject();
            l_data = DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "leftNavigation");

            if(lang==1) {
                imgFlag.setImageResource(R.drawable.usa_flag);
                a = l_data.getString("languageEnglish");
                language.setText(a);
            }else {
                imgFlag.setImageResource(R.drawable.china_flag);
                a = l_data.getString("languageChinese");
                language.setText(a);
            }


            DataModel.loading_box(this,true);
            String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
            RestClientWithoutString.getApiService(this).getSerReqList(header, new Callback<GetSerReqList>() {
                @Override
                public void success(GetSerReqList getSerReqList, Response response) {
                    DataModel.loading_box_stop();
                    list=getSerReqList.getResponse().getTickets();
                    if(list.size()==0){
                        noData.setVisibility(View.VISIBLE);
                        rcSerReq.setVisibility(View.GONE);
                        relLayout.setVisibility(View.VISIBLE);
                        addReqLay.setVisibility(View.GONE);

                    }else{
                       noData.setVisibility(View.GONE);
                        rcSerReq.setVisibility(View.VISIBLE);
                        addReqLay.setVisibility(View.GONE);
                        adapters=new AdapterServiceRequest(getApplicationContext(),list,language_data);
                        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
                        rcSerReq.setLayoutManager(lm);
                        rcSerReq.setAdapter(adapters);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                DataModel.loading_box_stop();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void profileClick(View view){
        MainActivity.proClick=true;
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        tabPos = 3;
        sp.save(SharedPrefNames.TAB_CLICK,tabPos);

    }

    public void submitDetails(View view){
        try {
            inflater= getLayoutInflater();
            alertLayout=inflater.inflate(R.layout.alert_submit_details,null);
            TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
            TextView tvMessage=alertLayout.findViewById(R.id.alertMessage);
            Button okBtn=alertLayout.findViewById(R.id.alertOkBtn);
            alt=new AlertDialog.Builder(this);
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
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logOut(View view){
        String a="";
        try {

            if(DataModel.Internetcheck(this)){
                DataModel.loading_box(this,true);
                String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                RestClientWithoutString.getApiService(this).logOut(header, new Callback<basic_response>() {
                    @Override
                    public void success(basic_response submit, Response response) {
                        DataModel.loading_box_stop();
                        Intent intent=new Intent(ActivityServiceRequest.this,ActivityLogin.class);
                        intent.putExtra("logout","true");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        DataModel.loading_box_stop();

                    }
                });
            }else{
                a=language_data.getString("InternetMessage");
                DataModel.showSnackBar(view,a);
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void settings(View view){

    }

    public void serviceRequest(View view){
        Intent intent=new Intent(ActivityServiceRequest.this,ActivityServiceRequest.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    public void langAlert(View view){
        try {
            View alertLayout = getLayoutInflater().inflate(R.layout.alert_language, null);
            TextView tvUsa = alertLayout.findViewById(R.id.usa);
            TextView tvChina = alertLayout.findViewById(R.id.china);
            TextView tvHeading = alertLayout.findViewById(R.id.tvselect);
            LinearLayout chinaLay=alertLayout.findViewById(R.id.chinaLay);
            LinearLayout usaLay=alertLayout.findViewById(R.id.usaLay);

            AlertDialog.Builder alt = new AlertDialog.Builder(this);
            alt.setView(alertLayout);
            alt.setCancelable(true);
            final AlertDialog alert = alt.show();
            alert.setCanceledOnTouchOutside(true);
            language_data=new JSONObject();
            language_data=DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "leftNavigation");
            String a = language_data.getString("languageEnglish");
            tvUsa.setText(a);
            a = language_data.getString("languageChinese");
            tvChina.setText(a);
            a = language_data.getString("select_language");
            tvHeading.setText(a);

            usaLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prefs.with(getApplicationContext()).save(SharedPrefNames.SELCTED_LANGUAGE,1);
                    alert.dismiss();
                    Intent intent=new Intent(ActivityServiceRequest.this,ActivityServiceRequest.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();

                }
            });
            chinaLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prefs.with(getApplicationContext()).save(SharedPrefNames.SELCTED_LANGUAGE,2);
                    alert.dismiss();
                    Intent intent=new Intent(ActivityServiceRequest.this,ActivityServiceRequest.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                }
            });

        }catch (Exception e){

        }
    }

    public void alertSubmit(String title,String message){
        try {
            LayoutInflater inflater= getLayoutInflater();
            View alertLayout=inflater.inflate(R.layout.alert_submit_details,null);
            TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
            tvhead.setVisibility(View.GONE);
            TextView tvMessage=alertLayout.findViewById(R.id.alertMessage);
            Button okBtn=alertLayout.findViewById(R.id.alertOkBtn);
            AlertDialog.Builder alt=new AlertDialog.Builder(this);
            alt.setView(alertLayout);
            alt.setCancelable(false);
            final AlertDialog alert=alt.show();
            alert.setCanceledOnTouchOutside(false);
            tvhead.setText(title);
            tvMessage.setText(message);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    try {
                        String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                        RestClientWithoutString.getApiService(getApplicationContext()).getSerReqList(header, new Callback<GetSerReqList>() {
                            @Override
                            public void success(GetSerReqList getSerReqList, Response response) {
                                DataModel.loading_box_stop();
                                list = new ArrayList<>();
                                list = getSerReqList.getResponse().getTickets();
                                if (list.size() == 0) {
                                    noData.setVisibility(View.VISIBLE);
                                    rcSerReq.setVisibility(View.GONE);
                                    relLayout.setVisibility(View.VISIBLE);
                                    addReqLay.setVisibility(View.GONE);
                                } else {
                                    noData.setVisibility(View.GONE);
                                    rcSerReq.setVisibility(View.VISIBLE);
                                    addReqLay.setVisibility(View.GONE);
                                    adapters = new AdapterServiceRequest(getApplicationContext(), list, language_data);
                                    LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
                                    rcSerReq.setLayoutManager(lm);
                                    rcSerReq.setAdapter(adapters);
                                }
                                accept.setVisibility(View.VISIBLE);
                                back.setVisibility(View.GONE);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                DataModel.loading_box_stop();
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri uri = null;
        String path="";
        if(requestCode==1&&resultCode==this.RESULT_OK){
            if (resultData != null) {
              try{
                  count=1;
                  uri = resultData.getData();
               if(uri.toString().startsWith("content")) {
                   path = getPath(uri);
               }else{
                   path=uri.getPath();
               }
                String name=path.substring(path.lastIndexOf("/")+1,path.length());
                  name=name.toLowerCase();
                if(name.endsWith("jpeg")||name.endsWith("jpg")||name.endsWith("png")||name.endsWith("doc")||name.endsWith("docx")
                        ||name.endsWith("pdf")||name.endsWith("txt"))
                {
                uploadProfilePic(path,"support/upload");
                    names.add(name);
                    adapter1=new AdapterUploadItem(this,names,6);
                    rcAttachment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                    rcAttachment.setAdapter(adapter1);
            }else{
                String a=language_data.getString("lableInvalidMediaError");
                DataModel.showSnackBar(view,a);
                }
            }catch (Exception e){
                  e.printStackTrace();
              }
            }
        }
        if(requestCode==2&&resultCode==this.RESULT_OK){
            if (resultData != null) {
                try{
                    count=2;
                    uri = resultData.getData();
                    if(uri.toString().startsWith("content")) {
                        path = getPath(uri);
                    }else{
                        path=uri.getPath();
                    }
                    String name=path.substring(path.lastIndexOf("/")+1,path.length());
                    name=name.toLowerCase();
                    if(name.endsWith("jpeg")||name.endsWith("jpg")||name.endsWith("png")||name.endsWith("doc")||name.endsWith("docx")
                            ||name.endsWith("pdf")||name.endsWith("txt"))
                    {
                        uploadProfilePic(path,"support/upload");
                        names.add(name);
                        DataModel.loading_box(this,true);
                        AdapterUploadItem adap=new AdapterUploadItem(this,names,7);
                        rcAttachExtra.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        rcAttachExtra.setAdapter(adap);
                    }else{
                        String a=language_data.getString("lableInvalidMediaError");
                        DataModel.showSnackBar(view,a);
                    }
                }catch (Exception e){}
            }
        }

    }
    @SuppressLint("NewApi")
    public String getPath(Uri uri){
        String picturePath="";
        try {
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("", DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
             picturePath= cursor.getString(columnIndex); // returns null
            cursor.close();
            if (picturePath == null) {
                try {
                    String wholeID = DocumentsContract.getDocumentId(uri);
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";

                    Cursor cur = getContentResolver().
                            query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);

                    String filePath = "";
                    int col = cur.getColumnIndex(column[0]);
                    if (cur.moveToFirst()) {
                        filePath = cur.getString(col);
                    }
                    cur.close();
                    picturePath = filePath;
                } catch (Exception e) {
                    picturePath = uri.getPath();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
            MultipartUploadRequest multipartUploadRequest=   new MultipartUploadRequest(this, uploadId, "http://apidb.nvish.com/public/"+str)
                    .addFileToUpload(path, "resource") //Adding file
                    .addHeader("Auth-Identifier",header);
            multipartUploadRequest.startUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {

        Log.e("Server",serverResponseBody+"");
        String s = new String(serverResponseBody);

        try {
            JSONObject obj = new JSONObject(s);
            if (obj.getInt("code")==200) {
                JSONObject jsonObject = obj.getJSONObject("response");
                int id=jsonObject.getInt("resource_id");
                switch (count){
                    case 1:
                        ids.add(id);
                        break;
                    case 2:
                        ids.add(id);
                        break;
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 1);
                } else {
                    Log.e(":hello ", " permission not granted");
                }
            }
            break;
            }
    }

    public void createReq(View v){
        try {
            back.setVisibility(View.VISIBLE);
            String a=language_data.getString("labelRequestType");
            spinHead.setText(a);
            a=language_data.getString("labelSubjectTitle");
            subject.setHint(a);
            a=language_data.getString("lableAllowed");
            allowed.setText(a+" "+allowed.getText());
            a=language_data.getString("labelIssueDescriptionTitle");
            description.setHint(a);
            a=language_data.getString("labelAttachments");
            attachments.setText(a);
            a=language_data.getString("labelServiceRequest");
            title.setText(a);
            a=langDataSerReq.getString("lablelCreateRequest");
            createReqBtn.setText(a);
            etSubject.setText("");
            etDes.setText("");
            DataModel.loading_box(this,true);
            String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
            RestClientWithoutString.getApiService(this).getQueryList(header, new Callback<GetQueryTypes>() {
                @Override
                public void success(GetQueryTypes getList, Response response) {
                    try{
                        listType=new ArrayList<>();
                        listIds=new ArrayList<>();
                        listType.add("Choose your option");
                        listIds.add(0);
                        if(getList.getResponse().getQuery_types().length>0){
                            for(int i=0;i<getList.getResponse().getQuery_types().length;i++) {
                                listType.add(getList.getResponse().getQuery_types()[i].getName());
                                listIds.add(getList.getResponse().getQuery_types()[i].getId());
                            }
                        }
                        if(listType.size()>0) {
                            noData.setVisibility(View.GONE);
                            rcSerReq.setVisibility(View.GONE);
                            accept.setVisibility(View.GONE);
                            addReqLay.setVisibility(View.VISIBLE);
                            ArrayAdapter ada=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,listType);
                            ada.setDropDownViewResource(R.layout.spinner_item);
                            spinType.setAdapter(ada);
                            spinType.setSelection(0);
                            ArrayList<ItemData> listFlag = new ArrayList<>();
                            listFlag.add(new ItemData("+86", R.drawable.china_flag));
                            listFlag.add(new ItemData("+91", R.drawable.india_flag));
                            SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_img_item, R.id.txt, listFlag);
                            spinFlag.setAdapter(adapter);
                            spinFlag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        switch (position) {
                                            case 0:
                                                cCode = "86";
                                                SharedPrefNames.PHONE_LENGTH = 11;
                                                InputFilter[] FilterArray = new InputFilter[1];
                                                FilterArray[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                                                etPhone.setFilters(FilterArray);
                                                String check = etPhone.getText().toString();
                                                if (check.length() != SharedPrefNames.PHONE_LENGTH && check.length() != 0) {
                                                    String a = language_data.getString("labelPhoneNumberError");
                                                    DataModel.showSnackBar(view, a);
                                                }
                                                break;
                                            case 1:
                                                cCode = "91";
                                                SharedPrefNames.PHONE_LENGTH = 10;
                                                InputFilter[] FilterArray1 = new InputFilter[1];
                                                FilterArray1[0] = new InputFilter.LengthFilter(SharedPrefNames.PHONE_LENGTH);
                                                etPhone.setFilters(FilterArray1);
                                                check = etPhone.getText().toString();
                                                if (check.length() != SharedPrefNames.PHONE_LENGTH && check.length() != 0) {
                                                    String a = language_data.getString("labelPhoneNumberError");
                                                    DataModel.showSnackBar(view, a);
                                                }
                                                break;
                                        }
                                        DataModel.loading_box_stop();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            etPhone.setText(sp.getString(SharedPrefNames.PHONE_NUMBER));
                            String Code=sp.getString(SharedPrefNames.C_CODE);
                            if(Code.equalsIgnoreCase("86")){
                                spinFlag.setSelection(0);
                            }else{
                                spinFlag.setSelection(1);
                            }
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    DataModel.loading_box_stop();
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    DataModel.showSnackBarError(view,json);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createRequest(View v){
           try {
               sub = etSubject.getText().toString();
               desc = etDes.getText().toString();
               phoneNum = etPhone.getText().toString();
               String t=spinType.getSelectedItem().toString();
               int index=spinType.getSelectedItemPosition();
               type=listIds.get(index).toString();
               String a="";
               if (t.equalsIgnoreCase("Choose your option")) {
                    a= language_data.getString("labelErrorRequestType");
                   DataModel.showSnackBar(view, a);
               } else if (sub.length() == 0) {
                    a = language_data.getString("labelErrorSubject");
                   DataModel.showSnackBar(view, a);
               } else if (desc.length() == 0) {
                   a= language_data.getString("labelErrorIssueDescription");
                   DataModel.showSnackBar(view, a);
               } else if (phoneNum.length() == 0||phoneNum.length()<SharedPrefNames.PHONE_LENGTH) {
                   a= language_data.getString("labelPhoneNumberError");
                   DataModel.showSnackBar(view, a);//lableDocumentError
               }/*else if (rcAttachment.getAdapter()==null) {
                   a= language_data.getString("lableDocumentError");
                   DataModel.showSnackBar(view, a);
               }*/
               else if (DataModel.Internetcheck(this)) {
                    DataModel.loading_box(this,true);
                    JSONObject obj=new JSONObject();
                    obj.put("ticket_title",sub);
                   obj.put("ticket_desc",desc);
                   obj.put("query_type",type);
                   obj.put("phone",phoneNum);
                   obj.put("country_code",cCode);
                   JSONArray ob=new JSONArray();
                   for(int i=0;i<ids.size();i++) {
                       JSONObject oj2 = new JSONObject();
                       oj2.put("id", ids.get(i));
                       oj2.put("name", "document");
                       ob.put(oj2);
                   }
                   obj.put("resource",ob);

                    String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                    RestClientWithoutString.getApiService(this).createRequest(header, new TypedString(obj.toString()), new Callback<GetTicket>() {
                        @Override
                        public void success(GetTicket getTicket, Response response) {
                            try{
                                DataModel.loading_box_stop();
                                String a=language_data.getString("lablelReqNumber");
                                a=a+" "+getTicket.getResponse().ticket_id;
                                title.setText(language_data.getString("labelServiceRequestTitle"));
                                alertSubmit("",a);
                                ids.clear();
                            }catch (Exception e){}
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            try{
                                DataModel.loading_box_stop();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
               } else {
                  /* a = language_data.getString("labelPhoneNumberError");
                   DataModel.showSnackBar(view, a);*/
               }
           }catch (Exception e){
               e.printStackTrace();
           }
    }

    public void onBackPressed(){
        try {
            if (list==null) {
                commentLay.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                addReqLay.setVisibility(View.GONE);
                rcSerReq.setVisibility(View.GONE);
                accept.setVisibility(View.VISIBLE);
                relLayout.setVisibility(View.VISIBLE);
            } else {
                noData.setVisibility(View.GONE);
                commentLay.setVisibility(View.GONE);
                rcSerReq.setVisibility(View.VISIBLE);
                accept.setVisibility(View.VISIBLE);
                addReqLay.setVisibility(View.GONE);
                relLayout.setVisibility(View.VISIBLE);
            }
            ArrayAdapter adap=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,new String []{"Choose your option"});
            adap.setDropDownViewResource(R.layout.spinner_item);
            spinType.setAdapter(adap);
            back.setVisibility(View.GONE);
            title.setText(language_data.getString("labelServiceRequestTitle"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addComment(View v){
        try {
            String a="";
            String comment = etComment.getText().toString();
            String ticketId=SharedPrefNames.TICKET_ID;
            if(comment.length()==0){
                a=langDataSerReq.getString("labelErrorComment");
                DataModel.showSnackBar(view,a);
            }else if (DataModel.Internetcheck(this)) {
                    DataModel.loading_box(this,true);
                    String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                    JSONObject ob=new JSONObject();
                    ob.put("comment",comment);
                    ob.put("ticket_id",ticketId);
                    JSONArray obj=new JSONArray();
                    for(int i=0;i<ids.size();i++){
                        JSONObject ob1=new JSONObject();
                        ob1.put("id",ids.get(i));
                        ob1.put("name","comment");
                        Log.e("print Id "," "+ids.get(i));
                        obj.put(ob1);
                    }
                    ob.put("resource",obj);
                    RestClientWithoutString.getApiService(this).addComment(header, new TypedString(ob.toString()), new Callback<ComentResponse>() {
                        @Override
                        public void success(ComentResponse response, Response response2) {
                            try{
                                etComment.setText("");
                               String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                                RestClientWithoutString.getApiService(getApplicationContext()).getTicket(SharedPrefNames.TICKET_ID, header, new Callback<GetTicketFull>() {
                                    @Override
                                    public void success(GetTicketFull getTicketFull, Response response) {
                                        DataModel.loading_box_stop();
                                        Ticket list=getTicketFull.getResponse().getTickets();
                                        if (list != null) {
                                            if(list.getComments().length>0){
                                                List<Comments> comentList=new ArrayList<>();
                                                for(int i=0;i<list.getComments().length;i++) {
                                                    comentList.add(i,list.getComments()[i]);
                                                }
                                                AdapterComments newAdap=new AdapterComments(getApplicationContext(),comentList);
                                                rcComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                                rcComments.setAdapter(newAdap);
                                                rcComments.setVisibility(View.VISIBLE);
                                                linComents.setVisibility(View.VISIBLE);
                                                names=new ArrayList<>();
                                                AdapterUploadItem adap=new AdapterUploadItem(getApplicationContext(),names,7);
                                                rcAttachExtra.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                                rcAttachExtra.setAdapter(adap);
                                            }
                                        }
                                    ids.clear();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        DataModel.loading_box_stop();
                                        String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                        DataModel.showSnackBarError(view,json);
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            DataModel.loading_box_stop();
                            String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                            DataModel.showSnackBarError(view,json);
                        }
                    });

            } else {
                a=langDataSerReq.getString("InternetMessage");
                DataModel.showSnackBar(view,a);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    }
