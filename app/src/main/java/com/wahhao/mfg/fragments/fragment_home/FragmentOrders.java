package com.wahhao.mfg.fragments.fragment_home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterOrders;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentOrders extends BaseFragment {

    private  View view;
    private RecyclerView rcView;
    private AdapterOrders adapter;
    private GridLayoutManager mGridLayoutManager;
    private List<String> names;
    private List<Integer> values;
    JSONObject language_data;
    private WebView webView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        init(view);
        return view;
    }


    public void init(View view){
        try {
            names = new ArrayList<>();
            values = new ArrayList<>();
            language_data = new JSONObject();
            loadData();
            webView=(WebView)view.findViewById(R.id.webViewOrder);
            //loadUrl("file:///android_asset/orders.html");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDefaultTextEncodingName("utf-8");
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("file:///android_asset/orders.html");
            adapter = new AdapterOrders(getContext(), names, values);
            rcView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rcView.setLayoutManager(mGridLayoutManager);
            rcView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void loadData() {
        try {
            language_data= DataModel.
                    setLanguage(Prefs.with(getContext()).getInt(SharedPrefNames.SELCTED_LANGUAGE,0),
                            getContext(),"homeDashboard");
            String a=language_data.getString("labelOrdersNew");
            names.add(a);
            values.add(65);
            a=language_data.getString("labelOrdersPacked");
            names.add(a);
            values.add(20);
            a=language_data.getString("labelOrdersShipped");
            names.add(a);
            values.add(10);
            a=language_data.getString("labelOrdersCompleted");
            names.add(a);
            values.add(65);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
