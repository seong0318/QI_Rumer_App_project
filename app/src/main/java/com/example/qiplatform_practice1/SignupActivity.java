package com.example.qiplatform_practice1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


interface UsernameCheckGet {
    @GET("/usernamecheck")
    Call<Result> getData(@Query("user_name") String username);
}

class UsernameCheckRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static UsernameCheckGet getApiService() {
        return getInstance().create(UsernameCheckGet.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

interface SignupPost {
    @FormUrlEncoded
    @POST("/signuphandle")
    Call<Result> postData(@FieldMap HashMap<String, Object> param);
}

class SignupRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static SignupPost getApiService() {
        return getInstance().create(SignupPost.class);
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
    private Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        activity = this;

        Button checkIdBtn = (Button) findViewById(R.id.checkid_btn);
        checkIdBtn.setOnClickListener((View.OnClickListener) this);
        final Button signupBtn = (Button) findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener((View.OnClickListener) this);

        final EditText pwdText = (EditText) findViewById(R.id.pwd_edttxt);
        final EditText pwdConfirmText = (EditText) findViewById(R.id.pwd_confirm_edttxt);

        pwdConfirmText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pwdText.getText().toString().equals(pwdConfirmText.getText().toString()))
                    signupBtn.setEnabled(true);
                else
                    signupBtn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(final View view) {
        final TextView usernameText = (TextView) findViewById(R.id.usn_edttxt);
        TextView emailText = (TextView) findViewById(R.id.email_edttxt);
        TextView pwdText = (TextView) findViewById(R.id.pwd_edttxt);
        String username, email, pwd;
        Call<Result> getResult;
        HashMap<String, Object> formData;
        Gson g = new Gson();

        switch (view.getId()) {
            case R.id.checkid_btn:
                username = usernameText.getText().toString();
                getResult = UsernameCheckRetrofit.getApiService().getData(username);
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

                if (findViewById(R.id.checkid_btn).isEnabled()) {
                    showMessage("Notice", "Please check id duplication first");
                    return;
                }

                formData = new HashMap();
                formData.put("user_name", username);
                formData.put("email", email);
                formData.put("pwd", pwd);

                getResult = SignupRetrofit.getApiService().postData(formData);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            int execResult = response.body().getResult();
//
                            switch (execResult) {
                                case 0:
                                    showSuccessMessage("Successful Registration", "Please complete the account verification in the email provided.");
                                    break;
                                case -1:
                                    showMessage("ERROR", "Store user Query error");
                                    break;
                                case -2:
                                    showMessage("ERROR", "Store temp_user Query error");
                                    break;
                                case -4:
                                    showMessage("ERROR", "Send mail error");
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
                break;
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
