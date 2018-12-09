package com.tech.arinzedroid.starchoice.ui.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.models.UserProductsModel;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {


    @BindView(R.id.product_name)
    EditText productNameEt;
    @BindView(R.id.product_price)
    EditText productPriceEt;
    @BindView(R.id.product_desc)
    EditText productDescEt;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private UserModel userModel;
    private AppViewModel appViewModel;

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment instance(UserModel userModel){
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.USER_DATA, Parcels.wrap(userModel));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            userModel = Parcels.unwrap(getArguments().getParcelable(Constants.USER_DATA));
        }
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_add_product,container,false);
        ButterKnife.bind(this,v);

       return v;
    }

    @OnClick(R.id.register_btn)
    public void onRegisterClick(View v){
        validateData();
    }

    private void validateData(){
        if(TextUtils.isEmpty(productNameEt.getText())){
            productNameEt.setError("Product Name must be specified");
            return;
        }
        if(TextUtils.isEmpty(productPriceEt.getText())){
            productPriceEt.setError("Product Price must be specified");
            return;
        }

        registerBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String productName = productNameEt.getText().toString();
        double productPrice = Double.parseDouble(productPriceEt.getText().toString());
        String productDesc = productDescEt.getText().toString();
        ProductModel productsModel = new ProductModel();
        productsModel.setDesc(productDesc);
        productsModel.setActive(true);
        productsModel.setPrice(productPrice);
        productsModel.setProductName(productName);
        //onRegisterProduct.onRegister(productsModel);

        appViewModel.addProducts(productsModel).observe(this, _isSuccessful -> {
            if(_isSuccessful != null && _isSuccessful){

                //product was created successfully proceed to adding it to client acct
                List<UserProductsModel> userProductsModelList = new ArrayList<>();

                UserProductsModel userProductsModel = new UserProductsModel();
                userProductsModel.setActive(true);
                userProductsModel.setProductModel(productsModel);
                userProductsModel.setUserModel(userModel);
                userProductsModel.setUserId(userModel.getId());
                userProductsModel.setProductId(productsModel.getId());

                userProductsModelList.add(userProductsModel);

                //add data to firebase server
                appViewModel.addUserProducts(userProductsModelList).observe(this, isSuccessful -> {
                    if(isSuccessful != null && isSuccessful){
                        Toast.makeText(getActivity(), "Products added to clients acct successfully", Toast.LENGTH_SHORT).show();
                        if(getActivity() != null){
                            getActivity().finish();
                        }
                    }else{
                        progressBar.setVisibility(View.GONE);
                        registerBtn.setEnabled(true);
                        Toast.makeText(getActivity(), "Product could not be added to this client. " +
                                "Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                progressBar.setVisibility(View.GONE);
                registerBtn.setEnabled(true);
                Toast.makeText(getActivity(), "Product could not be created. " +
                        "Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
