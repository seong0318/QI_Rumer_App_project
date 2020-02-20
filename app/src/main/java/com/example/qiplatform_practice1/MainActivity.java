package com.example.qiplatform_practice1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;

import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface SignoutGet {
    @GET("/signout/1")
    Call<Result> getData(@Query("usn") int usn);
}

class SignoutRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static SignoutGet getApiService() {
        return getInstance().create(SignoutGet.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class MainActivity extends FragmentActivity {
    private DrawerLayout drawer;
    private Activity activity = null;
    private Intent pwchange, listVIew;
    private String DEVICE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity ins = this;
        activity = this;
        setContentView(R.layout.activity_main);

        locationPermissionCheck();

        PolarBleApi api = PolarBleApiDefaultImpl.defaultImplementation(this, PolarBleApi.FEATURE_HR);
        api.setApiCallback(new PolarBleApiCallback() {
            @Override
            public void blePowerStateChanged(boolean powered) {
                super.blePowerStateChanged(powered);
                Log.d("MyApp", "BLE power: " + powered);
            }

            @Override
            public void deviceConnected(PolarDeviceInfo polarDeviceInfo) {
                super.deviceConnected(polarDeviceInfo);
                Log.d("MyApp", "Connected: " + polarDeviceInfo.deviceId);
                DEVICE_ID = polarDeviceInfo.deviceId;
            }

            @Override
            public void deviceConnecting(PolarDeviceInfo polarDeviceInfo) {
                super.deviceConnecting(polarDeviceInfo);
                Log.d("MyApp", "Connecting: " + polarDeviceInfo.deviceId);
                DEVICE_ID = polarDeviceInfo.deviceId;
            }

            @Override
            public void deviceDisconnected(PolarDeviceInfo polarDeviceInfo) {
                super.deviceDisconnected(polarDeviceInfo);
                Log.d("MyApp", "Disconnected: " + polarDeviceInfo.deviceId);
            }

            @Override
            public void ecgFeatureReady(String identifier) {
                super.ecgFeatureReady(identifier);
            }

            @Override
            public void accelerometerFeatureReady(String identifier) {
                super.accelerometerFeatureReady(identifier);
            }

            @Override
            public void ppgFeatureReady(String identifier) {
                super.ppgFeatureReady(identifier);
            }

            @Override
            public void ppiFeatureReady(String identifier) {
                super.ppiFeatureReady(identifier);
            }

            @Override
            public void biozFeatureReady(String identifier) {
                super.biozFeatureReady(identifier);
            }

            @Override
            public void hrFeatureReady(String identifier) {
                super.hrFeatureReady(identifier);
            }

            @Override
            public void disInformationReceived(String identifier, UUID uuid, String value) {
                super.disInformationReceived(identifier, uuid, value);
            }

            @Override
            public void batteryLevelReceived(String identifier, int level) {
                super.batteryLevelReceived(identifier, level);
            }

            @Override
            public void hrNotificationReceived(String identifier, PolarHrData data) {
                super.hrNotificationReceived(identifier, data);
                Log.d("MyApp", "HR: " + data.hr);
            }

            @Override
            public void polarFtpFeatureReady(String identifier) {
                super.polarFtpFeatureReady(identifier);
            }
        });

        if (savedInstanceState == null) {
            HomeFragment homeFrag = new HomeFragment(MainActivity.this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_drawer_menu, homeFrag);
            transaction.commit();
        }

        drawer = findViewById(R.id.drawer_layout);

        ImageButton ib_bluetooth = findViewById(R.id.ib_bluetooth);
        ib_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageButton menu = findViewById(R.id.ib_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_user_management: // 비밀번호 변경 버튼을 누른 경우
                        pwchange = new Intent(getApplicationContext(), Usermanagement.class);
                        startActivity(pwchange);
                        break;

                    case R.id.nav_sign_out: // 로그아웃 버튼을 누른 경우
                        signOutAction();
                        break;

                    case R.id.nav_sensor_regi: // Sensor Registration 메뉴를 누른 경우
                        break;

                    case R.id.nav_sensor_list: // Sensor List View 버튼을 누른 경우
                        listVIew = new Intent(getApplicationContext(), DeviceListView.class);
                        startActivity(listVIew);
                        break;

//                    case R.id.nav_heart:
//                        Intent heart = new Intent(getApplicationContext(), HeartHistory.class);
//                        startActivity(heart);
//                        break;

                    case R.id.nav_aqi_index:
                        Intent aqi = new Intent(getApplicationContext(), AqiIndex.class);
                        startActivity(aqi);
                        break;
                }
                return true;
            }
        });
    }

    public void signOutAction() {
        SharedPreferences sharePref = getSharedPreferences("SHARE_PREF", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharePref.edit();
        int usn = sharePref.getInt("usn", 0);
        Call<Result> getResult = SignoutRetrofit.getApiService().getData(usn);

        getResult.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    int execResult = response.body().getResult();
                    Intent intent;

                    switch (execResult) {
                        case 0:
                            editor.clear();
                            editor.commit();

                            intent = new Intent(activity, SigninActivity.class);
                            activity.startActivity(intent);
                            break;
                        case -1:
                            showMessage("ERROR", "Query error");
                            break;
                        case -2:
                            showMessage("NOTICE", "This account is already signed out");
                            break;
                        case -3:
                            showMessage("ERROR", "Update query error");
                            break;
                        case -4:
                            showFailMessage("NOTICE", "Please sign in first");
                            break;
                        default:
                            showMessage("ERROR", "Invalid access: " + execResult);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("ERROR", "username duplicate check retrofit error" + t.getMessage());
            }
        });
    }

    private void showFailMessage(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, SigninActivity.class);
                activity.startActivity(intent);
            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMessage(String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void locationPermissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }
}



