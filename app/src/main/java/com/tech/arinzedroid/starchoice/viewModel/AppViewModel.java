package com.tech.arinzedroid.starchoice.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tech.arinzedroid.starchoice.models.AgentModel;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.TransHistoryModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.models.UserProductsModel;
import com.tech.arinzedroid.starchoice.repo.AppRepo;

import java.util.List;

public class AppViewModel extends ViewModel {

    private AppRepo appRepo;
    private LiveData<List<UserModel>> userDataList;
    private LiveData<List<ProductModel>> productsLiveData;
    private LiveData<List<UserProductsModel>> userProductModelList;

    public  AppViewModel(){
        appRepo = new AppRepo();
    }

    public LiveData<Boolean> addData(UserModel userModel){
        return appRepo.addUserData(userModel);
    }

    public LiveData<List<UserModel>> getUserDataList(String agentId) {
            userDataList = appRepo.getAllUserData(agentId);
        return userDataList;
    }

    public LiveData<UserModel> updateUserData(UserModel userModel){
        return appRepo.updateUserData(userModel);
    }

    public  LiveData<UserModel> getUserDataById(String id){
        return appRepo.getUserDataById(id);
    }

    public LiveData<List<TransHistoryModel>> getTransHistory(String userId, String productId){
        return appRepo.getTransHistory(userId,productId);
    }

    public  LiveData<Boolean> addTransHistory(TransHistoryModel transHistoryModel,
                                              UserProductsModel userProductsModel){
        return appRepo.addTransHistory(transHistoryModel, userProductsModel);
    }

    public LiveData<AgentModel> loginAgent(String username, String password){
        return appRepo.loginAgent(username,password);
    }

    public LiveData<List<ProductModel>> getProducts(){
//        if(productsLiveData == null)
            productsLiveData = appRepo.getProducts();
        return productsLiveData;
    }
    public LiveData<Boolean> addUserProducts(List<UserProductsModel> userProductsModelsList){
        return appRepo.addUserProducts(userProductsModelsList);
    }

    public LiveData<List<UserProductsModel>> getUserProducts(String userId){
        //if(userProductModelList == null)
            userProductModelList = appRepo.getUserProducts(userId);
        return userProductModelList;
    }
}
