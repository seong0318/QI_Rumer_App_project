package com.example.qiplatform_practice1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import com.example.qiplatform_practice1.ui.gallery.GalleryFragment;
import com.example.qiplatform_practice1.ui.home.HomeFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    ImageView menu;
    DrawerLayout drawer;
    NavigationView nav;
    JSONObject json;
    String result = "";
    String result_code;
    Intent doginfo, pwchange, main, listVIew;

    private AppBarConfiguration mAppBarConfiguration;
    private GoogleMap mMap;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.InitializeLayout();

        fragmentManager = getSupportFragmentManager();


    }

    public void InitializeLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //   drawer = findViewById(R.id.drawer_layout);
        //  menu = findViewById(R.id.ib_menu);
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        final View nav_headaer_view = navigationView.getHeaderView(0);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
//                    case R.id.nav_dog_info: // 강아지 정보 저장을 누른 경우
//                        Log.d("Home","Home doginfo is clicked");
////                        doginfo = new Intent(getApplicationContext(), DogInfo.class);
////                        startActivity(doginfo);
//                        break;

                    case R.id.nav_user_management: // 비밀번호 변경 버튼을 누른 경우
                        pwchange = new Intent(getApplicationContext(), Usermanagement.class);
                        startActivity(pwchange);
                        break;

                    case R.id.nav_sign_out: // 로그아웃 버튼을 누른 경우
//                        json = new JSONObject();
//                        try {
//                            json.put("USN", Values.USN);
//                            Log.d("usn_log", json.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            result = new PostJSON().execute("http://teame-iot.calit2.net/heartdog/app/signout", json.toString()).get();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            JSONObject json_data = new JSONObject(result);
//                            Log.d("asdf5", "receive json: " + json_data.toString());
//                            result_code = (json_data.optString("result_code"));
//                            Log.d("asdf6", "result_code: " + result_code);
//
//                        } catch (Exception e) {
//                            Log.e("Fail 3", e.toString());
//                        }
//                        if(result_code.equals("0")){
//                            Toast.makeText(MainActivity.this, "Sign out complete", Toast.LENGTH_SHORT).show();
//                            main = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(main);
//                        }
//                        else if(result_code.equals("1")){
//                            Toast.makeText(MainActivity.this, "Sign out Error", Toast.LENGTH_SHORT).show();
//                        }
//                        break;

                    case R.id.nav_sensor_regi: // Sensor Registration 메뉴를 누른 경우
//                        if (Values.bluetooth_status.equals("1")) { // 센서가 어플리케이션에 연결되어 있는 경우
//                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // dialog 창을 띄움
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which) {
//                                        case DialogInterface.BUTTON_POSITIVE: // Dialog 에서 yes 버튼을 누른 경우
//                                            JSONObject jsonObject = new JSONObject();
//                                            try {
//                                                jsonObject.put("USN", Values.USN);
//                                                jsonObject.put("DEVICE", Values.DEVICE);
//                                                jsonObject.put("MAC_ADD", Values.MAC);
//                                                Log.d("asdf1", jsonObject.toString());
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            if (Values.USN.length() > 0) {
//                                                try {
//                                                    Log.d("asdf2", jsonObject.toString());
//                                                    result = new PostJSON().execute("http://teame-iot.calit2.net/heartdog/sensor/app/registration", jsonObject.toString()).get();
//                                                    Log.d("asdf3", result);
//                                                } catch (ExecutionException e) {
//                                                    e.printStackTrace();
//                                                } catch (Exception e) {
//                                                    Log.d("asdf411", e.toString());
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                            try {
//                                                JSONObject json_data = new JSONObject(result);
//                                                Log.d("asdf5", "receive json: " + json_data.toString());
//                                                result_code = (json_data.optString("result_code"));
//                                                Values.SSN = (json_data.optString("SSN"));
//                                                Log.d("asdf6", "result_code: " + result_code);
//                                                Log.d("asdf7", "SSN: " + Values.SSN);
//
//                                            } catch (Exception e) {
//                                                Log.e("Fail 3", e.toString());
//                                            }
//                                            if (result_code.equals("0")) {
//                                                Toast.makeText(MainActivity.this, "Registration complete", Toast.LENGTH_SHORT).show();
//                                            } else if (result_code.equals("1")) {
//                                                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
//                                            }
//                                            break;
//                                        case DialogInterface.BUTTON_NEGATIVE: // dialog 창에서 no 버튼을 누른 경우
//                                            Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();
//                                            break;
//                                    }
//                                }
//                            };
//                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                            builder.setMessage("Add " + Values.DEVICE + " to sensor list").setPositiveButton("Yes", dialogClickListener)
//                                    .setNegativeButton("No", dialogClickListener).show();
//                        } else if (Values.bluetooth_status.equals("2")) { // 센서와 연결되지있지 않은 경우
//                            Toast.makeText(MainActivity.this, "Connect device first", Toast.LENGTH_LONG).show();
//                        }
//                        break;

//                    case R.id.nav_sensor_list: // Sensor List View 버튼을 누른 경우
//                        listVIew = new Intent(getApplicationContext(), DeviceListView.class);
//                        startActivity(listVIew);
//                        break;

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
            //    });
//
//    }
//
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.main, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onSupportNavigateUp() {
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
////                || super.onSupportNavigateUp();
////    }
//
//    @Override
//    public void onBackPressed() { //back 버튼 차단
//        return;

        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}




