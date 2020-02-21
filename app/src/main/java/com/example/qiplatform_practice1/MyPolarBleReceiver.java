package com.example.qiplatform_practice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.StringTokenizer;

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
import retrofit2.http.Tag;

import static android.content.Context.MODE_PRIVATE;

interface InsertPolarDataPost {
    @FormUrlEncoded
    @POST("/sensor/insert/polar")
    Call<Result> postData(@FieldMap HashMap<String, Object> param);
}

class InsertPolarDataRetrofit {
    private static final String baseUrl = new Url().getUrl();

    public static InsertPolarDataPost getApiService() {
        return getInstance().create(InsertPolarDataPost.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}

public class MyPolarBleReceiver extends BroadcastReceiver {
    private String TAG = "MyPolarBleReceiver";
    private int usn;
    private boolean startPolarRecord;
    private String macAddress;

    public MyPolarBleReceiver(String macAddress, int usn, boolean startPolarRecord) {
        this.macAddress = macAddress;
        this.startPolarRecord = startPolarRecord;
        this.usn = usn;
    }

    public final static String ACTION_GATT_CONNECTED =
            "edu.ucsd.healthware.fw.device.ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "edu.ucsd.healthware.fw.device.ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_HR_DATA_AVAILABLE =
            "edu.ucsd.healthware.fw.device.ble.ACTION_HR_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "edu.ucsd.healthware.fw.device.ble.EXTRA_DATA";

    MainActivity caller;

    public void setCaller(MainActivity caller) {
        this.caller = caller;
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        GpsInfo gpsInfo = HomeFragment.gpsInfo;

        final String action = intent.getAction();
        if (ACTION_GATT_CONNECTED.equals(action)) {
            Log.w(this.getClass().getName(), "####ACTION_GATT_CONNECTED");
        } else if (ACTION_GATT_DISCONNECTED.equals(action)) {

        } else if (ACTION_HR_DATA_AVAILABLE.equals(action)) {
            //broadcastUpdate(ACTION_HR_DATA_AVAILABLE, heartRate+";"+pnnPercentage+";"+pnnCount+";"+rrThreshold+";"+bioHarnessSessionData.totalNN+";"+bioHarnessSessionData.lastRRvalue+";"+bioHarnessSessionData.sessionId);
            String data = intent.getStringExtra(EXTRA_DATA);
            StringTokenizer tokens = new StringTokenizer(data, ";");
            int heartRate = Integer.parseInt(tokens.nextToken());
            int pnnPercentage = Integer.parseInt(tokens.nextToken());
            int pnnCount = Integer.parseInt(tokens.nextToken());
            int rrThreshold = Integer.parseInt(tokens.nextToken());
            int totalNN = Integer.parseInt(tokens.nextToken());
            int lastRRvalue = Integer.parseInt(tokens.nextToken());
            String sessionId = tokens.nextToken();

            caller.displayHR(heartRate);

            if (startPolarRecord) {
                HashMap<String, Object> sendData = new HashMap<>();
                Call<Result> getResult;
//                Log.w("ddddddddddddddddd", "lat: " + gpsInfo.getLatitude() + "lng: " + gpsInfo.getLongitude());
                sendData.put("lat", gpsInfo.getLatitude());
                sendData.put("lng", gpsInfo.getLongitude());
                sendData.put("hr", heartRate);
                sendData.put("rr", lastRRvalue);
                sendData.put("usn", this.usn);
                sendData.put("mac", this.macAddress);

                getResult = InsertPolarDataRetrofit.getApiService().postData(sendData);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            int execResult = response.body().getResult();
                            Log.w("dddddddddddddddd", "execResult: " + execResult);

                            switch (execResult) {
                                case 0:
                                    break;
                                case -1:
                                    Log.e(TAG, "Sql query error");
                                    break;
                                case -2:
                                    Log.e(TAG, "Duplicate primary key in polar_data table");
                                    break;
                                case -3:
                                    Log.e(TAG, "not insert");
                                    break;
                                default:
                                    Log.e(TAG, "Invalid access");
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Log.e("ERROR", "username duplicate check retrofit error" + t.getMessage());
                    }
                });
//                Log.w(this.getClass().getName(), "####Mac address" + this.macAddress + "Received heartRate: " + heartRate + " pnnPercentage: " + pnnPercentage + " pnnCount: " + pnnCount + " rrThreshold: " + rrThreshold + " totalNN: " + totalNN + " lastRRvalue: " + lastRRvalue + " sessionId: " + sessionId);
            }
        }
    }
}
