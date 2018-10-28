package com.tech.arinzedroid.starchoice.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.adapter.UserAdapter;
import com.tech.arinzedroid.starchoice.interfaces.UserClickedInterface;
import com.tech.arinzedroid.starchoice.models.AgentModel;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.util.PrefUtils;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

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

    private UserAdapter adapter;
    private AppViewModel appViewModel;
    private List<UserModel> userModelList;
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

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Home Page");

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void userData(int position) {
        //Toast.makeText(this, "Item clicked at position " + position, Toast.LENGTH_SHORT).show();
        UserModel data = userModelList.get(position);
        Intent intent = new Intent(this,UserProfileActivity.class);
        intent.putExtra(Constants.USER_DATA, Parcels.wrap(data));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        retrieveUserData();
    }
}
