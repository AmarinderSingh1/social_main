package com.wahhao.mfg.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wahhao.mfg.ActivityBusinessDetails;
import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterSendSample;
import com.wahhao.mfg.adapters.AdapterUploadItem;
import com.wahhao.mfg.adapters.AdaptorSavedProduct;
import com.wahhao.mfg.beans.SampleBean;
import com.wahhao.mfg.beans.SavedProducts;
import com.wahhao.mfg.fragments.fragment_business_details.FragmentOtherInformation;
import com.wahhao.mfg.fragments.fragment_home.FragmentInventory;
import com.wahhao.mfg.fragments.fragment_home.FragmentOrders;
import com.wahhao.mfg.fragments.fragment_home.FragmentSales;
import com.wahhao.mfg.fragments.fragment_home.FragmentWallet;
import com.wahhao.mfg.fragments.fragment_products.FragmentApproved;
import com.wahhao.mfg.fragments.fragment_products.FragmentDisapproved;
import com.wahhao.mfg.fragments.fragment_products.FragmentInReview;
import com.wahhao.mfg.fragments.fragment_products.FragmentLive;
import com.wahhao.mfg.fragments.fragment_products.FragmentSavedProduct;
import com.wahhao.mfg.model.GetProduct;
import com.wahhao.mfg.model.GetSample;
import com.wahhao.mfg.model.SavedProductList;
import com.wahhao.mfg.model.Submit;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;
import com.wahhao.mfg.utils.SingleUploadBroadcastReceiver;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;


public class FragmentProduct extends BaseFragment implements SingleUploadBroadcastReceiver.Delegate {


    public static CoordinatorLayout sendSampleLayout;
    public static int fragCount=0,weightSum=0,quantitySum=0;
    public static String catId="";
    public static Prefs sp;
    public static View view;
    public static String error="";
    private Toolbar toolbar;
    public static CoordinatorLayout relAddProduct;
    public static TextInputLayout pName,pHSCode,pMCost,pSp,pCMar,pDSp,pAdd;
    public static EditText etProName,etHsCode,etManCost,etSp,etDSp,etCMar,etAdd;
    public static TextView pCategory,allowed,uploadHead,message,worksheetName,txtQuantity,txtWeight,txtQuantityValue,txtWeightValue;
    public static Button saveBtn,continueBtn;
    public static Spinner spin;
    public static ArrayAdapter adap;
    public static ImageView upload,uploadPlus;
    public static String proName,proHsCode,proManCost,proCsp,proDsp,proCmar,proAdd;
    private final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();
    public static List<String> list;
    public static List<String> delList,delIds;
    public static List<Integer> ids;
    public static int workId=0;

    //This is our viewPager
    public static ViewPager viewPager;
    public static TabLayout tabLayout;
    JSONObject language_data,language_data1,language_data2;
    private  String[] tabLabels;
    public static String[] items={"Choose your option"};
    public static RecyclerView flyRecView,sendSampleRec;
    public static AdapterUploadItem adapters;
    public static AdapterSendSample adapter2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        loadData(view);
        return view;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {
        super.onResume();
        uploadReceiver.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        uploadReceiver.unregister(getActivity());
    }

