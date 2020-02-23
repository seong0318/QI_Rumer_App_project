package com.example.qiplatform_practice1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Console;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface ChangePwdPost {
    @FormUrlEncoded
    @POST("/changepwdbtn/1")
    Call<Result> postData(@FieldMap HashMap<String, Object> param);
}

class ChangePwdRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static ChangePwdPost getApiService() {
        return getInstance().create(ChangePwdPost.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class ChangepwActivity extends AppCompatActivity implements Button.OnClickListener {
    private Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_changepw);
        activity = this;

        final EditText newPwd = (EditText) findViewById(R.id.newpw_edttxt);
        final EditText newPwdConfirm = (EditText) findViewById(R.id.confirmnewpw_edttxt);
        final Button changePwdBtn = (Button) findViewById(R.id.changepw_btn);
        changePwdBtn.setOnClickListener((View.OnClickListener) this);

        newPwdConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newPwd.getText().toString().equals(newPwdConfirm.getText().toString()))
                    changePwdBtn.setEnabled(true);
                else
                    changePwdBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(final View view) {
        EditText pwdText = (EditText) findViewById(R.id.currentpw_edttxt);
        EditText newPwdText = (EditText) findViewById(R.id.newpw_edttxt);
        String pwd, newPwd;

        if (view.getId() == R.id.changepw_btn) {
            pwd = pwdText.getText().toString();
            newPwd = newPwdText.getText().toString();
            changePwdAction(pwd, newPwd);
        }
    }

    private void changePwdAction(String pwd, String newPwd) {
        HashMap<String, Object> sendData;
        Call<Result> postResult;
        SharedPreferences sharePref = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        int usn = sharePref.getInt("usn", 0);

        sendData = new HashMap();
        sendData.put("usn", usn);
        sendData.put("pwd1", pwd);
        sendData.put("pwd2", newPwd);

        postResult = ChangePwdRetrofit.getApiService().postData(sendData);
        postResult.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    int execResult = response.body().getResult();

                    switch (execResult) {
                        case 0:
                            showSuccessMessage("NOTICE", "Successful password change");
                            break;
                        case -1:
                            showMessage("ERROR", "Query error");
                            break;
                        case -2:
                            showMessage("ERROR", "No update occurs");
                            break;
                        case -3:
                            showMessage("ERROR", "This account is already signed out.");
                            break;
                        case -4:
                            showMessage("NOTICE", "Invalid password");
                            break;
                        case -5:
                            showMessage("ERROR", "Invalid isDevice");
                            break;
                        default:
                            showMessage("ERROR", "Invalid access " + execResult);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("ERROR", "sign up retrofit error: " + t.getMessage());
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showSuccessMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
