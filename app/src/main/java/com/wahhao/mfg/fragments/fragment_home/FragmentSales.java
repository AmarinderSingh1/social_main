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

import org.json.JSONObject;

public class FragmentSales extends BaseFragment {
    View view;
    JSONObject language_data;
    Button viewBtn;
    TextView tvTitle,tvVsPre,tvSC,tvOne,tvTwo,tvThree,tv10,tv11,tv20,tv21,tv30,tv31;
    WebView webView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales, container, false);
        init(view);
        return view;
    }


    void init(View view){
        viewBtn=(Button)view.findViewById(R.id.viewBtn);
        tvTitle=(TextView)view.findViewById(R.id.tvtitle);
        tvVsPre=(TextView)view.findViewById(R.id.tvPrevious);
        tvSC=(TextView)view.findViewById(R.id.textsc);
        tvOne=(TextView)view.findViewById(R.id.tvOne);
        tvTwo=(TextView)view.findViewById(R.id.tvTwo);
        tvThree=(TextView)view.findViewById(R.id.tvThree);
        tv10=(TextView)view.findViewById(R.id.text10);
        tv11=(TextView)view.findViewById(R.id.text11);
        tv20=(TextView)view.findViewById(R.id.text20);
        tv21=(TextView)view.findViewById(R.id.text21);
        tv30=(TextView)view.findViewById(R.id.text30);
        tv31=(TextView)view.findViewById(R.id.text31);

        webView=(WebView)view.findViewById(R.id.webViewSales);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/sales.html");

        loadData();

    }

    void loadData() {
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "homeDashboard");
            String a=language_data.getString("labelSalesLink");
            viewBtn.setText(a);
            a=language_data.getString("labelSalesC3");
            tv30.setText(a);
            a=language_data.getString("labelSalesC2");
            tv20.setText(a);
            a=language_data.getString("labelSalesC1");
            tv10.setText(a);
            a=language_data.getString("labelSalesGrossText");
            tvOne.setText(a);
            a=language_data.getString("labelSalesSoldText");
            tvTwo.setText(a);
            a=language_data.getString("labelSalesDailyText");
            tvThree.setText(a);
            a=language_data.getString("labelSalesPrevText");
            tvVsPre.setText(a);
            a=language_data.getString("labelSalesComparisonText");
            tvSC.setText(a);
            a=language_data.getString("labelSalesHeading");
            tvTitle.setText(a+" - 2017");


        } catch (Exception e) {

        }
    }

}
