package com.mrnovacrm.b2b_admin;

//public class AdminOrdersTabFragment {
//}

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_dealer.CancelHistoryFragment;
import com.mrnovacrm.b2b_dealer.DeliveredOrderListFragment;
import com.mrnovacrm.b2b_dealer.OrderListFragment;
import com.mrnovacrm.b2b_dealer.PendingListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasad on 3/19/2018.
 */

public class AdminOrdersTabFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storeordersreport, container, false);
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
        adapter.addFragment(new DeliveredOrderListFragment(), getString(R.string.deliver));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            Log.e("ViewPagerAdapter", "ViewPagerAdapter");
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("getItem", "getItem");
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
