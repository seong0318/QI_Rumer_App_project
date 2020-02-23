package com.example.qiplatform_practice1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

interface IdCancellationPost {
    @FormUrlEncoded
    @POST("/idcancelhandle/1")
    Call<Result> postData(@FieldMap HashMap<String, Object> param);
}

class IdCancellationRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static IdCancellationPost getApiService() {
        return getInstance().create(IdCancellationPost.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class Idcancellation extends AppCompatActivity implements Button.OnClickListener {
    private Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcancellation);
        activity = this;

        Button idCancelBtn = (Button) findViewById(R.id.idcancellation_btn);
        idCancelBtn.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View view) {
        String pwd;
        EditText pwdText = (EditText) findViewById(R.id.pwd_edttxt);

        if (view.getId() == R.id.idcancellation_btn) {
            pwd = pwdText.getText().toString();
            idCancellationAction(pwd);
        }
    }

    private void idCancellationAction(String pwd) {
        HashMap<String, Object> sendData;
        Call<Result> postResult;
        SharedPreferences sharePref = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        int usn = sharePref.getInt("usn", 0);

        sendData = new HashMap();
        sendData.put("usn", usn);
        sendData.put("pwd", pwd);

        postResult = IdCancellationRetrofit.getApiService().postData(sendData);
        postResult.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    int execResult = response.body().getResult();

                    switch (execResult) {
                        case 0:
                            showSuccessMessage("NOTICE", "Successful ID Cancellation");
                            break;
                        case -1:
                            showMessage("ERROR", "Query error");
                            break;
                        case -2:
                            showMessage("NOTICE", "Invalid password");
                            break;
                        case -3:
                            showMessage("NOTICE", "Please login first");
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

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
