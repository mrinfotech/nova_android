package com.mrnovacrm.b2b_admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.mrnovacrm.R;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersTabActivity extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Activity mainfinish;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        setTheme(R.style.AppTheme);
        setTitle("Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.fragment_storeordersreport);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
     //   adapter.addFragment(new AdminPlacedOrdersFragment(), getString(R.string.placed));
        adapter.addFragment(new AdminPlacedOrdersFragment(), getString(R.string.placed));
        adapter.addFragment(new AdminApprovedOrdersFragment(), getString(R.string.approved));
        adapter.addFragment(new AdminRejectOrdersFragment(), getString(R.string.rejected));
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