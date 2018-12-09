package com.tech.arinzedroid.starchoice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.ui.fragment.AddProductFragment;
import com.tech.arinzedroid.starchoice.ui.fragment.SelectProductFragment;

import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {


    private UserModel userModel;

    public SectionPagerAdapter(FragmentManager fm, UserModel userModel) {
        super(fm);
        this.userModel = userModel;
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:{
               return AddProductFragment.instance(userModel);
           }
           case 1:{
               return SelectProductFragment.newInstance(userModel);
           }
           default: return new AddProductFragment();
       }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
