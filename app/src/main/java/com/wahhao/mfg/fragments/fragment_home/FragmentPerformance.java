package com.wahhao.mfg.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentPerformance extends BaseFragment {
    View view;
    JSONObject language_data;
    Button viewBtn;
    ProgressBar progress;
    TextView tvTitle,tvBenefits,tvOne,tvTwo,tvThree,tvFour;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_performance, container, false);
        init(view);
        return view;
    }

    public void init(View view){
       viewBtn=(Button)view.findViewById(R.id.viewPerformBtn);
        tvBenefits=(TextView)view.findViewById(R.id.textBenefits);
        tvTitle=(TextView)view.findViewById(R.id.tvtitle);
        tvOne=(TextView)view.findViewById(R.id.textOne);
        tvTwo=(TextView)view.findViewById(R.id.textTwo);
        tvThree=(TextView)view.findViewById(R.id.textThree);
        tvFour=(TextView)view.findViewById(R.id.textFour);
        progress=(ProgressBar)view.findViewById(R.id.progress);

        loadData();
    }

    public void loadData(){
        try {
            language_data= DataModel.
                    setLanguage(Prefs.with(getContext()).getInt(SharedPrefNames.SELCTED_LANGUAGE,0),
                            getContext(),"homeDashboard");
            String a=language_data.getString("labelPerformanceHeading");
            tvBenefits.setText(a);
            a=language_data.getString("labelPerformanceHeading");
            tvOne.setText(a);
            a=language_data.getString("labelPerformanceFree");
            tvTwo.setText(a);
            a=language_data.getString("labelPerformanceMin");
            tvThree.setText(a);
            a=language_data.getString("labelPerformanceFee");
            tvFour.setText(a);
            a=language_data.getString("labelPerformanceLevel");
            tvTitle.setText(a);
            a=language_data.getString("labelPerformanceBtn");
            viewBtn.setText(a);
            progress.setProgress(62);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
