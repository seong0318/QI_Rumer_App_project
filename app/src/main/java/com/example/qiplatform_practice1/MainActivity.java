package com.example.qiplatform_practice1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;
    private ImageButton menu, ib_bluetooth;
    private DrawerLayout drawer;
    private NavigationView nav;
    private Activity activity = null;
    private Intent pwchange, main, listVIew, home;

    private static MainActivity ins;

    HomeFragment homeFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ins = this;
        activity = this;
        fragmentManager = getSupportFragmentManager();
        homeFragment = (HomeFragment) fragmentManager.findFragmentById(R.id.fragmentMap);

        setContentView(R.layout.activity_main);
        locationPermissionCheck();

        if (savedInstanceState == null) {
            homeFrag = new HomeFragment(MainActivity.this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_drawer_menu, homeFrag);
            transaction.commit();
        }

        drawer = findViewById(R.id.drawer_layout);
        ib_bluetooth = findViewById(R.id.ib_bluetooth);
        nav = findViewById(R.id.nav_view);
        TextView heart = findViewById(R.id.tv_heart);
        menu = findViewById(R.id.ib_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        ib_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(MainActivity.this, Udoactivity.class);
                startActivity(itent);
            }
        });

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
                        Intent intent = new Intent(getApplicationContext(), SensorRegistration.class);
                        startActivity(intent);
                        break;

//                    case R.id.nav_sensor_list: // Sensor List View 버튼을 누른 경우
//                        listVIew = new Intent(getApplicationContext(), DeviceListView.class);
//                        startActivity(listVIew);
//                        break;

                    case R.id.nav_history:
                        Intent heart = new Intent(getApplicationContext(), History.class);
                        startActivity(heart);
                        break;


                    case R.id.nav_aqi_index:
                        Intent aqiidx = new Intent(getApplicationContext(), AQI_index.class);
                        startActivity(aqiidx);
                        break;
                }
                return true;
            }
        });
    }

    public static MainActivity getInstace() {
        return ins;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeFragment.deactivatePolar();
    }

    public void displayHR(int hr) {
        //display on the textview
        Log.e(this.getClass().getName(), "displayHR(): " + hr);
        homeFrag.displayHR(hr);

    }

    private static IntentFilter makePolarGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyPolarBleReceiver.ACTION_GATT_CONNECTED);
        intentFilter.addAction(MyPolarBleReceiver.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MyPolarBleReceiver.ACTION_HR_DATA_AVAILABLE);
        return intentFilter;
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
                    int execResult = response.body().getResult();
                    Intent intent;

                    switch (execResult) {
                        case 0:
                            editor.clear();
                            editor.commit();
                            homeFrag.deactivatePolar();

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

    public void showFailMessage(String title, String message) {
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

    public void showMessage(String title, String message) {
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

    public void locationPermissionCheck() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
    }
}
