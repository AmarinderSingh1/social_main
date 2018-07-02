package com.wahhao.mfg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;
import com.wahhao.mfg.adapters.AdapterMenu;
import com.wahhao.mfg.adapters.AdapterUploadItem;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentAgentDetails;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentComnapyInfo;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentOtherInformation;
import com.wahhao.mfg.model.GetProvince;
import com.wahhao.mfg.model.GetSeller;
import com.wahhao.mfg.model.OnboardResponse;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.ApiSer;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.BaseActivity;
import com.wahhao.mfg.utils.DataModel;
import com.wahhao.mfg.utils.Download;
import com.wahhao.mfg.utils.FragNavController;
import com.wahhao.mfg.utils.SingleUploadBroadcastReceiver;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;
import retrofit2.Call;
import retrofit2.Retrofit;

public class ActivityBusinessDetails extends BaseActivity implements SingleUploadBroadcastReceiver.Delegate {

    public static AdapterUploadItem adapters;
    public static boolean proClick=false;

    List<String> list,list2,list3;
    Prefs sp;
    public static int tabPos;
    private TabLayout tabLayout;
    //This is our viewPager
    public static ViewPager viewPager;
    int fragCount;
    private View view;
    public static List<String> provinces;
    public static int[] provincesId;
    JSONObject language_data,langData;
    private String[] tabLabels;
    private Toolbar toolbar;
    LayoutInflater inflater;
    View alertLayout;
    AlertDialog.Builder alt;
    public static String aName,aPhone,aCode,signUri,contractUri,sealUri,agentname,agentphone,amc,at,aes,exp,osap;
    //CoordinatorLayout cLayout;
    public static String cName,cStreet,cPostalCode,cBRN,cProvince,cDistrict,cCity;
    String sProvince[],sDistrict[],sCity[];
    TextView title,tvContent,tvUplaod,tvAccept,tvCon,tvSeal,conDownload;
    ImageView contract,signature,seal;
    Button accept;
    CheckBox check;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    public static RecyclerView rcMenu,rcSeal,rcSign,rcContract;
    TextView allowed;
    List<String> menu;
    private AdapterMenu adapter1;
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
    public static FragNavController mNavController;
    String value="";
    ImageView drawerIcon;
    TextView menuName;
    String [] tabTitles;
    public static CircleImageView proImg;
    public ImageView imgFlag;
    public TextView language;


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ButterKnife.bind(this);
        FrameLayout frameLayout=findViewById(R.id.container);
        if(getIntent().hasExtra("value"))
        {
         value=getIntent().getStringExtra("value");
        }
        if(value.equalsIgnoreCase("sign"))
        {
            view=getLayoutInflater().inflate(R.layout.activity_sign_contract,null);
            frameLayout.addView(view);
        }else if(value.equalsIgnoreCase("business")){
            view=getLayoutInflater().inflate(R.layout.activity_businessdetails,null);
            frameLayout.addView(view);
        }
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
        bottomTabLayout.setEnabled(false);
        if (bottomTabLayout != null) {
            for (int i = 0; i < mTabIconsNonSelected.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(getTabView(i,0));
            }
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
    }
    private View getTabView(int position,int check) {


        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_bottom, null);
        android.widget.ImageView icon = (android.widget.ImageView) view.findViewById(R.id.tab_icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(tabTitles[position]);
        title.setTextColor(getResources().getColor(R.color.lighter_black));
        if(position==3){
            title.setTextColor(getResources().getColor(R.color.white));
        }
        icon.setImageDrawable(DataModel.setDrawableSelector(this,
                mTabIconsNonSelected[position],
                mTabIconsSelected[position]));
//        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        return view;
    }

