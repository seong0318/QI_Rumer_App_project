package com.example.qiplatform_practice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    private boolean startPolarRecord;
    private String macAddress;

    public MyPolarBleReceiver(String macAddress, boolean startPolarRecord) {
        this.macAddress = macAddress;
        this.startPolarRecord = startPolarRecord;
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
    public void onReceive(final Context ctx, Intent intent) {
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
                sendData.put("lat", gpsInfo.getLatitude());
                sendData.put("lng", gpsInfo.getLongitude());
                sendData.put("hr", heartRate);
                sendData.put("rr", lastRRvalue);
                sendData.put("mac", this.macAddress);

                getResult = InsertPolarDataRetrofit.getApiService().postData(sendData);
                getResult.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            int execResult = response.body().getResult();

                            switch (execResult) {
                                case 0:
//                                    Toast.makeText(ctx.getApplicationContext(), "Connect Polar sensor", Toast.LENGTH_LONG).show();
                                    break;
                                case -1:
                                    Log.e(TAG, "Sql query error");
                                    caller.homeFrag.deactivatePolar();
                                    break;
                                case -2:
                                    Log.e(TAG, "Duplicate primary key in polar_data table");
                                    caller.homeFrag.deactivatePolar();
                                    break;
                                case -3:
                                    Toast.makeText(ctx.getApplicationContext(), "Check your mac address", Toast.LENGTH_LONG).show();
                                    if (caller.homeFrag != null)
                                        caller.homeFrag.deactivatePolar();
                                    break;
                                default:
                                    Log.e(TAG, "Invalid access");
                                    caller.homeFrag.deactivatePolar();
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
        }
    }
}
