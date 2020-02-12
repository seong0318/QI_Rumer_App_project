package com.example.qiplatform_practice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qiplatform_practice1.Url;
import com.example.qiplatform_practice1.URLConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

class Result {
    private int result;

    public Result(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}

interface RetrofitExService {
    @GET("/usernamecheck/1")
    Call<Result> getData(@Query("user_name") String username);
}

class RetrofitClient {
    private static final String baseUrl = new Url().getUrl();

    public static RetrofitExService getApiService() {
        return getInstance().create(RetrofitExService.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

interface RetrofitPost {
    @FormUrlEncoded
    @POST("/signuphandle/1")
    Call<Result> postData(@FieldMap HashMap<String, Object> param);
}

class RetrofitSignup {
    private static final String baseUrl = new Url().getUrl();

    public static RetrofitPost getApiService() {
        return getInstance().create(RetrofitPost.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class SignupActivity extends AppCompatActivity implements Button.OnClickListener {
    private Url url = new Url();

    public String getUrl() {
        return url.getUrl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button checkIdBtn = (Button) findViewById(R.id.checkid_btn);
        checkIdBtn.setOnClickListener((View.OnClickListener) this);
        Button signupBtn = (Button) findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(final View view) {
        final TextView usernameText = (TextView) findViewById(R.id.usn_edttxt);
        TextView emailText = (TextView) findViewById(R.id.email_edttxt);
        TextView pwdText = (TextView) findViewById(R.id.pwd_edttxt);
        TextView pwdConfirmText = (TextView) findViewById(R.id.pwd_confirm_edttxt);
        String username, email, pwd, pwd_confirm;
        Call<Result> getResult;
        HashMap<String, Object> formData;
        Gson g = new Gson();

        switch (view.getId()) {
            case R.id.checkid_btn:
                username = usernameText.getText().toString();
                getResult = RetrofitClient.getApiService().getData(username);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {//
                            int execResult = response.body().getResult();

                            if (execResult == 0) {
                                showMessage("Usable username", "Please continue");

                                Button button = (Button) findViewById(view.getId());
                                button.setEnabled(false);
                                usernameText.setEnabled(false);
                            } else if (execResult < 0) {
                                showMessage("ERROR", "Query error");
                            } else {
                                showMessage("Duplicate username", "Please enter username again");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Log.e("ERROR", "username duplicate check retrofit error" + t.getMessage());
                    }
                });
                break;
            case R.id.signup_btn:
                username = usernameText.getText().toString();
                email = emailText.getText().toString();
                pwd = pwdText.getText().toString();
                pwd_confirm = pwdConfirmText.getText().toString();

                formData = new HashMap();
                formData.put("user_name", username);
                formData.put("email", email);
                formData.put("pwd", pwd);

                getResult = RetrofitSignup.getApiService().postData(formData);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            int execResult = response.body().getResult();
//
                            if (execResult == 0)
                                showMessage("Successful Registration", "Please complete the account verification in the email provided.");
                            else if (execResult == -1)
                                showMessage("ERROR", "Store user Query error");
                            else if (execResult == -2)
                                showMessage("ERROR", "Store temp_user Query error");
                            else if (execResult == -4)
                                showMessage("ERROR", "Send mail error");
                            else
                                showMessage("ERROR", "Invalid access " + execResult);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Log.e("ERROR", "sign up retrofit error: " + t.getMessage());
                    }
                });
                break;
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
}
