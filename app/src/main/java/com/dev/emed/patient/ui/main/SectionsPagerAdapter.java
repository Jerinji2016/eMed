package com.dev.emed.patient.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dev.emed.R;
import com.dev.emed.patient.PatientHistoryFragment;
import com.dev.emed.patient.PatientProfileFragment;
import com.dev.emed.patient.PatientExtrasFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private String userId;

    @StringRes
    private static final int[] TAB_TITLES = new int[] {
            R.string.tab_ptn_profile,
            R.string.tab_ptn_scan,
            R.string.tab_ptn_history
    };
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0 :
                fragment = PatientProfileFragment.newInstance(this.getUserId());
                break;
            case 1 :
                fragment = PatientExtrasFragment.newInstance(this.getUserId());
                break;
            case 2:
                fragment = PatientHistoryFragment.newInstance(this.getUserId());
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

    public void setUserId(String user) {
        userId = user;
    }
    private String getUserId() {
        return this.userId;
    }
}