    @SuppressLint("NewApi")
    void loadData() {
        try {
            ids=new ArrayList<>();
            ids.add(0,0);
            ids.add(1,0);
            ids.add(2,0);
            provinces = new ArrayList<String>();
            bottomTabLayout.getTabAt(3).select();
            bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View v=tab.getCustomView();
                    TextView tv=(TextView)v.findViewById(R.id.title);
                    tv.setTextColor(getResources().getColor(R.color.white));
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    tabPos = tab.getPosition();
                    sp.save(SharedPrefNames.TAB_CLICK,tabPos);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View v=tab.getCustomView();
                    TextView tv=(TextView)v.findViewById(R.id.title);
                    tv.setTextColor(getResources().getColor(R.color.lighter_black));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            sp = new Prefs(this);
            proImg=findViewById(R.id.profile_image);
            title = (TextView) findViewById(R.id.title);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            rcMenu = (RecyclerView) findViewById(R.id.rcViewMenu);
            GridLayoutManager lManager = new GridLayoutManager(this, 2);
            rcMenu.setLayoutManager(lManager);
            Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
            Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
            rcMenu.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider, 2));
            rcMenu.setAdapter(MainActivity.adapter);
            drawerIcon=(ImageView)findViewById(R.id.navBtn);
            menuName=(TextView)findViewById(R.id.menuTitle);
            imgFlag=(ImageView)findViewById(R.id.imgFlag);
            language=(TextView)findViewById(R.id.textLanguage);
            int lang=Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0);
            JSONObject l_data=new JSONObject();
            l_data = DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "leftNavigation");

            if(lang==1) {
                imgFlag.setImageResource(R.drawable.usa_flag);
                String a = l_data.getString("languageEnglish");
                language.setText(a);
            }else {
                imgFlag.setImageResource(R.drawable.china_flag);
                String a = l_data.getString("languageChinese");
                language.setText(a);
            }

            String url=sp.getString(SharedPrefNames.PROFILE_PIC);
            if(url!=null){
                if(url.length()>0) {
                    Picasso.get().load(url)
                            .into(proImg);
                }else{
                    proImg.setImageResource(R.drawable.pro_icon);
                }
            }
            drawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
            });
            String name=sp.getString(SharedPrefNames.FULL_NAME);
            if(name.length()>0){
                menuName.setText(name);
            }
            if (value.equalsIgnoreCase("sign")) {
                list=new ArrayList<>();
                list2=new ArrayList<>();
                list3=new ArrayList<>();
                rcContract=(RecyclerView)findViewById(R.id.recContract);
                rcSeal=(RecyclerView)findViewById(R.id.recSeal);
                rcSign=(RecyclerView)findViewById(R.id.recSignature);
                tvAccept=(TextView)findViewById(R.id.textAccept);
                tvContent=(TextView)findViewById(R.id.textViewContent);
                tvUplaod=(TextView)findViewById(R.id.textUploadSign);
                tvCon=(TextView)findViewById(R.id.textUploadContract);
                tvSeal=(TextView)findViewById(R.id.textUploadSeal);
                accept=(Button)findViewById(R.id.uploadSignBtn);
                check=(CheckBox)findViewById(R.id.checkbox);
                if(check.isChecked()){
                    check.setButtonTintList(this.getColorStateList(R.color.theme_color));
                }else{
                    check.setButtonTintList(this.getColorStateList(R.color.btnText));
                }
                contract=(ImageView)findViewById(R.id.uploadContract);
                signature=(ImageView)findViewById(R.id.uploadSign);
                seal=(ImageView)findViewById(R.id.uploadSeal);
                allowed=(TextView)findViewById(R.id.allowed);
                conDownload=(TextView)view.findViewById(R.id.textDownloadContract);
                language_data = DataModel.
                        setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                this, "onboardContract");
                String a=language_data.getString("labelConntractTitle");
                title.setText(a);
                a=language_data.getString("labelUploadSign");
                tvUplaod.setText(a);
                a=language_data.getString("labelUpploadCheckbox");
                tvAccept.setText(a);
                a=language_data.getString("lableUploadContract");
                tvCon.setText(a);
                a=language_data.getString("lableUploadSeal");
                tvSeal.setText(a);
                a=language_data.getString("labelAcceptBtn");
                accept.setText(a);
                a=language_data.getString("lableAllowed");
                allowed.setText(a+allowed.getText().toString());


                contract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        if (ContextCompat.checkSelfPermission(ActivityBusinessDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
                        }else {
                            intent.setType("*/*");
                            startActivityForResult(intent, 3);
                        }
                    }
                });
                signature.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        if (ContextCompat.checkSelfPermission(ActivityBusinessDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);
                        }else {
                            intent.setType("*/*");
                            startActivityForResult(intent, 5);
                        }
                    }
                });
                seal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        if (ContextCompat.checkSelfPermission(ActivityBusinessDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},4);
                        }else {
                            intent.setType("*/*");
                            startActivityForResult(intent, 4);
                        }
                    }
                });
                conDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            /*URL u = new URL("http://wahhao.apis.com/uploads/products/assets/ship_detail_1527161658.pdf");
                            File outputFile=new File("Contract.pdf");
                            URLConnection conn = u.openConnection();
                            int contentLength = conn.getContentLength();

                            DataInputStream stream = new DataInputStream(u.openStream());

                            byte[] buffer = new byte[contentLength];
                            stream.readFully(buffer);
                            stream.close();
                            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
                            fos.write(buffer);
                            fos.flush();
                            fos.close();*/

                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                    }
                });

                list=new ArrayList<>();
                list2=new ArrayList<>();
                list3=new ArrayList<>();
                rcContract.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                adapters=new AdapterUploadItem(this,list,3);
                rcContract.setAdapter(adapters);

                rcSeal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                adapters=new AdapterUploadItem(this,list2,4);
                rcSeal.setAdapter(adapters);

                rcSign.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                adapters=new AdapterUploadItem(this,list3,5);
                rcSign.setAdapter(adapters);
            }
            if (value.equalsIgnoreCase("business")) {

                language_data = DataModel.
                        setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                this, "onboardBusinessDetails");
                tabLabels = new String[]{language_data.getString("labelComapnyInfoHeading"),
                        language_data.getString("labelAgentHeading"),
                        language_data.getString("labelOtherInfoHeading")};
                title.setText(language_data.getString("labelBusinessTitle"));
                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                adapter.addFragment(new FragmentComnapyInfo(), tabLabels[0]);
                adapter.addFragment(new FragmentAgentDetails(), tabLabels[1]);
                adapter.addFragment(new FragmentOtherInformation(), tabLabels[2]);
                viewPager = (ViewPager) findViewById(R.id.pager1);
                viewPager.setAdapter(adapter);
                viewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout1);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFC4C3"));
                tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
                tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#ffffff"));

                if (DataModel.Internetcheck(this)) {
                    DataModel.loading_box(this, true);
                    final String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                            String sellerId = sp.getString(SharedPrefNames.SELLER_ID);
                            RestClientWithoutString.getApiService(getApplicationContext()).getSeller(sellerId, header, new Callback<GetSeller>() {
                                @Override
                                public void success(final GetSeller getSeller, Response response) {
                                    try {
                                        DataModel.loading_box_stop();
                                        cName = getSeller.getResponse().getDetails().getUser().getC_name();
                                        cStreet = getSeller.getResponse().getDetails().getUser().getStreet();
                                        cPostalCode = getSeller.getResponse().getDetails().getUser().getPostal_code();
                                        cBRN = getSeller.getResponse().getDetails().getUser().getBusiness_registration_number();
                                        cProvince = getSeller.getResponse().getDetails().getUser().getProvince();
                                        cDistrict = getSeller.getResponse().getDetails().getUser().getDistrict();
                                        cCity = getSeller.getResponse().getDetails().getUser().getCity();
                                        aName = getSeller.getResponse().getDetails().getUser().getRegistration_agent();
                                        aPhone = getSeller.getResponse().getDetails().getUser().getRegistration_agent_phone();
                                        aCode = getSeller.getResponse().getDetails().getUser().getRegistration_agent_phone_area_code();
                                        amc = getSeller.getResponse().getDetails().getUser().getYear_annual_manufacturing_capacity();
                                        at = getSeller.getResponse().getDetails().getUser().getYear_annual_turnover();
                                        aes = getSeller.getResponse().getDetails().getUser().getYear_exports();
                                        exp = getSeller.getResponse().getDetails().getUser().getExperience_in_us_market();
                                        osap = getSeller.getResponse().getDetails().getUser().getOther_info();
                                        if (cName != null && cBRN != null && cStreet != null && cPostalCode != null) {
                                            FragmentComnapyInfo.etName.setText(cName);
                                            FragmentComnapyInfo.etStNum.setText(cStreet);
                                            FragmentComnapyInfo.etBRN.setText(cBRN);
                                            FragmentComnapyInfo.etPCode.setText(cPostalCode);
                                        }
                                        if (aName != null) {
                                            if (aName.length() > 0) {
                                                FragmentAgentDetails.etagentName.setText(aName);
                                                FragmentAgentDetails.etagentPhone.setText(aPhone);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @SuppressLint("NewApi")
                                @Override
                                public void failure(RetrofitError error) {
                                    DataModel.loading_box_stop();
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    DataModel.showSnackBarError(view,json);
                                }
                            });
                        }
                        else {
                    String a=language_data.getString("InternetMessage");
                    DataModel.showSnackBar(view,a);
                }

                }

            } catch(Exception e){

            }


    }

    private void initDownload(){
/*
* http://data.nvish.com/assets/contract-format.pdf
http://uat-data.nvish.com/assets/contract-format.pdf
* */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://data.nvish.com")
                .build();

        ApiSer retrofitInterface = retrofit.create(ApiSer.class);

        Call<ResponseBody> request = retrofitInterface.downloadFile();
        try {

            downloadFile(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }
    private int totalFileSize;

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.zip");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                //sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        //onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

        Log.e("Download"," Complete");


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
                        Intent intent=new Intent(ActivityBusinessDetails.this,ActivityLogin.class);
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
        /*try {
            initDownload();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public void serviceRequest(View view){

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
                    Intent intent=new Intent(ActivityBusinessDetails.this,ActivityBusinessDetails.class);
                    intent.putExtra("value",value);
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
                    Intent intent=new Intent(ActivityBusinessDetails.this,ActivityBusinessDetails.class);
                    intent.putExtra("value",value);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                }
            });

        }catch (Exception e){

        }
    }


    public void accept(final View view){
        try {
            if(rcContract.getAdapter()==null){
                String a=language_data.getString("lableContracttError");
                DataModel.showSnackBar(view,a);
            }else if(rcContract.getAdapter().getItemCount()==0){
                String a=language_data.getString("lableContracttError");
                DataModel.showSnackBar(view,a);
            }else if (!check.isChecked()) {
                String a=language_data.getString("lableTnC");
                DataModel.showSnackBar(view,a);
            } else if (DataModel.Internetcheck(ActivityBusinessDetails.this)) {
                String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                DataModel.loading_box(ActivityBusinessDetails.this, true);
                JSONObject object=new JSONObject();
                object.put("terms",1);
                JSONArray array=new JSONArray();
                for(int i=0;i<3;i++){
                    JSONObject oj=new JSONObject();
                    switch (i) {
                        case 0:
                            oj.put("name", "contract");
                            oj.put("id",ids.get(0));
                            break;
                        case 1:
                            oj.put("name", "signature");
                            oj.put("id",ids.get(2));

                            break;
                        case 2:
                            oj.put("name", "seal");
                            oj.put("id",ids.get(1));
                            break;

                    }
                    array.put(oj);
                }
                object.put("contract",array);
                RestClientWithoutString.getApiService(ActivityBusinessDetails.this).userContract(header,new TypedString(object.toString()), new Callback<Submit>() {
                    @Override
                    public void success(Submit submit, Response response) {
                        try {
                            String msg = language_data.getString("lableAddSending");
                            String head=language_data.getString("lableDear");
                            head=head+" "+sp.getString(SharedPrefNames.FULL_NAME);
                            head=head+"\n"+language_data.getString("lableCongrats");
                            alertSubmit(head,msg);
                            sp.save(SharedPrefNames.HAS_SIGNED_CONTRACT, true);
                            sp.save(SharedPrefNames.TAB_CLICK,3);
                            //MainActivity.switchTab(3);
                        }catch (Exception e){}
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void alertSubmit(String title,String message){
        try {
            LayoutInflater inflater= getLayoutInflater();
            View alertLayout=inflater.inflate(R.layout.alert_submit_details,null);
            TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
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
                    Intent intent=new Intent(ActivityBusinessDetails.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                uploadProfilePic(path,"");
                FragmentOtherInformation.upl=uri.toString();
                FragmentOtherInformation.list.add(name);
                FragmentOtherInformation.adapters=new AdapterUploadItem(this,FragmentOtherInformation.list,2);
                FragmentOtherInformation.horRecView.setAdapter(FragmentOtherInformation.adapters);
                DataModel.loading_box(this,true);
            }else{
                String a=language_data.getString("labelErrorFileFormat");
                DataModel.showSnackBar(view,a);
                }
            }catch (Exception e){}
            }
        }

        if(requestCode==3&&resultCode==RESULT_OK){
            if (resultData != null) {
                try{
                    count=3;
                    uri = resultData.getData();
                    if(uri.toString().startsWith("content")) {
                        path = getPath(uri);
                    }else{
                        path=uri.getPath();
                    }
                    list.add(0,"");
                String name=path.substring(path.lastIndexOf("/")+1,path.length());
                name=name.toLowerCase();
                if(name.endsWith("jpeg")||name.endsWith("jpg")||name.endsWith("png")||name.endsWith("doc")||name.endsWith("docx")
                        ||name.endsWith("pdf"))
                {
                uploadProfilePic(path,"/contract");
                list.set(0,path.substring(path.lastIndexOf("/")+1,path.length()));
                String obj = list.get(0); // remember first item
                list.clear(); // clear complete list
                list.add(obj);
                rcContract.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                adapters=new AdapterUploadItem(this,list,3);
                rcContract.setAdapter(adapters);
                    DataModel.loading_box(this,true);
                Log.e("Data", "Uri: " + uri.toString());
                }else{
                    String a=language_data.getString("labelErrorFileFormat");
                    DataModel.showSnackBar(view,a);
                }
            }catch (Exception e){}
            }
        }
        if(requestCode==4&&resultCode==RESULT_OK){
            if (resultData != null) {
               try{
                   count=4;
                   uri = resultData.getData();
                   if(uri.toString().startsWith("content")) {
                       path = getPath(uri);
                   }else{
                       path=uri.getPath();
                   }
                   list2.add(0,"");
                String name=path.substring(path.lastIndexOf("/")+1,path.length());
                   name=name.toLowerCase();
                if(name.endsWith("jpeg")||name.endsWith("jpg")||name.endsWith("png")||name.endsWith("doc")||name.endsWith("docx")
                        ||name.endsWith("pdf"))
                {
                uploadProfilePic(path,"/contract");
                list2.set(0,path.substring(path.lastIndexOf("/")+1,path.length()));
                String obj = list2.get(0); // remember first item
                list2.clear(); // clear complete list
                list2.add(obj);
                rcSeal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                adapters=new AdapterUploadItem(this,list2,4);
                rcSeal.setAdapter(adapters);
                    DataModel.loading_box(this,true);
                 Log.e("Data", "Uri: " + uri.toString());
                }else{
                    String a=language_data.getString("labelErrorFileFormat");
                    DataModel.showSnackBar(view,a);
                }
               }catch (Exception e){}
            }
        }
        if(requestCode==5&&resultCode==RESULT_OK){
            if (resultData != null) {
               try{
                   count=5;
                   uri = resultData.getData();
                   if(uri.toString().startsWith("content")) {
                       path = getPath(uri);
                   }else{
                       path=uri.getPath();
                   }
                   list3.add(0,"");
                   String name=path.substring(path.lastIndexOf("/")+1,path.length());
                   name=name.toLowerCase();
                   if(name.endsWith("jpeg")||name.endsWith("jpg")||name.endsWith("png")||name.endsWith("doc")||name.endsWith("docx")
                           ||name.endsWith("pdf"))
                   {
                uploadProfilePic(path,"/contract");
                list3.set(0,path.substring(path.lastIndexOf("/")+1,path.length()));
                String obj = list3.get(0); // remember first item
                list3.clear(); // clear complete list
                list3.add(obj);
                rcSign.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                adapters=new AdapterUploadItem(this,list3,5);
                rcSign.setAdapter(adapters);
                       DataModel.loading_box(this,true);
                Log.e("Data", "Uri: " + uri.toString());
            }else{
                String a=language_data.getString("labelErrorFileFormat");
                DataModel.showSnackBar(view,a);
            }
        }catch (Exception e){}
            }
        }

    }

    @SuppressLint("NewApi")
    public String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
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
            MultipartUploadRequest multipartUploadRequest=   new MultipartUploadRequest(this, uploadId, "http://apidb.nvish.com/public/sellers/resource"+str)
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
             FragmentOtherInformation.ids.add(id);
             break;
                 case 2:
                     break;
                 case 3:
                     ids.set(0,id);
                     break;
                 case 4:
                     ids.set(1,id);
                     break;
                 case 5:
                     ids.set(2,id);
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
            case 3: {
              if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  intent.setType("*/*");
                    startActivityForResult(intent, 3);
                } else {
                    Log.e(":hello ", " permission not granted");
                }
            }
            break;
            case 4: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 4);
                } else {
                    Log.e(":hello ", " permission not granted");
                }
            }
            break;
            case 5: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 5);
                } else {
                    Log.e(":hello ", " permission not granted");
                }
                break;
            }
        }
    }


}
