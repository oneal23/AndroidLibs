package com.lei.baselib_java.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private LinkedList<Fragment> fmList = new LinkedList<>();
    private LinkedList<String> fmTitleList = new LinkedList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fmList.get(position);
    }

    @Override
    public int getCount() {
        return fmList.size();
    }

    public void addFragment(Fragment fragment) {
        fmList.add(fragment);
    }
    public void addFragment(Fragment fragment,String title) {
        fmList.add(fragment);
        fmTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (fmTitleList.size()>0){
            return fmTitleList.get(position);
        }
        return super.getPageTitle(position);
    }

    public Fragment getFragment(int position) {
        return fmList.get(position);
    }

}