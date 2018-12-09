package com.tech.arinzedroid.starchoice.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.adapter.UserAdapter;
import com.tech.arinzedroid.starchoice.interfaces.UserClickedInterface;
import com.tech.arinzedroid.starchoice.models.AgentModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.util.PrefUtils;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements UserClickedInterface,
        SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recycler_view)
    RecyclerView userRv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.agent_name)
    TextView agentNameTv;
    @BindView(R.id.search_et)
    EditText searchET;

    private UserAdapter adapter;
    private AppViewModel appViewModel;
    private List<UserModel> userModelList;
    private List<UserModel> searchedUserList;
    private PrefUtils prefUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        prefUtils = new PrefUtils(this);

        //get agent details
        retrieveAgentData(getIntent());

        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
        }

        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

       FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                startActivityForResult((new Intent(this,
                        RegisterUserActivity.class)),Constants.USER));

        retrieveUserData();

        onSearch();
    }

    private void retrieveAgentData(Intent intent){
        if(intent != null){
            AgentModel agentModel = Parcels.unwrap(intent.getParcelableExtra(Constants.AGENT_DATA));
            if(agentModel != null)
                agentNameTv.setText(String.valueOf("Welcome " + agentModel.getFirstname() + " " +
                agentModel.getLastName()));
        }
    }

    private void retrieveUserData(){
        mSwipeRefresh.setRefreshing(true);
        noDataTv.setVisibility(View.GONE);
        appViewModel.getUserDataList(prefUtils.getAgentId()).observe(this, userDataList -> {
            if(userDataList != null && userDataList.size() > 0){
                this.userModelList = userDataList;
                searchedUserList = userDataList;
                adapter = new UserAdapter(userDataList,this);
                userRv.setAdapter(adapter);
            }else {
                if(adapter != null)
                    adapter.clearAdapter();
                noDataTv.setVisibility(View.VISIBLE);
            }
            mSwipeRefresh.setRefreshing(false);
        });
    }

    private void onSearch(){
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void performSearch(String query){
        if(!TextUtils.isEmpty(query)){
            if(userModelList != null && !userModelList.isEmpty()){
                List<UserModel> userModels = new ArrayList<>();
                for(UserModel userModel : userModelList){
                    if(userModel.getFullname().toLowerCase().contains(query.toLowerCase())){
                        userModels.add(userModel);
                    }
                }
                searchedUserList = userModels;
                adapter = new UserAdapter(userModels,this);
                userRv.setAdapter(adapter);
            }
        }else{
            searchedUserList = userModelList;
            adapter.addAll(userModelList);
        }
    }

    private void toggleSoftKeyPad(boolean show){
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            if(show){
                inputMethodManager.showSoftInput(searchET,InputMethodManager.SHOW_IMPLICIT);
            }else{
                inputMethodManager.hideSoftInputFromWindow(searchET.getWindowToken(),0);
            }

        }
    }

//    @Override
//    public void finish(){
//        if(searchET.hasFocus()){
//            searchET.setText("");
//            searchET.clearFocus();
//            searchET.setVisibility(View.GONE);
//            if(getSupportActionBar() != null){
//                getSupportActionBar().setTitle("Home");
//            }
//        }else{
//            super.finish();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.e(this.getClass().getSimpleName(),"onActivityResult");
        if(requestCode == Constants.USER && resultCode == RESULT_OK){
            if(data.getBooleanExtra(Constants.REGISTER, false))
                retrieveUserData();
            Log.e(this.getClass().getSimpleName(),String.valueOf(data.getBooleanExtra(Constants.REGISTER,false)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            retrieveUserData();
            return true;
        }else if(id == R.id.action_search){
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("");
                if(searchET.getVisibility() == View.GONE){
                    searchET.setVisibility(View.VISIBLE);
                    searchET.requestFocus();
                    toggleSoftKeyPad(true);
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void userData(int position) {
        if(searchedUserList != null && !searchedUserList.isEmpty() && searchedUserList.size() > position){
            UserModel data = searchedUserList.get(position);
            Intent intent = new Intent(this,UserProfileActivity.class);
            intent.putExtra(Constants.USER_DATA, Parcels.wrap(data));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Invalid user selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRefresh() {
        retrieveUserData();
    }
}
