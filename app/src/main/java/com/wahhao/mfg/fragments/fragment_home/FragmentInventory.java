package com.wahhao.mfg.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentInventory extends BaseFragment {
    View view;
    JSONObject language_data;
    Button scheduleBtn;
    TextView tvskus,tvlstock,tvofstock,tvskus1,tvlstock1,tvofstock1;
    WebView webView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inventory, container, false);
        init(view);
        return view;
    }


    public void init(View view){
        language_data=new JSONObject();
        webView=(WebView)view.findViewById(R.id.webViewInventory);
        scheduleBtn=(Button)view.findViewById(R.id.scheduleBtn);
        tvskus=(TextView)view.findViewById(R.id.skus);
        tvskus1=(TextView)view.findViewById(R.id.skus1);
        tvlstock=(TextView)view.findViewById(R.id.lstock);
        tvlstock1=(TextView)view.findViewById(R.id.lstock1);
        tvofstock=(TextView)view.findViewById(R.id.ofstock);
        tvofstock1=(TextView)view.findViewById(R.id.ofstock1);
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
        webView.loadUrl("file:///android_asset/inventory.html");

        loadData();
    }
    public void loadData() {
        try {
            language_data= DataModel.
                    setLanguage(Prefs.with(getContext()).getInt(SharedPrefNames.SELCTED_LANGUAGE,0),
                            getContext(),"homeDashboard");
            String a=language_data.getString("labelInventoryTransferBtn");
            scheduleBtn.setText(a);
            a=language_data.getString("labelInventorySku");
            tvskus.setText(a);
            a=language_data.getString("labelInventoryLowStock");
            tvlstock.setText(a);
            a=language_data.getString("labelInventoryOutStock");
            tvofstock.setText(a);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
