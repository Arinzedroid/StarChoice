package com.tech.arinzedroid.starchoice.ui.fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.adapter.ProductAdapter;
import com.tech.arinzedroid.starchoice.interfaces.RemoveProductInterface;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.models.UserProductsModel;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddProduct extends DialogFragment implements AdapterView.OnItemSelectedListener, RemoveProductInterface{



    @BindView(R.id.confirm_btn)
    Button confirmButton;
    @BindView(R.id.spinner)
    Spinner productItemsSpinner;
    @BindView(R.id.recycler_view)
    RecyclerView productRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private List<ProductModel> productModelList = new ArrayList<>();
    private List<UserProductsModel> selectedProducts = new ArrayList<>();
    private List<ProductModel> adapterList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private OnFragmentInteractionListener mListener;
    private UserModel userModel;
    private AppViewModel appViewModel;

    private static final String PRODUCTS = "PRODUCTS";
    private static final String USER_MODEL= "USER_MODEL";

    public AddProduct() {
        //Required empty public constructor
    }

    public static AddProduct newInstance(List<ProductModel> productModels, UserModel userModel){
        AddProduct fragment = new AddProduct();
        Bundle arg = new Bundle();
        arg.putParcelable(PRODUCTS, Parcels.wrap(productModels));
        arg.putParcelable(USER_MODEL,Parcels.wrap(userModel));
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the user_product_items for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        ButterKnife.bind(this,view);

        ArrayAdapter<ProductModel> spinnerAdapter = new ArrayAdapter<>(getContext(),
               android.R.layout.simple_spinner_item,productModelList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productAdapter = new ProductAdapter(adapterList,this);
        productRecyclerView.setAdapter(productAdapter);

        productItemsSpinner.setAdapter(spinnerAdapter);
        productItemsSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        if(getArguments() != null){
            productModelList = Parcels.unwrap(getArguments().getParcelable(PRODUCTS));
            userModel = Parcels.unwrap(getArguments().getParcelable(USER_MODEL));
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @OnClick(R.id.confirm_btn)
    public void onConfirm(View v){
        processData();
    }

    private void processData(){
        progressBar.setVisibility(View.VISIBLE);
        appViewModel.addUserProducts(selectedProducts).observe(this, isSuccessful -> {
            if(isSuccessful != null && isSuccessful){
                if(mListener != null)
                    mListener.onConfirmClicked(selectedProducts);
                dismiss();
            }else {
                Toast.makeText(getActivity(), "Operation was not successful. Please try again later", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void displaySelectedItem(ProductModel productModel){
        UserProductsModel userProductsModel = new UserProductsModel();
        userProductsModel.setProductModel(productModel);
        userProductsModel.setProductId(productModel.getId());
        userProductsModel.setUserId(userModel.getId());
        userProductsModel.setUserModel(userModel);
        userProductsModel.setDateCreated(new Date());
        selectedProducts.add(userProductsModel);
        productAdapter.addProduct(productModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        displaySelectedItem((ProductModel) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void removeProduct(int position) {
        if(!selectedProducts.isEmpty() && selectedProducts.get(position) != null){
            selectedProducts.remove(position);
            productAdapter.removeProduct(position);
        }
    }

    public interface OnFragmentInteractionListener {
        void onConfirmClicked(List<UserProductsModel> userProductModels);
    }
}
