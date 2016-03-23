package com.almog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by almog on 3/10/16.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private String fragments [] = {"All tasks","Waiting Tasks"};

    public SectionsPagerAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return AllTasksFragment.newInstance(position);
            case 1:
                return WaitingTasksFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }
}

