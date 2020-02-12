package com.example.qiplatform_practice1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface SigninPost {
    @FormUrlEncoded
    @POST("/signinhandle")
    Call<Result> postData(@FieldMap HashMap<String, Object> param);
}

class SigninRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static SigninPost getApiService() {
        return getInstance().create(SigninPost.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class SigninActivity extends AppCompatActivity implements Button.OnClickListener {
    Activity activity = null;
    EditText usn_edttxt, pwd_edttxt;
    Button signin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        activity = this;

        usn_edttxt = findViewById(R.id.usn_edttxt);
        pwd_edttxt = findViewById(R.id.pwd_edttxt);
        signin_btn = findViewById(R.id.signin_btn);
    }

    public void onClick(View view) {
        String username = usn_edttxt.getText().toString();
        String pwd = pwd_edttxt.getText().toString();
        Call<Result> getResult;
        HashMap<String, Object> formData;

        switch (view.getId()) {
            case R.id.forgotpwd_txt: {
                Intent intent = new Intent(SigninActivity.this, ForgotpwActivity.class);
                startActivity(intent);
                return;
            }
            case R.id.signup_txt: {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
                return;
            }

            case R.id.signin_btn: {
                formData = new HashMap();
                formData.put("user_name", username);
                formData.put("pwd", pwd);

                getResult = SigninRetrofit.getApiService().postData(formData);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            int execResult = response.body().getResult();

                            switch (execResult) {
                                case 0:
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                    break;
                                case -1:
                                    showMessage("ERROR", "Query error");
                                    break;
                                case -2:
                                    showMessage("NOTICE", "Invalid username or password");
                                    break;
                                case -3:
                                    showMessage("NOTICE", "Please complete email verification first");
                                    break;
                                default:
                                    showMessage("ERROR", "Invalid access: " + execResult);
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

//            Intent intent = new Intent(SigninActivity.this, PopupActivity.class);
//            intent.putExtra("username", username);
//            intent.putExtra("password", pwd);
//            startActivityForResult(intent, 1);
//            break;
        }
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

    public void onBackPressed() {


        //int i=90;
        finish();
    }
}