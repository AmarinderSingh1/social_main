package com.wahhao.mfg.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.BaseFragment;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

public class FragmentWallet extends BaseFragment {
    View view;
    JSONObject language_data;
    Button transferBtn;
    TextView tvWalletBal,tvlastpayment, tvnextpayment, tvoutstanding, tvunbilledorders,tvlastpaymentValue, tvnextpaymentValue, tvoutstandingValue, tvunbilledordersValue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        language_data=new JSONObject();
        transferBtn=(Button)view.findViewById(R.id.twbBtn);
        tvWalletBal=(TextView)view.findViewById(R.id.textwalletbal);
        tvlastpayment=(TextView)view.findViewById(R.id.textlp);
        tvlastpaymentValue=(TextView)view.findViewById(R.id.textlp1);
        tvnextpayment=(TextView)view.findViewById(R.id.textnp);
        tvnextpaymentValue=(TextView)view.findViewById(R.id.textnp1);
        tvoutstanding=(TextView)view.findViewById(R.id.textop);
        tvoutstandingValue=(TextView)view.findViewById(R.id.textop1);
        tvunbilledorders=(TextView)view.findViewById(R.id.textuo);
        tvunbilledordersValue=(TextView)view.findViewById(R.id.textuo1);
        loadData();

    }


    public void loadData(){
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "homeDashboard");
            String a = language_data.getString("labelWalletLastPay");
            tvlastpayment.setText(a);
            a = language_data.getString("labelWalletNextPay");
            tvnextpayment.setText(a);
            a = language_data.getString("labelWalletOutstandingPay");
            tvoutstanding.setText(a);
            a = language_data.getString("labelWalletUnbilledOrders");
            tvunbilledorders.setText(a);
            a = language_data.getString("labelWalletTransferBtn");
            transferBtn.setText(a);
            a=language_data.getString("labelWalletBalance");
            tvWalletBal.setText(a);
        }catch (Exception e){

        }
    }



}