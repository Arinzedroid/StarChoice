package com.tech.arinzedroid.starchoice.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.adapter.TransHistoryAdapter;
import com.tech.arinzedroid.starchoice.models.TransHistoryModel;
import com.tech.arinzedroid.starchoice.models.UserProductsModel;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.util.PrefUtils;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransHistoryActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.amt)
    EditText amtET;
    @BindView(R.id.confirm_btn)
    Button confirmButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.progress_bar2)
    ProgressBar progressBar2;
    @BindView(R.id.recycler_view)
    RecyclerView transHistoryRv;
    @BindView(R.id.payment_text)
    TextView paymentTv;


    private UserProductsModel userProductsModel;
    private AppViewModel appViewModel;
    private TransHistoryAdapter transHistoryAdapter;
    private boolean isDirty; private PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_history);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        prefUtils = new PrefUtils(this);

        //get data from intent
        retrieveIntent(getIntent());

        //get transHitory from firebase
        getTransHistory(userProductsModel.getUserId(), userProductsModel.getProductId());

    }

    @OnClick(R.id.confirm_btn)
    public void onConfirm(View v){
        processData();
    }

    private void processData(){
        if(TextUtils.isEmpty(amtET.getText())){
            amtET.setError("Required");
            return;
        }
        double amt = Double.parseDouble(amtET.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        confirmButton.setEnabled(false);
        TransHistoryModel transHistoryModel = new TransHistoryModel();
        transHistoryModel.setAmount(amt);
        transHistoryModel.setUserId(userProductsModel.getUserId());
        transHistoryModel.setProductId(userProductsModel.getProductId());
        transHistoryModel.setAgentId(prefUtils.getAgentId());
        transHistoryModel.setStatus("Confirmed");

        //calculate the new amount paid and update data
        double amt2 = userProductsModel.getAmtPaid();
        double price = userProductsModel.getProductModel().getPrice();
        if((amt + amt2) >= price)
            userProductsModel.setPaidFully(true);
        userProductsModel.setAmtPaid(amt + amt2);

        appViewModel.addTransHistory(transHistoryModel,userProductsModel).observe(this, isSuccessful -> {
            if(isSuccessful != null && isSuccessful){
                progressBar.setVisibility(View.GONE);
                amtET.setText("");
                amtET.requestFocus();
                isDirty = true;
                Toast.makeText(this, "Amount paid successfully", Toast.LENGTH_SHORT).show();

                getTransHistory(userProductsModel.getUserId(), userProductsModel.getProductId());
            }else{
                Toast.makeText(this, "Payment was not successful. Please try again later", Toast.LENGTH_SHORT).show();
            }
            confirmButton.setEnabled(true);
        });
    }

    private void retrieveIntent(Intent intent){
        if(intent != null){
            UserProductsModel data = Parcels.unwrap(intent.getParcelableExtra(Constants.USER_PRODUCT));
            if(data != null){
                userProductsModel = data;
                if(data.getProductModel() != null)
                toolbar.setTitle(String.valueOf("Payment for " + data.getProductModel().getProductName()));
                if(userProductsModel.isPaidFully()){
                    amtET.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                    paymentTv.setText(String.valueOf("This item has been fully paid for."));
                }
            }

        }
    }

    private void getTransHistory(String userId, String productId){
        progressBar2.setVisibility(View.VISIBLE);
        appViewModel.getTransHistory(userId,productId).observe(this,
                transHistoryModels -> {
                    if(transHistoryModels != null && !transHistoryModels.isEmpty()){
                        transHistoryAdapter = new TransHistoryAdapter(transHistoryModels);
                        transHistoryRv.setAdapter(transHistoryAdapter);
                    }else{
                        if(transHistoryAdapter != null)
                            transHistoryAdapter.clearAll();
                    }
                    progressBar2.setVisibility(View.GONE);
                });
    }

    @Override
    public void onBackPressed(){
        if(isDirty)
            setResult(RESULT_OK);
        super.onBackPressed();

    }

}
