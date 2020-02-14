package com.example.qiplatform_practice1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ForgotPwdGet {
    @GET("/forgotpasswordhandle")
    Call<Result> getData(@Query("user_name") String username);
}

class ForgotPwdRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static ForgotPwdGet getApiService() {
        return getInstance().create(ForgotPwdGet.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class ForgotpwActivity extends AppCompatActivity {
    private Activity activity = null;
    private TextView fgSignupTxt;
    private Button sendMailBtn;
    private EditText usernameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);
        activity = this;

        fgSignupTxt = (TextView) findViewById(R.id.fgsignup_txt);
        sendMailBtn = (Button) findViewById(R.id.sendemail_btn);
        usernameEditText = (EditText) findViewById(R.id.username_edttxt);

        fgSignupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotpwActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                Call<Result> getResult = ForgotPwdRetrofit.getApiService().getData(username);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            int execResult = response.body().getResult();

                            switch (execResult) {
                                case 0:
                                    showSuccessMessage("NOTICE", "Please complete the account verification in the email provided.");
                                    break;
                                case -1:
                                    showMessage("ERROR", "Query error");
                                    break;
                                case -2:
                                    showMessage("NOTICE", "The verification process is already in progress.");
                                    break;
                                case -3:
                                    showMessage("NOTICE", "Not exist username");
                                    break;
                                case -4:
                                    showMessage("ERROR", "sending email error");
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
                Intent intent = new Intent(activity, SigninActivity.class);
                activity.startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
