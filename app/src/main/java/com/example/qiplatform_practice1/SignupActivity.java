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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


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

        switch (view.getId()) {
            case R.id.checkid_btn:
                String username = usernameText.getText().toString();
                Call<Object> getResult = RetrofitClient.getApiService().getData(username);
                getResult.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {//
                            int execResult = Integer.valueOf((String) response.body());

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
