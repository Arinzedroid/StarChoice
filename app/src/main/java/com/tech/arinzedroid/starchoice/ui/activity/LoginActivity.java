package com.tech.arinzedroid.starchoice.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.util.PrefUtils;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_btn)
    Button loginButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.username)
    EditText usernameET;
    @BindView(R.id.password)
    EditText passwordET;

    AppViewModel appViewModel;
    PrefUtils prefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        prefUtils = new PrefUtils(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        loginButton.setOnClickListener( view -> validateControls());

        //new PrefUtils(this).setAgentId("45hy67rt43mj6y");

    }

    private void validateControls(){
        if(TextUtils.isEmpty(usernameET.getText())){
            usernameET.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(passwordET.getText())){
            passwordET.setError("Required");
            return;
        }

        processData();
    }

    private void processData(){
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        appViewModel.loginAgent(username,password).observe(this, agentData -> {
            if(agentData != null){
                prefUtils.setAgentId(agentData.getId());
                Intent intent = new Intent(this,MainActivity.class);
                intent.putExtra(Constants.AGENT_DATA, Parcels.wrap(agentData));
                startActivity(intent);
                finish();
            }else{
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                usernameET.requestFocus();
                Toast.makeText(this, "Login was not successful. Try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
