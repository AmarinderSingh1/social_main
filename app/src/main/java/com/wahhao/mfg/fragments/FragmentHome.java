package com.wahhao.mfg.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.wahhao.mfg.MainActivity;
import com.wahhao.mfg.R;
import com.wahhao.mfg.fragments.fragment_home.FragmentInventory;
import com.wahhao.mfg.fragments.fragment_home.FragmentOrders;
import com.wahhao.mfg.fragments.fragment_home.FragmentPerformance;
import com.wahhao.mfg.fragments.fragment_home.FragmentSales;
import com.wahhao.mfg.fragments.fragment_home.FragmentWallet;
import com.wahhao.mfg.sharedprefrances.Prefs;
import com.wahhao.mfg.sharedprefrances.SharedPrefNames;
import com.wahhao.mfg.utils.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class FragmentHome extends BaseFragment {

    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    int fragCount;
    private View view;
    JSONObject language_data;
    private  String[] tabLabels;
    private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        loadData(view);
        return view;
    }

    void loadData(View view){
        try {
            language_data = DataModel.
                    setLanguage(Prefs.with(getActivity()).getInt(SharedPrefNames.SELCTED_LANGUAGE, 0),
                            getActivity(), "homeDashboard");
            tabLabels=new String[]{language_data.getString("labelOrdersTitle"),
                    language_data.getString("labelWalletTitle"),
                    language_data.getString("labelSalesTitle"),language_data.getString("labelInventoryTitle"),
                    language_data.getString("labelPerformanceTitle")};

            String a=language_data.getString("labelDashBoardHeading");
            MainActivity.toolTitle.setText(a);

        }catch (Exception e){

        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentOrders(), tabLabels[0]);
        adapter.addFragment(new FragmentWallet(), tabLabels[1]);
        adapter.addFragment(new FragmentSales(), tabLabels[2]);
        adapter.addFragment(new FragmentInventory(), tabLabels[3]);
        adapter.addFragment(new FragmentPerformance(), tabLabels[4]);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)view. findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFC4C3"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#CCFFFFFF"), Color.parseColor("#ffffff"));

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        String arr[]={"com.wahhao.mfg.fragments.fragment_home.FragmentOrders","com.wahhao.mfg.fragments.fragment_home.FragmentWallet",
                "com.wahhao.mfg.fragments.fragment_home.FragmentSales","com.wahhao.mfg.fragments.fragment_home.FragmentInventory",
                "com.wahhao.mfg.fragments.fragment_home.FragmentPerformance"};
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


}
