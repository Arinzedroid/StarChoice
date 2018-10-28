package com.tech.arinzedroid.starchoice.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.adapter.TransHistoryAdapter;
import com.tech.arinzedroid.starchoice.adapter.UserProductAdapter;
import com.tech.arinzedroid.starchoice.interfaces.UserProductClickedInterface;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.models.UserProductsModel;
import com.tech.arinzedroid.starchoice.ui.fragment.AddProduct;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.util.FormatUtil;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileActivity extends AppCompatActivity implements
        AddProduct.OnFragmentInteractionListener, UserProductClickedInterface{

    @BindView(R.id.name)
    TextView nameTv;
    @BindView(R.id.recycler_view)
    RecyclerView userProductRv;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.refresh)
    Button refreshBtn;
    @BindView(R.id.phone)
    TextView phoneTv;
    @BindView(R.id.agent_name)
    TextView agentNameTv;
    @BindView(R.id.address)
    TextView addressTv;
    @BindView(R.id.kin_name)
    TextView kinNameTv;
    @BindView(R.id.kin_address)
    TextView kinAddressTv;
    @BindView(R.id.kin_phone)
    TextView kinPhoneTv;
    @BindView(R.id.amt_remaining)
    TextView amtRemainingTv;
    @BindView(R.id.amt_paid)
    TextView amtPaidTv;
    @BindView(R.id.product_bought)
    TextView productBoughtTv;

    private AppViewModel appViewModel;
    private UserModel userModel;
    private UserProductAdapter userProductAdapter;
    private List<UserProductsModel> userProductsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("User Profile");

        populateViews(getIntent());
        getUserProducts();
    }

    private void populateViews(Intent intent){
        if(intent != null){
            UserModel userModel = Parcels.unwrap(intent.getParcelableExtra(Constants.USER_DATA));
            if(userModel != null){
                this.userModel = userModel;
                nameTv.setText(userModel.getFullname());
                phoneTv.setText(userModel.getPhone());
                addressTv.setText(userModel.getAddress());
                kinNameTv.setText(userModel.getKinName());
                kinAddressTv.setText(userModel.getKinAddress());
                kinPhoneTv.setText(userModel.getKinPhone());
                agentNameTv.setText(String.valueOf("Agent: " + userModel.getAgentName()));
            }
        }
    }

    private void getAllProducts(){
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.fab).setEnabled(false);
        appViewModel.getProducts().observe(this, products -> {
            if(products != null && !products.isEmpty()){
                findViewById(R.id.fab).setEnabled(true);
                progressBar.setVisibility(View.GONE);
                DialogFragment fragment = AddProduct.newInstance(products,userModel);
                fragment.show(getSupportFragmentManager(),"dialogFragment");
            }else{
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.fab).setEnabled(true);
                Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void computeTransValuesForUser(List<UserProductsModel> userProductsModels){
        if(userProductsModels != null && !userProductsModels.isEmpty()){
            int totalBought = 0; double totalAmt = 0, totalAmtPaid = 0, totalAmtRem = 0;
            for(int i = 0; i < userProductsModels.size(); i++){
                if(userProductsModels.get(i).isPaidFully())
                    totalBought += 1;
                totalAmt += userProductsModels.get(i).getProductModel().getPrice();
                totalAmtPaid += userProductsModels.get(i).getAmtPaid();
            }
            totalAmtRem = totalAmt - totalAmtPaid;
            amtPaidTv.setText(FormatUtil.formatPrice(totalAmtPaid));
            amtRemainingTv.setText(FormatUtil.formatPrice(totalAmtRem));
            productBoughtTv.setText(String.valueOf(totalBought));
        }else{
            amtPaidTv.setText(FormatUtil.formatPrice(0));
            amtRemainingTv.setText(FormatUtil.formatPrice(0));
            productBoughtTv.setText("0");
        }
    }

    private void getUserProducts(){
        progressBar.setVisibility(View.VISIBLE);
        if(userProductAdapter != null){
            userProductAdapter.clearData();
            this.userProductsModelList.clear();
        }

        appViewModel.getUserProducts(userModel.getId()).observe(this, userProductsModels -> {
            if(userProductsModels != null && !userProductsModels.isEmpty()){
                userProductAdapter = new UserProductAdapter(userProductsModels,this);
                userProductRv.setAdapter(userProductAdapter);
                this.userProductsModelList = new ArrayList<>(userProductsModels);
                computeTransValuesForUser(userProductsModels);
            }else
                computeTransValuesForUser(null);
            progressBar.setVisibility(View.GONE);
        });
    }

    @OnClick(R.id.fab)
    public void fabClick(View v){
        getAllProducts();
    }

    @OnClick(R.id.refresh)
    public void onRefresh(View v){
        getUserProducts();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == Constants.ACTIVITY_RESULT && resultCode == RESULT_OK){
            getUserProducts();
        }
    }

    @Override
    public void onConfirmClicked(List<UserProductsModel> userProductsModels) {
        if(userProductsModels != null && !userProductsModels.isEmpty()){
            if(userProductAdapter != null){
                userProductAdapter.addProducts(userProductsModels);
                this.userProductsModelList.addAll(userProductsModels);
            }else{
                userProductAdapter = new UserProductAdapter(userProductsModels,this);
                userProductRv.setAdapter(userProductAdapter);
                this.userProductsModelList = new ArrayList<>(userProductsModels);
            }
        }
    }

    @Override
    public void onProductClick(int position) {
        if(userProductsModelList != null && userProductsModelList.size() > position){
            UserProductsModel data = userProductsModelList.get(position);
            Intent intent = new Intent(this,TransHistoryActivity.class);
            intent.putExtra(Constants.USER_PRODUCT, Parcels.wrap(data));
            startActivityForResult(intent,Constants.ACTIVITY_RESULT);
        }
    }
}
