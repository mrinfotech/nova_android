package com.mrnovacrm.b2b_dealer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrnovacrm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasad on 3/19/2018.
 */

public class StoreOrdersTabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dealerstoreorders, container, false);
        viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new OrderListFragment(), getString(R.string.placed));
        adapter.addFragment(new PendingListFragment(), getString(R.string.pending));
        adapter.addFragment(new CancelHistoryFragment(), getString(R.string.cancelled));
        adapter.addFragment(new DeniedOrderListFragment(), getString(R.string.denied));
        adapter.addFragment(new DeliveredOrderListFragment(), getString(R.string.deliver));
        adapter.addFragment(new RejectedOrderListFragment(), getString(R.string.rejected));

       viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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
}


//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TabHost;
//
//import com.nova.R;
//
///**
// * Created by prasad on 4/3/2018.
// */
//
//public class StoreOrdersTabFragment extends Fragment implements TabHost.OnTabChangeListener {
//
//    private int headerposition = 0;
//    PagerAdapter adapter;
//
//    public StoreOrdersTabFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_storeordersreport, container, false);
//
//        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
//
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.placed)));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.pending)));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.cancelled)));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.deliver)));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.rejected)));
//
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
//        adapter = new StoreOrdersTabFragment.PagerAdapter
//                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//            headerposition = 0;
//            TabLayout.Tab tab = tabLayout.getTabAt(headerposition);
//            tab.select();
//            viewPager.setCurrentItem(headerposition);
//
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                //     Log.e("tab selection","tab"+tab.getPosition());
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                viewPager.getAdapter().notifyDataSetChanged();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//        return rootView;
//    }
//
//    @Override
//    public void onTabChanged(String s) {
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    class PagerAdapter extends FragmentStatePagerAdapter {
//        int mNumOfTabs;
//
//        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
//            super(fm);
//            this.mNumOfTabs = NumOfTabs;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            switch (position) {
//
//                case 0:
//                    return new OrderListFragment();
//
//                case 1:
//                    return new PendingListFragment();
//
//                case 2:
//                    return new CancelHistoryFragment();
//
//                case 3:
//                    return new DeliveredOrderListFragment();
//
//                case 4:
//                    return new RejectedOrderListFragment();
//
//                default:
//                    return null;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return mNumOfTabs;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//    }
//}