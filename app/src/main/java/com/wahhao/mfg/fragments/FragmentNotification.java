package com.wahhao.mfg.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.adapters.AdapterNotifications;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentNotification extends BaseFragment {



    int fragCount;
    private View view;
    private Toolbar toolbar;
    RecyclerView recView;
    AdapterNotifications adapter;
    List<String> headings;
    List<String> values;
    JSONObject language_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        init(view);
        return view;
    }


    void init(View view){
        headings=new ArrayList<>();
        values=new ArrayList<>();
        language_data=new JSONObject();
        recView=(RecyclerView)view.findViewById(R.id.recView);
        lodaData();
        adapter=new AdapterNotifications(getContext(),headings,values);
        LinearLayoutManager lManager=new LinearLayoutManager(getContext());
        recView.setLayoutManager(lManager);
        recView.setAdapter(adapter);
    }

    void lodaData(){
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "notifications");
           String a=language_data.getString("labelProductApproved");
           headings.add(a);
            a=language_data.getString("labelOutStock");
            headings.add(a);
            a=language_data.getString("labelLowInventory");
            headings.add(a);
            a=language_data.getString("labelMoneyTransfer");
            headings.add(a);
            a=language_data.getString("labelLowInventory");
            headings.add(a);
            String value="";
            a=language_data.getString("labelYourNewProduct");
            value=a;
            a=language_data.getString("labelSparklers");
            value=value+" '"+a+"' ";
            a=language_data.getString("labelApprovedText");
            value=value+a;
            values.add(value);
            value="";
            a=language_data.getString("labelYourProduct");
            value=a;
            a=language_data.getString("labelFlower");
            value=value+" '"+a+"' ";
            a=language_data.getString("labelOutStockText");
            value=value+a;
            values.add(value);
            value="";
            value=values.get(0);
            values.add(value);
            a=language_data.getString("labelMoneyTransfer");
            value="$2347.99 "+a;
            values.add(value);
            value=values.get(0);
            values.add(value);

            a=language_data.getString("labelNotifications");
            MainActivity.toolTitle.setText(a);

        }catch (Exception e){

        }
    }

}
