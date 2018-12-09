package com.tech.arinzedroid.starchoice.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tech.arinzedroid.starchoice.models.AgentModel;
import com.tech.arinzedroid.starchoice.models.ProductModel;
import com.tech.arinzedroid.starchoice.models.TransHistoryModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.models.UserProductsModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRepo {

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    private static final String ID = "id";
    private static final String FULLNAME = "fullname";
    private static final String ADDRESS = "address";
    private static final String PIC_URL = "picUrl";
    private static final String PHONE = "phone";
    private static final String DATE_CREATED = "dateCreated";
    private static final String DATE_UPDATED = "dateUpdated";
    private static final String TOTAL_AMOUNT = "totalAmount";

    private static final String IS_PAID_FULLY = "paidFully";
    private static final String AMT_PAID = "amtPaid";



    private static final String AGENT_TABLE_NAME = "AGENTS";
    private static final String CLIENT_TABLE_NAME = "CLIENTS";
    private static final String TRANS_HISTORY_TABLE = "TRANSACTION_HISTORY";
    private static final String PRODUCTS = "PRODUCTS";
    private static final String USER_PRODUCTS = "USER_PRODUCTS";


    public LiveData<Boolean> addUserData(UserModel userModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();

        firestoreDB.collection(CLIENT_TABLE_NAME).document(userModel.getId())
                .set(userModel)
                .addOnSuccessListener(documentReference -> isSuccessful.postValue(true))
                .addOnFailureListener(exception -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<List<UserModel>> getAllUserData(String agentId){
        final MutableLiveData<List<UserModel>> userDataMutableList = new MutableLiveData<>();
        List<UserModel> userModelList = new ArrayList<>();
        firestoreDB.collection(CLIENT_TABLE_NAME)
                .whereEqualTo("agentId",agentId)
                .orderBy(DATE_UPDATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //loop through results
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userModelList.add(document.toObject(UserModel.class));
                        }
                        userDataMutableList.postValue(userModelList);
                    }else{
                        userDataMutableList.postValue(null);
                        Log.e(this.getClass().getSimpleName(), "Error getting documents: ",
                                task.getException());
                    }


                });
        return userDataMutableList;
    }

    public LiveData<UserModel> getUserDataById(String id){
        final MutableLiveData<UserModel> userDataMutableLiveData = new MutableLiveData<>();
        firestoreDB.collection(CLIENT_TABLE_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       DocumentSnapshot documentSnapshot = task.getResult();
                       if(documentSnapshot.exists())
                           userDataMutableLiveData.postValue(documentSnapshot.toObject(UserModel.class));
                       else userDataMutableLiveData.postValue(null);
                   }else{
                       userDataMutableLiveData.postValue(null);
                       Log.e(this.getClass().getSimpleName(), "Error getting documents: ", task.getException());
                   }
                });
        return userDataMutableLiveData;
    }

    public LiveData<UserModel> updateUserData(UserModel userModel){
        final MutableLiveData<UserModel> userDataMutableLiveData = new MutableLiveData<>();

        Map<String,Object> data = new HashMap<>();//
        data.put(ID, userModel.getId());
        data.put(FULLNAME, userModel.getFullname());
        data.put(ADDRESS, userModel.getAddress());
        data.put(PIC_URL, userModel.getPicUrl());
        data.put(PHONE, userModel.getPhone());
        data.put(DATE_CREATED, userModel.getDateCreated());
        data.put(DATE_UPDATED, userModel.getDateUpdated());
        data.put(TOTAL_AMOUNT, userModel.getTotalAmount());

        firestoreDB.collection(CLIENT_TABLE_NAME).document(userModel.getId())
                .update(data)
                .addOnSuccessListener(aVoid -> userDataMutableLiveData.postValue(userModel))
                .addOnFailureListener(aVoid -> userDataMutableLiveData.postValue(null));
        return userDataMutableLiveData;
    }

    public  LiveData<List<TransHistoryModel>> getTransHistory(String userId, String productId){
        final MutableLiveData<List<TransHistoryModel>> transHistoryMutableLiveData = new MutableLiveData<>();
        List<TransHistoryModel> transHistories = new ArrayList<>();
        firestoreDB.collection(TRANS_HISTORY_TABLE)
                .whereEqualTo("userId",userId)
                .whereEqualTo("productId",productId)
                .orderBy(DATE_CREATED,Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            transHistories.add(doc.toObject(TransHistoryModel.class));
                        }
                        transHistoryMutableLiveData.postValue(transHistories);
                    }else{
                        transHistoryMutableLiveData.postValue(null);
                        Log.e(this.getClass().getSimpleName(), "Error getting documents: ", task.getException());
                    }
                });

        return transHistoryMutableLiveData;
    }

    public LiveData<Boolean> addTransHistory(TransHistoryModel transHistoryModel,
                                             UserProductsModel userProductsModel){
        Log.e("AppRepo agentId", transHistoryModel.getAgentId());
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();

        firestoreDB.collection(TRANS_HISTORY_TABLE).document(transHistoryModel.getId())
                .set(transHistoryModel)
                .addOnSuccessListener(docRef -> {

                    Map<String, Object> data = new HashMap<>();
                    data.put(IS_PAID_FULLY,userProductsModel.isPaidFully());
                    data.put(AMT_PAID,userProductsModel.getAmtPaid());
                    data.put(DATE_UPDATED,new Date());

                    DocumentReference documentReference = firestoreDB.collection(USER_PRODUCTS)
                            .document(userProductsModel.getId());
                    documentReference.update(data)
                            .addOnSuccessListener(success -> isSuccessful.postValue(true))
                            .addOnFailureListener(failure -> isSuccessful.postValue(false));
                })
                .addOnFailureListener(ex -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<AgentModel> loginAgent(String username, String password){
        final MutableLiveData<AgentModel> agentDataMutableLiveData = new MutableLiveData<>();
        firestoreDB.collection(AGENT_TABLE_NAME)
                .whereEqualTo("username",username).whereEqualTo("password",password)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot documentSnapshots = task.getResult();
                        if(documentSnapshots.size() > 0)
                            agentDataMutableLiveData.postValue(documentSnapshots.getDocuments().get(0).toObject(AgentModel.class));
                        else if(documentSnapshots.size() == 0)
                            agentDataMutableLiveData.postValue(null);

                    }else
                        agentDataMutableLiveData.postValue(null);
                });
        return agentDataMutableLiveData;
    }

    public LiveData<Boolean> addUserProducts(List<UserProductsModel> userProductsModelList){
        final MutableLiveData<Boolean> success = new MutableLiveData<>();
        for(UserProductsModel data : userProductsModelList){
            firestoreDB.collection(USER_PRODUCTS).document(data.getId())
                    .set(data)
                    .addOnSuccessListener(docRef -> success.postValue(true))
                    .addOnFailureListener(ex -> success.postValue(false));
        }
        return success;
    }

    public LiveData<List<UserProductsModel>> getUserProducts(String userId){
        final MutableLiveData<List<UserProductsModel>> userProductsModelMutableLiveData = new MutableLiveData<>();
        List<UserProductsModel> userProductsModels = new ArrayList<>();
        firestoreDB.collection(USER_PRODUCTS)
                .whereEqualTo("userId",userId)
                .orderBy(DATE_CREATED,Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot dc = task.getResult();
                        Object vb = dc.getDocuments();
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            userProductsModels.add(doc.toObject(UserProductsModel.class));
                        }
                        userProductsModelMutableLiveData.postValue(userProductsModels);
                    }else{
                        userProductsModelMutableLiveData.postValue(null);
                        Log.e(this.getClass().getSimpleName(),"Error on getUserProducts", task.getException());
                    }
                });
        return userProductsModelMutableLiveData;
    }

    public  LiveData<List<ProductModel>> getProducts(){
        final MutableLiveData<List<ProductModel>> productsList = new MutableLiveData<>();
        List<ProductModel> productModels = new ArrayList<>();
        firestoreDB.collection(PRODUCTS)
                .whereEqualTo("isActive", true)
                .orderBy(DATE_CREATED,Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener( task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            productModels.add(doc.toObject(ProductModel.class));
                        }
                        productsList.postValue(productModels);
                    }else{
                        productsList.postValue(null);
                        Log.e(this.getClass().getSimpleName(),"Error getting document", task.getException());
                    }
                });

        return  productsList;
    }

    public LiveData<Boolean> addProducts(ProductModel productsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDB.collection(PRODUCTS).document(productsModel.getId())
                .set(productsModel)
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }



}
