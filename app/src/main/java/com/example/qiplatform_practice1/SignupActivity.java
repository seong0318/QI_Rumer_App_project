package com.example.qiplatform_practice1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qiplatform_practice1.Url;
import com.example.qiplatform_practice1.URLConnector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

class checkDuplicate {
    private final String isDup;

    public checkDuplicate(String isDup) {
        this.isDup = isDup;
    }

    public String getIsDup() {
        return isDup;
    }
}

class SignupData {
    private final String username;
    private final String email;
    private final String pwd;
    private final String confirm_pwd;

    public SignupData(String username, String email, String pwd, String confirm_pwd) {
        this.username = username;
        this.email = email;
        this.pwd = pwd;
        this.confirm_pwd = confirm_pwd;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public String getConfirm_pwd() {
        return confirm_pwd;
    }
}

interface RetrofitExService {
    String url = new Url().getUrl();

    @GET("/usernamecheck/1")
    Call<Object> getData(@Query("user_name") String username);
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

public class SignupActivity extends AppCompatActivity implements Button.OnClickListener {
    private Url url = new Url();

    public String getUrl() {
        return url.getUrl();
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        checkid_btn = findViewById(R.id.checkid_btn);
//        signinhere = findViewById(R.id.gosignin_txt);
//        usn_edttxt = findViewById(R.id.usn_edttxt);
//        signup_btn = findViewById(R.id.signup_btn);
//
////        Url url = new Url();
////        URLConnector task = new URLConnector(url + "signup");
////
////        task.start();
////
////        try {
////            task.join();
////            System.out.println("waiting...");
////        } catch (InterruptedException e) {
////            System.out.println(e);
////        }
////
////        String result = task.getResult();
////        System.out.println(result);
//
//        checkid_btn.setOnClickListener(new View.OnClickListener() {
//            Url url = new Url();
//            URLConnector task = new URLConnector(url + "signup");
//
//            task.start();
//
//            try {
//                task.join();
//                System.out.println("waiting...");
//            } catch (InterruptedException e) {
//                System.out.println(e);
//            }
//
//            String result = task.getResult();
//            System.out.println(result);
//        });
//
//        signinhere.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
//
//                startActivityForResult(intent, 1);
//
//                String username = usn_edttxt.getText().toString();
//
//                return;
//
//            }
//        });
//
//        signup_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignupActivity.this, VerifyPopupActivity.class);
//                startActivityForResult(intent, 1);
//                return;
//
//            }
//        });
//    }
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
    public void onClick(View view) {
        TextView usernameText = (TextView) findViewById(R.id.usn_edttxt);
        switch (view.getId()) {
            case R.id.checkid_btn:
                final String username = usernameText.getText().toString();
                Call<Object> getResult = RetrofitClient.getApiService().getData(username);
                getResult.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            System.out.println("username: " + username);
                            System.out.println("data: " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.e("ERROR", "retrofit error");
                    }
                });

                break;
            case R.id.signup_btn:
                System.out.println("2");
                break;
        }
    }
}