    void loadData(final View view){
        try {
            //fragCount=0;
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return false;
                }
            });
            MainActivity.backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        FragmentSavedProduct.language_data=new JSONObject();
                        FragmentProduct.saveBtn.setText(language_data1.getString("labelSaveButton"));
                        FragmentSavedProduct.language_data=DataModel.
                                setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                        getActivity(), "products");
                        FragmentProduct.viewPager.setVisibility(View.VISIBLE);
                        FragmentProduct.tabLayout.setVisibility(View.VISIBLE);
                        FragmentProduct.relAddProduct.setVisibility(View.GONE);
                        FragmentProduct.sendSampleLayout.setVisibility(View.GONE);
                        MainActivity.backArrow.setVisibility(View.GONE);
                        MainActivity.toolTitle.setText(FragmentSavedProduct.language_data.getString("labelProductHeading"));
                        FragmentSavedProduct.adapSaved=new AdaptorSavedProduct(getContext(),SharedPrefNames.savedProductList,language_data,1);
                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        FragmentSavedProduct.listSavedData.setLayoutManager(llm);
                        FragmentSavedProduct.listSavedData.setAdapter(FragmentSavedProduct.adapSaved);
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    }catch (Exception e){}
                }
            });
            weightSum=0;
            quantitySum=0;
            sp=new Prefs(getActivity());
            ids=new ArrayList<>();
            list=new ArrayList<>();
            delIds=new ArrayList<>();
            delList=new ArrayList<>();
            language_data2= DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "sendSamples");
            language_data1= DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "addProducts");
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "products");
            tabLabels=new String[]{language_data.getString("labelSavedTitle"),
                    language_data.getString("labelReviewTitle"),
                    language_data.getString("labelApprovedTitle"),
                    language_data.getString("labelLiveTitle"),
                     language_data.getString("labelDisapprovedTitle")};
            String a=language_data.getString("labelProductHeading");
            MainActivity.toolTitle.setText(a);
        relAddProduct=(CoordinatorLayout)view.findViewById(R.id.rel1);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentSavedProduct(), tabLabels[0]);
        adapter.addFragment(new FragmentInReview(), tabLabels[1]);
        adapter.addFragment(new FragmentApproved(), tabLabels[2]);
        adapter.addFragment(new FragmentLive(), tabLabels[3]);
        adapter.addFragment(new FragmentDisapproved(), tabLabels[4]);
        viewPager=(ViewPager)view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout)view. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFC4C3"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#ffffff"));

        sendSampleLayout=(CoordinatorLayout)view.findViewById(R.id.rel2);
        sendSampleRec=(RecyclerView)view.findViewById(R.id.sendRecView);

        continueBtn=(Button)view.findViewById(R.id.sendSampleBtn);
        txtQuantity=(TextView)view.findViewById(R.id.textQuantity);
        txtQuantityValue=(TextView)view.findViewById(R.id.textQuantityValue);
        txtWeight=(TextView)view.findViewById(R.id.textWeight);
        txtWeightValue=(TextView)view.findViewById(R.id.textWeightValue);

            pName=(TextInputLayout)view.findViewById(R.id.pnameInput);
            pHSCode=(TextInputLayout)view.findViewById(R.id.hsCodeInput);
            pMCost=(TextInputLayout)view.findViewById(R.id.manCostInput);
            pSp=(TextInputLayout)view.findViewById(R.id.spInput);
            pDSp=(TextInputLayout)view.findViewById(R.id.DspInput);
            pCMar=(TextInputLayout)view.findViewById(R.id.cMarginInput);
            pAdd=(TextInputLayout)view.findViewById(R.id.AddInput);

            uploadPlus=(ImageView)view.findViewById(R.id.uploadPlus);
            upload=(ImageView)view.findViewById(R.id.upload);

            etProName=(EditText)view.findViewById(R.id.editPname);
            etHsCode=(EditText)view.findViewById(R.id.editHS);
            etManCost=(EditText)view.findViewById(R.id.editManCost);
            etSp=(EditText)view.findViewById(R.id.editSP);
            etDSp=(EditText)view.findViewById(R.id.editDSP);
            etCMar=(EditText)view.findViewById(R.id.editMargin);
            etAdd=(EditText)view.findViewById(R.id.editAdd);

            pCategory=(TextView)view.findViewById(R.id.textProCat);
            allowed=(TextView)view.findViewById(R.id.textFormat);
            uploadHead=(TextView)view.findViewById(R.id.textUplaodDoc);
            message=(TextView)view.findViewById(R.id.textWarning);
            worksheetName=(TextView)view.findViewById(R.id.imgName);

            saveBtn=(Button)view.findViewById(R.id.saveBtn);

            spin=(Spinner)view.findViewById(R.id.spinnerProCat);
            adap = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
            adap.setDropDownViewResource(R.layout.spinner_item);

            flyRecView=(RecyclerView)view.findViewById(R.id.flyRecView);
            flyRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
            adapters=new AdapterUploadItem(getActivity(),list,1);
            flyRecView.setAdapter(adapters);

            etManCost.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str=etSp.getText().toString();
                    int i,j,k;
                    if(str.length()>0) {
                        i=Integer.parseInt(str);
                        str=etManCost.getText().toString();
                        if(str.length()>0) {
                            j = Integer.parseInt(str);
                            k = i-j;
                            if (k > 0) {
                                etCMar.setText(k+"");
                            }else{
                                etCMar.setText("0");
                            }
                        }else{
                            etCMar.setText("0");
                        }
                    }/*else{
                        etCMar.setText("0");
                    }*/
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            etSp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str=etManCost.getText().toString();
                    int i,j,k;
                    if(str.length()>0) {
                        i=Integer.parseInt(str);
                        str=etSp.getText().toString();
                        if(str.length()>0) {
                            j = Integer.parseInt(str);
                            k =   j-i;
                            if (k > 0) {
                                etCMar.setText(k+"");
                            }else{
                                etCMar.setText("0");
                            }
                        }else{
                            etCMar.setText("0");
                        }
                    }/*else{
                        etCMar.setText("0");
                    }*/
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            a="";
            a=language_data1.getString("labelProdCategory");
            pCategory.setText(a);
            a=language_data1.getString("labelProdName");
            pName.setHint(a);
            a=language_data1.getString("labelHSCode");
            pHSCode.setHint(a);
            a=language_data1.getString("labelManfCost");
            pMCost.setHint(a);
            a=language_data1.getString("labelCurrentPrice");
            pSp.setHint(a);
            a=language_data1.getString("labelCurrentMargin");
            pCMar.setHint(a);
            a=language_data1.getString("labelDesiredPrice");
            pDSp.setHint(a);
            a=language_data1.getString("lableAdditionalInformation");
            pAdd.setHint(a);
            a=language_data1.getString("labelUploadDocuments");
            uploadHead.setText(a);
            a=language_data1.getString("labelUploadText");
            message.setText(a);
            a=language_data1.getString("lableAllowed");
            allowed.setText(a+" "+allowed.getText().toString());
            a=language_data1.getString("labelSaveButton");
            saveBtn.setText(a);
            a=language_data1.getString("labelAttachSheet");
            worksheetName.setText(a);

            a=language_data2.getString("labelContinueBtn");
            continueBtn.setText(a);
            a=language_data2.getString("labelTotalQuantity");
            txtQuantity.setText(a);
            a=language_data2.getString("labelTotalWeight");
            txtWeight.setText(a);

            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    catId=SharedPrefNames.ids.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            uploadPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    //Intent intent=new Intent(Intent.ACTION_PICK);
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 2);
                    }
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    //Intent intent=new Intent(Intent.ACTION_PICK);
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, 1);
                        //startActivityForResult(intent, 1);
                    }
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try{
                       InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                       proName=etProName.getText().toString();
                    proHsCode=etHsCode.getText().toString();
                    proManCost=etManCost.getText().toString();
                    proCsp=etSp.getText().toString();
                    proCmar=etCMar.getText().toString();
                    proDsp=etDSp.getText().toString();
                    proAdd=etAdd.getText().toString();
                    String a="";
                    if(spin.getSelectedItem().toString().equalsIgnoreCase("Choose your option"))
                    {
                        a=language_data1.getString("labelCategoryErr");
                        DataModel.showSnackBar(view,a);
                    }else if(proName.length()==0){
                        a=language_data1.getString("labelNameErr");
                        DataModel.showSnackBar(view,a);
                    }else if(proHsCode.length()==0){
                        a=language_data1.getString("labelHScodeErr");
                        DataModel.showSnackBar(view,a);
                    }else if(proManCost.length()==0){
                        a=language_data1.getString("labelManfCostErr");
                        DataModel.showSnackBar(view,a);
                    }else if(proCsp.length()==0){
                        a=language_data1.getString("labelSPErr");
                        DataModel.showSnackBar(view,a);
                    }else if(proCmar.length()==0){
                        a=language_data1.getString("labelMarginErr");
                        DataModel.showSnackBar(view,a);
                    }else if(proDsp.length()==0){
                        a=language_data1.getString("labelDesiredSPErr");
                        DataModel.showSnackBar(view,a);
                    }else if(fragCount==0){
                        a=language_data1.getString("labelResourcesError");
                        DataModel.showSnackBar(view,a);
                    }else if(flyRecView.getAdapter().getItemCount()==0){
                        a=language_data1.getString("labelResourcesError");
                        DataModel.showSnackBar(view,a);
                    }else if(DataModel.Internetcheck(getActivity())){
                        DataModel.loading_box(getActivity(),true);
                        JSONObject obj=new JSONObject();

                        obj.put("category_id",catId);
                        obj.put("current_margin_per_unit",proCmar);
                        obj.put("current_selling_price",proCsp);
                        obj.put("desired_selling_price",proDsp);
                        obj.put("manufacturing_cost",proManCost);
                        obj.put("name",proName);
                        obj.put("sub_category_id","");
                        obj.put("hs_code",proHsCode);
                        JSONArray ob=new JSONArray();
                        JSONObject ob1=new JSONObject();
                        ob1.put("id",workId);
                        ob1.put("name","worksheet");
                        ob.put(ob1);
                        for(int i=0;i<ids.size();i++)
                        {
                            JSONObject ob2=new JSONObject();
                            ob2.put("id",ids.get(i));
                            ob2.put("name","document");
                            ob.put(ob2);
                        }
                        if(fragCount==10){
                            String abc="";
                            for(int i=0;i<delIds.size();i++)
                            {
                                if(i==delList.size()-1){
                                    abc = abc +"";
                                }else {
                                    abc = abc + delIds.get(i) + ",";
                                }
                            }
                            obj.put("resource_del",abc);
                        }

                        obj.put("resource",ob);
                        obj.put("description",proAdd);
                        if(fragCount==10){
                            String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                            RestClientWithoutString.getApiService(getActivity()).updateProduct(SharedPrefNames.PRODUCT_ID,header, new TypedString(obj.toString()), new Callback<GetProduct>() {
                                @Override
                                public void success(GetProduct submit, Response response) {
                                    try {
                                        DataModel.loading_box_stop();
                                        String head = language_data1.getString("labelUpdateProduct");
                                        String message = language_data1.getString("labelUpdateSuccess");
                                        workId = 0;
                                        ids.clear();
                                        alertSubmit(head, message, 1);
                                    /*List<SavedProducts> list=SharedPrefNames.getProductList(getContext(),sp,view);
                                    FragmentSavedProduct.adapSaved=new AdaptorSavedProduct(getContext(),list,language_data,1);
                                    FragmentSavedProduct.listSavedData.setAdapter(FragmentSavedProduct.adapSaved);*/
                                        //DataModel.showSnackBar(view, "Success");
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    DataModel.loading_box_stop();
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    DataModel.showSnackBarError(view, json);
                                }
                            });
                        }else {
                            String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                            RestClientWithoutString.getApiService(getActivity()).submitProduct(header, new TypedString(obj.toString()), new Callback<GetProduct>() {
                                @Override
                                public void success(GetProduct submit, Response response) {
                                    try {
                                        DataModel.loading_box_stop();
                                        String head = language_data1.getString("labelAddProduct");
                                        String message = language_data1.getString("labelAddSuccess");
                                        workId = 0;
                                        ids.clear();
                                        alertSubmit(head, message, 1);
                                    /*List<SavedProducts> list=SharedPrefNames.getProductList(getContext(),sp,view);
                                    FragmentSavedProduct.adapSaved=new AdaptorSavedProduct(getContext(),list,language_data,1);
                                    FragmentSavedProduct.listSavedData.setAdapter(FragmentSavedProduct.adapSaved);*/
                                        //DataModel.showSnackBar(view, "Success");
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    DataModel.loading_box_stop();
                                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                    DataModel.showSnackBarError(view, json);
                                }
                            });
                        }
                    } else{
                        a = language_data1.getString("InternetMessage");
                        DataModel.showSnackBar(view,a);
                    }

                    }catch (Exception e){}
                }
            });

            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        String a = "";
                        int m=0;
                        JSONObject obj=new JSONObject();
                        JSONArray arr=new JSONArray();
                        for (int i = 0; i < AdapterSendSample.sampleList.size(); i++) {
                            SampleBean sb = AdapterSendSample.sampleList.get(i);
                            if(sb.getQuantity()==null){
                                a = language_data2.getString("labelQuantityError");
                                DataModel.showSnackBar(view,a);
                                break;
                            }else if (sb.getQuantity().length() == 0||sb.getQuantity().equalsIgnoreCase("0")) {
                                a = language_data2.getString("labelQuantityError");
                                DataModel.showSnackBar(view,a);
                                break;
                            } else if (sb.getWeight()==null){
                                a = language_data2.getString("labelWeightError");
                                DataModel.showSnackBar(view,a);
                                break;
                            }else if (sb.getWeight().length() == 0||sb.getWeight().equalsIgnoreCase("0")) {
                                a = language_data2.getString("labelWeightError");
                                DataModel.showSnackBar(view,a);
                                break;
                            }  else if (sb.getShippingName() == null) {
                                a = language_data2.getString("labelShippingCompanyError");
                                DataModel.showSnackBar(view,a);
                                break;
                            }else if (sb.getShippingName().length() == 0) {
                                a = language_data2.getString("labelShippingCompanyError");
                                DataModel.showSnackBar(view,a);
                                break;
                            }else if (sb.getTrackingCode() ==null) {
                                a = language_data2.getString("labelTrackingIdError");
                                DataModel.showSnackBar(view,a);
                                break;
                            } else if (sb.getTrackingCode().length() == 0) {
                                a = language_data2.getString("labelTrackingIdError");
                                DataModel.showSnackBar(view,a);
                                break;
                            }else {
                                m++;
                                JSONObject ob=new JSONObject();
                                ob.put("product_id",sb.getId());
                                ob.put("qty_units", "Nos");
                                ob.put("total_qty",sb.getQuantity());
                                ob.put("packing_weight",sb.getWeight());
                                ob.put("weight_units","kilograms");
                                ob.put("packing_length","");
                                ob.put("length_units","");
                                ob.put("packing_width","");
                                ob.put("width_units","");
                                ob.put("packing_height","");
                                ob.put("height_units","");
                                ob.put("shipping_company",sb.getShippingName());
                                ob.put("tracking_id",sb.getTrackingCode());
                                ob.put("confirm",1);
                                arr.put(ob);
                            }
                        }

                        obj.put("samples",arr);
                        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
                        if(m==AdapterSendSample.sampleList.size()){
                            if(DataModel.Internetcheck(getActivity())){
                                RestClientWithoutString.getApiService(getActivity()).submitSample(header, new TypedString(obj.toString()), new Callback<GetSample>() {
                                    @Override
                                    public void success(GetSample getSample, Response response) {
                                        DataModel.loading_box_stop();
                                        if(getSample.getCode()==200){
                                            try {
                                                String head = language_data2.getString("labelModalHeading");
                                                String message = language_data2.getString("labelModalText");
                                                String url=getSample.getResponse().getPdf_url();
                                                sp.save(SharedPrefNames.PDF_URL,url);
                                                alertSubmit(head, message,2);
                                                /*List<SavedProducts> list=SharedPrefNames.getInReviewList(getContext(),sp,view);
                                                FragmentInReview.adapSaved=new AdaptorSavedProduct(getContext(),list,language_data,1);
                                                FragmentInReview.listSavedData.setAdapter(FragmentSavedProduct.adapSaved);*/
                                            }catch (Exception e){}
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        DataModel.loading_box_stop();
                                        String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                        DataModel.showSnackBarError(view,json);
                                    }
                                });

                            }else {
                                a=language_data1.getString("InternetMessage");
                             DataModel.showSnackBar(view,a);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
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
                switch (fragCount){
                    case 2:
                        workId=id;
                       break;
                    case 1:
                        ids.add(id);
                        //fragCount=0;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        Uri uri = null;
        String path = "";
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            if (resultData != null) {
               try{
                   fragCount = 1;
                   uri = resultData.getData();
                   if(uri.toString().startsWith("content")) {
                       path = getPath(uri);
                   }else{
                       path=uri.getPath();
                   }
                String name=path.substring(path.lastIndexOf("/")+1,path.length());
                name=name.toLowerCase();
                if(name.endsWith(".jpeg")||name.endsWith(".jpg")||name.endsWith(".png")||
                        name.endsWith(".doc")||name.endsWith(".docx")||name.endsWith(".pdf")||name.endsWith(".txt")) {
                    uploadProfilePic(path, "products/upload");
                    list.add(name);
                    adapters = new AdapterUploadItem(getActivity(), list, 1);
                    flyRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    flyRecView.setAdapter(adapters);
                    DataModel.loading_box(getActivity(),true);
                }else{
                    String a=language_data1.getString("labelErrorFileFormat");
                    DataModel.showSnackBar(view,a);
                }
            }catch (Exception e){}
                Log.e("Data", "Uri: ");
            }
        }
        if(requestCode==2&&resultCode==getActivity().RESULT_OK){
            if (resultData != null) {
                try{
                    fragCount=2;
                    uri = resultData.getData();
                    if(uri.toString().startsWith("content")) {
                        path = getPath(uri);
                    }else{
                        path=uri.getPath();
                    }
                    String name=path.substring(path.lastIndexOf("/")+1,path.length());
                name=name.toLowerCase();
                if(name.endsWith(".jpeg")||name.endsWith(".jpg")||name.endsWith(".png")||
                        name.endsWith(".doc")||name.endsWith(".docx")||name.endsWith(".pdf")||name.endsWith(".txt")) {
                    worksheetName.setText(name);
                    uploadProfilePic(path, "products/upload");
                    DataModel.loading_box(getActivity(),true);
                }else{
                    String a=language_data1.getString("labelErrorFileFormat");
                    DataModel.showSnackBar(view,a);
                }
            }catch (Exception e){}
                Log.e("Data", "Uri: " + uri.toString());
            }
        }
        if(resultCode==getActivity().RESULT_CANCELED){
            fragCount=3;
        }
    }

    @SuppressLint("NewApi")
    public String getPath(Uri uri){
        String picturePath="";
        try {
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d("", DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            picturePath= cursor.getString(columnIndex); // returns null
            cursor.close();
            if (picturePath == null) {
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
                } catch (Exception e) {
                    picturePath = uri.getPath();
                }
            }
        }catch (Exception e){
            picturePath = uri.getPath();
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

    public void alertSubmit(String title,String message,int i){
        try {
            LayoutInflater inflater= getLayoutInflater();
            View alertLayout=inflater.inflate(R.layout.alert_submit_details,null);
            TextView tvhead=alertLayout.findViewById(R.id.alertHeading);
            TextView tvMessage=alertLayout.findViewById(R.id.alertMessage);
            Button okBtn=alertLayout.findViewById(R.id.alertOkBtn);
            AlertDialog.Builder alt=new AlertDialog.Builder(getActivity());
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
                    tabLayout.getTabAt(0).select();
                }
            });

                FragmentSavedProduct.language_data=new JSONObject();
                FragmentSavedProduct.language_data=DataModel.
                        setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                                getActivity(), "products");
                FragmentProduct.viewPager.setVisibility(View.VISIBLE);
                FragmentProduct.tabLayout.setVisibility(View.VISIBLE);
                FragmentProduct.relAddProduct.setVisibility(View.GONE);
                FragmentProduct.sendSampleLayout.setVisibility(View.GONE);
                FragmentProduct.sendSampleLayout.setVisibility(View.GONE);
                MainActivity.backArrow.setVisibility(View.GONE);
                MainActivity.toolTitle.setText(FragmentSavedProduct.language_data.getString("labelProductHeading"));

                    AdaptorSavedProduct.selectedList=new ArrayList<>();
                    FragmentSavedProduct.btnSendSample.setEnabled(false);
                    FragmentSavedProduct.btnSendSample.setBackgroundResource(R.drawable.rounded_grey);
                    DataModel.loading_box(getActivity(), true);
                    String header = sp.getString(SharedPrefNames.ACCESS_TOKEN);
                    RestClientWithoutString.getApiService(getActivity()).getInReviewProduct(header, new Callback<SavedProductList>() {
                        @Override
                        public void success(SavedProductList savedProductList, Response response) {
                            DataModel.loading_box_stop();
                            FragmentInReview.dataProduct = savedProductList.getResponse().getProducts();
                            FragmentInReview.adapSaved = new AdaptorSavedProduct(getContext(), FragmentInReview.dataProduct, language_data, 2);
                            LinearLayoutManager llm = new LinearLayoutManager(getContext());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            FragmentInReview.listSavedData.setLayoutManager(llm);
                            FragmentInReview.listSavedData.setAdapter(FragmentInReview.adapSaved);
                            if (FragmentInReview.dataProduct.size() > 0) {
                                FragmentInReview.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                FragmentInReview.relNoData.setVisibility(View.GONE);
                                FragmentInReview.listSavedData.setVisibility(View.VISIBLE);
                                FragmentInReview.linButtons.setVisibility(View.VISIBLE);
                            } else if (FragmentInReview.dataProduct.size() == 0) {
                                FragmentInReview.mSwipeRefreshLayout.setVisibility(View.GONE);
                                FragmentInReview.relNoData.setVisibility(View.VISIBLE);
                                FragmentInReview.listSavedData.setVisibility(View.GONE);
                                FragmentInReview.linButtons.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                            DataModel.showSnackBarError(view,json);
                        }
                    });

                    try {
                        DataModel.loading_box(getActivity(), true);
                        RestClientWithoutString.getApiService(getActivity()).getSavedProduct(header, new Callback<SavedProductList>() {
                            @Override
                            public void success(SavedProductList savedProductList, Response response) {
                                DataModel.loading_box_stop();
                                SharedPrefNames.savedProductList=new ArrayList<>();
                                SharedPrefNames.savedProductList = savedProductList.getResponse().getProducts();
                                FragmentSavedProduct.adapSaved = new AdaptorSavedProduct(getContext(), SharedPrefNames.savedProductList, language_data, 1);
                                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                FragmentSavedProduct.listSavedData.setLayoutManager(llm);
                                FragmentSavedProduct.listSavedData.setAdapter(FragmentSavedProduct.adapSaved);
                                if (SharedPrefNames.savedProductList.size() > 0) {
                                    FragmentSavedProduct.relNoData.setVisibility(View.GONE);
                                    FragmentSavedProduct.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                    FragmentSavedProduct.listSavedData.setVisibility(View.VISIBLE);
                                    FragmentSavedProduct.linButtons.setVisibility(View.VISIBLE);
                                } else if (SharedPrefNames.savedProductList.size() == 0) {
                                    FragmentSavedProduct.mSwipeRefreshLayout.setVisibility(View.GONE);
                                    FragmentSavedProduct.relNoData.setVisibility(View.VISIBLE);
                                    FragmentSavedProduct.listSavedData.setVisibility(View.GONE);
                                    FragmentSavedProduct.linButtons.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void failure(RetrofitError error) {
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                DataModel.showSnackBarError(view,json);
                            }
                        });
                    } catch (Exception e) {
                    }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                fragCount=6;
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, 1);
                } else {
                    Log.e(":hello ", " permission not granted");
                }
            }
                break;
                case 2: {
                    fragCount=7;
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 2);
                    } else {
                        Log.e(":hello ", " permission not granted");
                    }
                    break;
                }


            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

}
