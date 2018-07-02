package com.wahhao.mfg;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.squareup.picasso.Picasso;
import com.wahhao.mfg.adapters.AdapterMenu;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.fragments.FragmentAddProduct;
import com.wahhao.mfg.fragments.FragmentHome;
import com.wahhao.mfg.fragments.FragmentNotification;
import com.wahhao.mfg.fragments.FragmentOnboarding;
import com.wahhao.mfg.fragments.FragmentProduct;
import com.wahhao.mfg.fragments.FragmentProfile;
import com.wahhao.mfg.fragments.fragment_products.FragmentSavedProduct;
import com.wahhao.mfg.model.OnboardResponse;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.model.basic_response;
import com.wahhao.mfg.network.ApiSer;
import com.wahhao.mfg.network.ApiService;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.BaseActivity;
import com.wahhao.mfg.utils.DataModel;
import com.wahhao.mfg.utils.Download;
import com.wahhao.mfg.utils.FragNavController;
import com.wahhao.mfg.utils.FragmentHistory;


import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation,
        FragNavController.TransactionListener, FragNavController.RottfragmentListener {

    public static String onBoarding="";
    public static boolean hasContract=false;
    public static boolean proClick=false;

    @BindView(R.id.container)
    FrameLayout contentFrame;
    String titles[];
    RecyclerView rcMenu;
    public static List<String> menu;
    TextView tvSerReq,tvSettings,tvSignOut;
    Prefs sp;
    String [] tabTitles;

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




    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;
    public View view;
    public static ImageView imgProfile;
    public static TextView menuTitle;
    public static FragNavController mNavController;

    public  static CircleImageView profilePic;
    public static FragmentHistory fragmentHistory;
    public static Toolbar toolbar;
    public static TextView toolTitle;
    public static JSONObject language_data;
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public View navHeader;
    public ImageView imageView;
    public static ImageView backArrow;
    public static AdapterMenu adapter;
    public ImageView imgFlag;
    public TextView language;
    public static int lang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rottfragmentListener(this, mTabIconsNonSelected.length)
                .build();
        init();


    }

    public void loadData() {
        try {
            lang=Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0);
            language_data=new JSONObject();
            language_data = DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "rightNavigation");
            titles = new String[]{language_data.getString("labelServiceRequests"),
                    language_data.getString("labelSettings"),
                    language_data.getString("labelLogout")};
            tabTitles=new String[]{
                    language_data.getString("labelHome"),
                    language_data.getString("labelProducts"), language_data.getString("labelNotifications"),
                    language_data.getString("labelMyAccount")
            };
            tvSerReq.setText(titles[0]);
            tvSettings.setText(titles[1]);
            tvSignOut.setText(titles[2]);
            menu=new ArrayList<>();
            String a=language_data.getString("labelOrders");
            menu.add(a);
            a=language_data.getString("labelInventory");
            menu.add(a);
            a=language_data.getString("labelWallet");
            menu.add(a);
            a=language_data.getString("labelPerformance");
            menu.add(a);
            a=language_data.getString("labelSales");
            menu.add(a);
            a=language_data.getString("labelCustomerFeedback");
            menu.add(a);
            a=language_data.getString("labelMessageCenter");
            menu.add(a);
            a=language_data.getString("labelLiveChat");
            menu.add(a);
            a=language_data.getString("labelUsername");
            menuTitle.setText(a);

            a=sp.getString(SharedPrefNames.FULL_NAME);
            if(a.length()>0){
                menuTitle.setText(a);
            }
            if(lang==1) {
                imgFlag.setImageResource(R.drawable.usa_flag);
                a = language_data.getString("languageEnglish");
                language.setText(a);
            }else {
                imgFlag.setImageResource(R.drawable.china_flag);
                a = language_data.getString("languageChinese");
                language.setText(a);
            }



        } catch (Exception e) {

        }
    }


    @SuppressLint("NewApi")
    public void init(){
    sp=new Prefs(this);
        hasContract=sp.getBoolean(SharedPrefNames.HAS_SIGNED_CONTRACT,false);
        onBoarding=sp.getString(SharedPrefNames.COMP_STAGE_KEY);
        proClick=false;
        view=findViewById(R.id.mainLayout);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        tvSerReq=(TextView)findViewById(R.id.textserreq);
    tvSettings=(TextView)findViewById(R.id.textsettings);
    tvSignOut=(TextView)findViewById(R.id.textsignout);
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    imgProfile=(ImageView)findViewById(R.id.imgProfile);
    menuTitle=(TextView)findViewById(R.id.menuTitle);
    toolbar=(Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolTitle=(TextView)findViewById(R.id.title);
    imgFlag=(ImageView)findViewById(R.id.imgFlag);
    language=(TextView)findViewById(R.id.textLanguage);
    imageView=(ImageView)findViewById(R.id.navBtn);
    backArrow=(ImageView)findViewById(R.id.navBackBtn);
    profilePic=findViewById(R.id.profile_image);
        String url=sp.getString(SharedPrefNames.PROFILE_PIC);
        if(url!=null){
            if(url.length()>0) {
                Picasso.get().load(url)
                        .into(profilePic);
            }else{
                profilePic.setImageResource(R.drawable.pro_icon);
            }
        }
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawer.openDrawer(Gravity.RIGHT);
        }
    });
    loadData();
     initTab();
     rcMenu=(RecyclerView)findViewById(R.id.rcViewMenu);
    GridLayoutManager lManager=new GridLayoutManager(this,2);
    rcMenu.setLayoutManager(lManager);
        Drawable horizontalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
        rcMenu.addItemDecoration(new GridDividerItemDecoration(horizontalDivider, verticalDivider, 2));
    adapter=new AdapterMenu(this,menu);
    rcMenu.setAdapter(adapter);
    fragmentHistory = new FragmentHistory();


    bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            View v=tab.getCustomView();
            TextView tv=(TextView)v.findViewById(R.id.title);
            tv.setTextColor(getResources().getColor(R.color.white));
            backArrow.setVisibility(View.GONE);
            fragmentHistory.push(tab.getPosition());
            switchTab(tab.getPosition());
            //mNavController.replaceStackOnboard(null);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            View v=tab.getCustomView();
            TextView tv=(TextView)v.findViewById(R.id.title);
            tv.setTextColor(getResources().getColor(R.color.lighter_black));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            backArrow.setVisibility(View.GONE);
            mNavController.clearStack();
            switchTab(tab.getPosition());
                mNavController.replaceStackOnboard(null);
        }
    });
        if(hasContract) {
            switchTab(0);
        }else{
            bottomTabLayout.getTabAt(3).select();
        }

}

    private void initTab() {

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
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(tabTitles[position]);
        title.setTextColor(getResources().getColor(R.color.lighter_black));
        if(hasContract&&position==0){
           title.setTextColor(getResources().getColor(R.color.white));
        }
        icon.setImageDrawable(DataModel.setDrawableSelector(MainActivity.this,
                mTabIconsNonSelected[position],
                mTabIconsSelected[position]));
        //tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {

        super.onStop();
    }


    public void loadTab(){
        try{
            JSONObject obj=new JSONObject();
            obj=DataModel.
                    setLanguage(Prefs.with(this).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            this, "rightNavigation");
            tabTitles=new String[]{
                    obj.getString("labelHome"),
                    obj.getString("labelProducts"), obj.getString("labelNotifications"),
                    obj.getString("labelMyAccount")};
            for(int i=0;i<mTabIconsNonSelected.length;i++){
                View v=bottomTabLayout.getTabAt(i).getCustomView();
                TextView title = (TextView) v.findViewById(R.id.title);
                title.setText(tabTitles[i]);
            }
            }catch(Exception ex){}
    }


    public void onResume(){
        super.onResume();
            loadTab();
            String url=sp.getString(SharedPrefNames.PROFILE_PIC);
            if(url!=null){
                if(url.length()>0) {
                    Picasso.get().load(url)
                            .into(profilePic);
                }else{
                    profilePic.setImageResource(R.drawable.pro_icon);
                }
            }

            int i = sp.getInt(SharedPrefNames.TAB_CLICK);
            if (i > 0) {
                bottomTabLayout.getTabAt(i).select();
                sp.delete(SharedPrefNames.TAB_CLICK);
            } else if (i == 0) {
                bottomTabLayout.getTabAt(0).select();
                sp.delete(SharedPrefNames.TAB_CLICK);
            } else {
                if (hasContract) {
                    if (FragmentProduct.fragCount == 0) {
                        bottomTabLayout.getTabAt(0).select();
                    }
                } else {
                    bottomTabLayout.getTabAt(3).select();
                }
                sp.delete(SharedPrefNames.TAB_CLICK);
            }
    }

    public void onDestroy(){
        super.onDestroy();
        sp.delete(SharedPrefNames.TAB_CLICK);
    }

    public static void switchTab(int position) {
        mNavController.switchTab(position);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (!mNavController.isRottfragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                } else {

                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }

    public void fragmentBack(View v) {

        if (!mNavController.isRottfragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                } else {

                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }

    private void updateTabSelection(int currentTab){

        for (int i = 0; i <  mTabIconsNonSelected.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
            if(currentTab != i) {
                selectedTab.getCustomView().setSelected(false);
            }else{
                selectedTab.getCustomView().setSelected(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }




    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {

        }
    }



    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {
             }
    }

    @Override
    public Fragment getRottfragment(int index) {
        switch (index) {
            case FragNavController.TAB1:
                return new FragmentHome();
            case FragNavController.TAB2:
                 return new FragmentProduct();
            case FragNavController.TAB3:
                return new FragmentNotification();
            case FragNavController.TAB4:
                getDetails();
                hasContract=sp.getBoolean(SharedPrefNames.HAS_SIGNED_CONTRACT,false);
                onBoarding=sp.getString(SharedPrefNames.COMP_STAGE_KEY);
                if(hasContract){
                    return new FragmentProfile();
                }
                else {
                    if(proClick){
                        proClick=false;
                        return new FragmentProfile();
                    }
                    else {
                        return new FragmentOnboarding();
                    }
                }

        }
        throw new IllegalStateException("Need to send an index that we know");
    }


    public void businessDetails(View view){
        Intent intent=new Intent(this,ActivityBusinessDetails.class);
        intent.putExtra("value","business");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void signContact(View view){
        Intent intent=new Intent(this,ActivityBusinessDetails.class);
        intent.putExtra("value","sign");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    public void getDetails() {
        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
        RestClientWithoutString.getApiService(this).getDropDown(header, new Callback<OnboardResponse>() {
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


    public void profileClick(View view){
        proClick=true;
        drawer.closeDrawer(Gravity.RIGHT);
        if(hasContract) {
            bottomTabLayout.getTabAt(3).select();
            switchTab(3);
        }else{
            if(bottomTabLayout.getSelectedTabPosition()==3) {
                mNavController.replaceStackOnboard(null);
            }else{
                bottomTabLayout.getTabAt(3).select();
                mNavController.replaceStackOnboard(null);
            }
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
                        Intent intent=new Intent(MainActivity.this,ActivityLogin.class);
                        intent.putExtra("logout","true");
                        sp.delete(SharedPrefNames.ACCESS_TOKEN);
                        sp.delete(SharedPrefNames.COMP_STAGE_KEY);
                        sp.delete(SharedPrefNames.PROFILE_PIC);
                        sp.delete(SharedPrefNames.HAS_SIGNED_CONTRACT);
                        sp.delete(SharedPrefNames.FULL_NAME);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        finish();
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
        try{
           // initDownload();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void serviceRequest(View view){
        drawer.closeDrawer(Gravity.RIGHT);
        Intent intent=new Intent(getApplicationContext(),ActivityServiceRequest.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }



    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Contract.pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.e("Hello ", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
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
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

                }
            });
            chinaLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prefs.with(getApplicationContext()).save(SharedPrefNames.SELCTED_LANGUAGE,2);
                    alert.dismiss();
                    drawer.closeDrawer(Gravity.RIGHT);
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

                }
            });

       }catch (Exception e){

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

}
