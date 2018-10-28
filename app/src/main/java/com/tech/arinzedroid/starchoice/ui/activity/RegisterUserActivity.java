package com.tech.arinzedroid.starchoice.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.tech.arinzedroid.starchoice.R;
import com.tech.arinzedroid.starchoice.models.UserModel;
import com.tech.arinzedroid.starchoice.util.Constants;
import com.tech.arinzedroid.starchoice.util.PrefUtils;
import com.tech.arinzedroid.starchoice.viewModel.AppViewModel;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterUserActivity extends AppCompatActivity {
    @BindView(R.id.name)
    EditText nameTv;
    @BindView(R.id.address)
    EditText addressEt;
    @BindView(R.id.phone)
    EditText phoneEt;
    @BindView(R.id.kin_name)
    EditText kinName;
    @BindView(R.id.kin_address)
    EditText kinAddress;
    @BindView(R.id.kin_phone)
    EditText kinPhone;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.agent_name)
    EditText agentNameTv;

    private boolean register;
    private PrefUtils prefUtils;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        prefUtils = new PrefUtils(this);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("Register New Client");
    }

    @OnClick(R.id.register_btn)
    public void onRegisterClick(View view){
        validateControl();
    }

    private void validateControl(){
        if(TextUtils.isEmpty(nameTv.getText())){
            nameTv.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(phoneEt.getText())){
            phoneEt.setError("Required");
            return;
        }

        if(TextUtils.isEmpty(kinName.getText())){
            kinName.setError("Required");
            return;
        }
        if(TextUtils.isEmpty(kinPhone.getText())){
            kinPhone.setError("Required");
            return;
        }

        processData();
    }

    private void processData(){
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        String agentName = agentNameTv.getText().toString();
        String name = nameTv.getText().toString();
        String address = addressEt.getText().toString();
        String phone = phoneEt.getText().toString();

        String kName = kinName.getText().toString();
        String kAddress = kinAddress.getText().toString();
        String kPhone = kinPhone.getText().toString();

        UserModel data = new UserModel();
        data.setAgentName(agentName);
        data.setFullname(name);
        data.setAddress(address);
        data.setPhone(phone);
        data.setDateCreated(new Date());
        data.setDateUpdated(new Date());
        data.setAgentId(prefUtils.getAgentId());
        data.setKinName(kName);
        data.setKinAddress(kAddress);
        data.setKinPhone(kPhone);

        //TODO save data to firebase
        appViewModel.addData(data).observe(this,isSuccessful -> {
            progressBar.setVisibility(View.GONE);
            registerBtn.setEnabled(true);
            if(isSuccessful != null && isSuccessful){
                Toast.makeText(this, "Data saved successfully",Toast.LENGTH_SHORT).show();
                register = true;

                nameTv.setText("");
                nameTv.requestFocus();
                addressEt.setText("");
                phoneEt.setText("");
                kinAddress.setText("");
                kinName.setText("");
                kinPhone.setText("");

            }else{
                Toast.makeText(this, "Error creating user, pls try again later", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed(){
        Intent data = new Intent();
        data.putExtra(Constants.REGISTER,register);
        setResult(RESULT_OK,data);
        super.onBackPressed();
    }
}
