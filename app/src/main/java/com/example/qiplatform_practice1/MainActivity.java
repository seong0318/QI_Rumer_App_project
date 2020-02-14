package com.example.qiplatform_practice1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity {

    ImageButton menu;
    DrawerLayout drawer;
    NavigationView nav;
    JSONObject json;
    String result = "";
    String result_code;
    Intent pwchange, main, listVIew, home;
    TextView heart;

    private static MainActivity ins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ins = this;

        setContentView(R.layout.activity_main);
        locationPermissionCheck();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_drawer_menu, new HomeFragment(MainActivity.this));
            transaction.commit();
        }
        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);
        heart = findViewById(R.id.tv_heart);

        menu = findViewById(R.id.ib_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        activatePolar();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
//

                    case R.id.nav_user_management: // 비밀번호 변경 버튼을 누른 경우
                        pwchange = new Intent(getApplicationContext(), Usermanagement.class);
                        startActivity(pwchange);
                        break;

                    case R.id.nav_sign_out: // 로그아웃 버튼을 누른 경우
                        json = new JSONObject();
                        try {
                            json.put("USN", Values.USN);
                            Log.d("usn_log", json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            result = new PostJSON().execute("http://teama-iot.calit2.net/signin", json.toString()).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject json_data = new JSONObject(result);
                            Log.d("asdf5", "receive json: " + json_data.toString());
                            result_code = (json_data.optString("result_code"));
                            Log.d("asdf6", "result_code: " + result_code);

                        } catch (Exception e) {
                            Log.e("Fail 3", e.toString());
                        }
                        if(result_code.equals("0")){
                            Toast.makeText(MainActivity.this, "Sign out complete", Toast.LENGTH_SHORT).show();
                            main = new Intent(getApplicationContext(), SigninActivity.class);
                            startActivity(main);
                        }
                        else if(result_code.equals("1")){
                            Toast.makeText(MainActivity.this, "Sign out Error", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.nav_sensor_regi: // Sensor Registration 메뉴를 누른 경우
                        if(Values.bluetooth_status.equals("1")) { // 센서가 어플리케이션에 연결되어 있는 경우
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // dialog 창을 띄움
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE: // Dialog 에서 yes 버튼을 누른 경우
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("USN", Values.USN);
                                                jsonObject.put("DEVICE", Values.DEVICE);
                                                jsonObject.put("MAC_ADD", Values.MAC);
                                                Log.d("asdf1", jsonObject.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (Values.USN.length() > 0) {
                                                try {
                                                    Log.d("asdf2", jsonObject.toString());
                                                    result = new PostJSON().execute("http://teame-iot.calit2.net/heartdog/sensor/app/registration", jsonObject.toString()).get();
                                                    Log.d("asdf3", result);
                                                } catch (ExecutionException e) {
                                                    e.printStackTrace();
                                                }catch (Exception e) {
                                                    Log.d("asdf411", e.toString());
                                                    e.printStackTrace();
                                                }
                                            }
                                            try {
                                                JSONObject json_data = new JSONObject(result);
                                                Log.d("asdf5", "receive json: " + json_data.toString());
                                                result_code = (json_data.optString("result_code"));
                                                Values.SSN = (json_data.optString("SSN"));
                                                Log.d("asdf6", "result_code: " + result_code);
                                                Log.d("asdf7", "SSN: " + Values.SSN);

                                            } catch (Exception e) {
                                                Log.e("Fail 3", e.toString());
                                            }
                                            if(result_code.equals("0")){
                                                Toast.makeText(MainActivity.this, "Registration complete", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(result_code.equals("1")){
                                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE: // dialog 창에서 no 버튼을 누른 경우
                                            Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Add " + Values.DEVICE + " to sensor list").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                        else if(Values.bluetooth_status.equals("2")) { // 센서와 연결되지있지 않은 경우
                            Toast.makeText(MainActivity.this, "Connect device first", Toast.LENGTH_LONG).show();
                        }
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
                        Intent aqi = new Intent(getApplicationContext(), AQI_index.class);
                        startActivity(aqi);
                        break;
                }
                return true;
            }
        });
    }

    public static MainActivity getInstace(){
        return ins;
    }

    public void updateTheTextView(final String t) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                heart = findViewById(R.id.tv_heart);
                heart.setText(t);

                try {
                    JSONObject json = new JSONObject(t);
                    Log.d("asdf123", String.valueOf(json));
                    result = new PostJSON().execute("http://teame-iot.calit2.net/heartdog/heartrate/transfer", json.toString()).get();
                    Log.d("asdf1234", result);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                JSONObject json_data = null;
                try {
                    json_data = new JSONObject(result);
                    Log.d("asdf5", "receive json: " + json_data.toString());
                    result_code = (json_data.optString("result_code"));
                    Log.d("asdf6", "result_code: " + result_code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final MyPolarBleReceiver mPolarBleUpdateReceiver = new MyPolarBleReceiver() {};

    protected void activatePolar() {
        Log.w(this.getClass().getName(), "activatePolar()");
        registerReceiver(mPolarBleUpdateReceiver, makePolarGattUpdateIntentFilter());
        mPolarBleUpdateReceiver.setCaller(this);
    }
    private static IntentFilter makePolarGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyPolarBleReceiver.ACTION_GATT_CONNECTED);
        intentFilter.addAction(MyPolarBleReceiver.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MyPolarBleReceiver.ACTION_HR_DATA_AVAILABLE);
        return intentFilter;
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



