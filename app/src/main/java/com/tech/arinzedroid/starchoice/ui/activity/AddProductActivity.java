package com.tech.arinzedroid.starchoice.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.adapter.SectionPagerAdapter;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.ui.fragment.AddProductFragment;
import com.tech.arinzedroid.starchoice.ui.fragment.SelectProductFragment;
import com.tech.arinzedroid.starchoice.util.Constants;

import org.parceler.Parcels;

import java.util.List;

public class AddProductActivity extends AppCompatActivity{


    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        getDataFromIntents(getIntent());
        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), userModel);

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void getDataFromIntents(Intent intent){
        if(intent != null){
            userModel = Parcels.unwrap(intent.getParcelableExtra(Constants.USER_DATA));
            if(userModel == null){
                Toast.makeText(this, "User not valid. Pls select a valid user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
