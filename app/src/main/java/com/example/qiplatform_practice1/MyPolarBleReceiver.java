package com.example.qiplatform_practice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class MyPolarBleReceiver extends BroadcastReceiver {
    public final static String ACTION_GATT_CONNECTED =
            "edu.ucsd.healthware.fw.device.ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "edu.ucsd.healthware.fw.device.ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_HR_DATA_AVAILABLE =
            "edu.ucsd.healthware.fw.device.ble.ACTION_HR_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "edu.ucsd.healthware.fw.device.ble.EXTRA_DATA";

    MainActivity caller;


    GpsInfo gpsInfo;
    String usn = Values.USN;

    String result = "";
    String result_code;

    public void setCaller(MainActivity caller) {
        this.caller = caller;
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        final String action = intent.getAction();
        if (ACTION_GATT_CONNECTED.equals(action)) {
            Log.w(this.getClass().getName(), "####ACTION_GATT_CONNECTED");
        } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
        } else if (ACTION_HR_DATA_AVAILABLE.equals(action)) {
            //broadcastUpdate(ACTION_HR_DATA_AVAILABLE, heartRate+";"+pnnPercentage+";"+pnnCount+";"+rrThreshold+";"+bioHarnessSessionData.totalNN+";"+bioHarnessSessionData.lastRRvalue+";"+bioHarnessSessionData.sessionId);
            String data = intent.getStringExtra(EXTRA_DATA);
            StringTokenizer tokens = new StringTokenizer(data, ";");
            String heartRate = tokens.nextToken();
            int pnnPercentage = Integer.parseInt(tokens.nextToken());
            int pnnCount = Integer.parseInt(tokens.nextToken());
            int rrThreshold = Integer.parseInt(tokens.nextToken());
            int totalNN = Integer.parseInt(tokens.nextToken());
            int lastRRvalue = Integer.parseInt(tokens.nextToken());
            String sessionId = tokens.nextToken();
            Log.w(this.getClass().getName(), "####Received heartRate: " +heartRate+" pnnPercentage: "+pnnPercentage+" pnnCount: "+pnnCount+" rrThreshold: "+rrThreshold+" totalNN: "+totalNN+" lastRRvalue: "+lastRRvalue+" sessionId: "+sessionId);

            JSONObject jsonObject = new JSONObject();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date()); // Find todays date

                jsonObject.put("USN", usn);
                jsonObject.put("TIMESTAMP", currentDateTime);
                jsonObject.put("HEART_RATE", heartRate);
                Log.d("asdf1", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (usn.length() > 0) {
                try {
                    Log.d("asdf2", jsonObject.toString());
                    result = new PostJSON().execute("http://teame-iot.calit2.net/heartdog/heartrate/transfer", jsonObject.toString()).get();
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
                Log.d("asdf6", "result_code: " + result_code);

            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
            try {
                MainActivity.getInstace().updateTheTextView(heartRate);
            } catch (Exception e) {
            }
        }
    }
}