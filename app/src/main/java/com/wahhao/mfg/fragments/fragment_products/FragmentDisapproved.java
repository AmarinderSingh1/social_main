package com.wahhao.mfg.fragments.fragment_products;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterUploadItem;
import com.wahhao.mfg.adapters.AdaptorSavedProduct;
import com.wahhao.mfg.beans.SavedProducts;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.fragments.FragmentProduct;
import com.wahhao.mfg.model.GetCategoryList;
import com.wahhao.mfg.model.SavedProductList;
import com.wahhao.mfg.network.RestClientWithoutString;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class FragmentDisapproved extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    JSONObject language_data;
    RelativeLayout relNoData;
    TextView txtTitle1,txtTitle2,txtTitle3,txtTitle4;
    Button btnAddProd1, btnAddprod2,btnSendSample;
    RecyclerView listSavedData;
    ImageView imgRefresh;
    LinearLayout linButtons;
    boolean hasContract=false;
    Prefs sp;
    AdaptorSavedProduct adapSaved;
    List<SavedProducts> dataProduct,selectedList;
    SwipeRefreshLayout mSwipeRefreshLayout;

    int fragCount;
   private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_product, container, false);
        init(view);
        return view;
    }


    public void init(View view){
        try {
            relNoData = (RelativeLayout) view.findViewById(R.id.relNoProd);
            linButtons = (LinearLayout) view.findViewById(R.id.linButtons);
            txtTitle1 = (TextView) view.findViewById(R.id.textTitle1);
            txtTitle2 = (TextView) view.findViewById(R.id.textTitle2);
            txtTitle3 = (TextView) view.findViewById(R.id.textTitle3);
            txtTitle4 = (TextView) view.findViewById(R.id.textTitle4);
            btnAddProd1 = (Button) view.findViewById(R.id.btnaddProd1);
            btnAddprod2 = (Button) view.findViewById(R.id.btnaddProd2);
            btnSendSample = (Button) view.findViewById(R.id.btnsendSample);
            btnSendSample.setVisibility(View.GONE);
            imgRefresh = (ImageView) view.findViewById(R.id.imgRefresh);
            listSavedData = (RecyclerView) view.findViewById(R.id.listSaved);
            language_data = new JSONObject();
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            loadData();
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            btnAddprod2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClick();
                }

            });

            btnAddProd1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClick();
                }

            });
            mSwipeRefreshLayout.setOnRefreshListener(this);

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void loadData(){
        try {
            sp=new Prefs(getActivity());
            hasContract=sp.getBoolean(SharedPrefNames.HAS_SIGNED_CONTRACT,false);
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "products");
            txtTitle1.setText(language_data.getString("noDisapproved"));
            txtTitle2.setText(language_data.getString("startAddingText"));
            txtTitle3.setText(language_data.getString("noDataText"));
            txtTitle4.setText(language_data.getString("noDataDesc"));
            btnAddProd1.setText(language_data.getString("labelAddProdButton"));
            btnAddprod2.setText(language_data.getString("labelAddProdButton"));
            btnSendSample.setText(language_data.getString("labelSendSamplesButton"));
            relNoData.setVisibility(View.VISIBLE);
            linButtons.setVisibility(View.GONE);
        }catch (Exception e){

        }
        getProductSavedData(0);
    }


    public void getProductSavedData(int i){

        if(i==0) {
            DataModel.loading_box(getActivity(), true);
        }

        String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);
        RestClientWithoutString.getApiService(getContext())
                .getDisapprovedProduct(header,
                        new Callback<SavedProductList>() {
                            @Override
                            public void success(SavedProductList basic_response, Response response) {
                                DataModel.loading_box_stop();
                                try{
                                    if(basic_response.getResponse().getProducts().size()>0){
                                        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                        linButtons.setVisibility(View.VISIBLE);
                                        relNoData.setVisibility(View.GONE);
                                        listSavedData.setVisibility(View.VISIBLE);
                                        dataProduct=basic_response.getResponse().getProducts();
                                        adapSaved=new AdaptorSavedProduct(getContext(),dataProduct,language_data,4);
                                        LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                                        listSavedData.setLayoutManager(llm);
                                        listSavedData.setAdapter(adapSaved);
                                        if(hasContract){
                                            relNoData.setVisibility(View.GONE);
                                            linButtons.setVisibility(View.VISIBLE);
                                        }else{
                                            relNoData.setVisibility(View.GONE);
                                            linButtons.setVisibility(View.GONE);
                                        }


                                    }else{
                                        mSwipeRefreshLayout.setVisibility(View.GONE);
                                        linButtons.setVisibility(View.GONE);
                                        relNoData.setVisibility(View.VISIBLE);
                                        listSavedData.setVisibility(View.GONE);
                                        btnAddprod2.setVisibility(View.GONE);
                                        if(hasContract){
                                            btnAddprod2.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }catch (Exception e){
                                    mSwipeRefreshLayout.setVisibility(View.GONE);
                                    linButtons.setVisibility(View.GONE);
                                    relNoData.setVisibility(View.VISIBLE);
                                    listSavedData.setVisibility(View.GONE);
                                    btnAddprod2.setVisibility(View.GONE);
                                    if(!hasContract){
                                        btnAddprod2.setVisibility(View.VISIBLE);
                                    }
                                }
                                mSwipeRefreshLayout.setRefreshing(false);

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                DataModel.loading_box_stop();
                                String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                                DataModel.showSnackBarError(view,json);
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setVisibility(View.GONE);
                            }
                        });

    }

    public void myClick(){
        try{
            SharedPrefNames.name=new ArrayList<>();
            SharedPrefNames.ids=new ArrayList<>();
            FragmentProduct.etProName.setText("");
            FragmentProduct.etAdd.setText("");
            FragmentProduct.etCMar.setText("");
            FragmentProduct.etDSp.setText("");
            FragmentProduct.etHsCode.setText("");
            FragmentProduct.etManCost.setText("");
            FragmentProduct.etSp.setText("");
            FragmentProduct.list=new ArrayList<>();
            SharedPrefNames.name=new ArrayList<>();
            SharedPrefNames.ids=new ArrayList<>();
            FragmentProduct.adapters=new AdapterUploadItem(getActivity(),FragmentProduct.list,1);
            FragmentProduct.flyRecView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
            FragmentProduct.flyRecView.setAdapter(FragmentProduct.adapters);

            FragmentProduct.viewPager.setVisibility(View.GONE);
            FragmentProduct.tabLayout.setVisibility(View.GONE);
            FragmentProduct.relAddProduct.setVisibility(View.VISIBLE);
            MainActivity.backArrow.setVisibility(View.VISIBLE);
            language_data=new JSONObject();
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "addProducts");
            String a=language_data.getString("labelProductInfo");
            MainActivity.toolTitle.setText(a);
            a=language_data.getString("labelAttachSheet");
            FragmentProduct.worksheetName.setText(a);
            DataModel.loading_box(getActivity(),true);
            String header=sp.getString(SharedPrefNames.ACCESS_TOKEN);

            RestClientWithoutString.getApiService(getActivity()).getProductCategory(header, new Callback<GetCategoryList>() {
                @Override
                public void success(GetCategoryList getCategoryList, Response response) {
                    SharedPrefNames.name.add(0,"Choose your option");
                    SharedPrefNames.ids.add(0,"choose");
                    for(int i=0;i<getCategoryList.getResponse().getCat().length;i++){
                        DataModel.loading_box_stop();
                        String na=getCategoryList.getResponse().getCat()[i].getName();
                        String id=getCategoryList.getResponse().getCat()[i].getParent_ids();
                        SharedPrefNames.name.add(na);
                        SharedPrefNames.ids.add(id);
                    }
                    FragmentProduct.adap = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, SharedPrefNames.name);
                    FragmentProduct.adap.setDropDownViewResource(R.layout.spinner_item);
                    FragmentProduct.spin.setAdapter(FragmentProduct.adap);
                }

                @Override
                public void failure(RetrofitError error) {
                    DataModel.loading_box_stop();
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    DataModel.showSnackBarError(view,json);
                }
            });
        }catch (Exception e){}
        FragmentProduct.view.setFocusableInTouchMode(true);
        FragmentProduct.view.setFocusable(true);
    }


    public void onRefresh(){
        getProductSavedData(1);
    }

}
