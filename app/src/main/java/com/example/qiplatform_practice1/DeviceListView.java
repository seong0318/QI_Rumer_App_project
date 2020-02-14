package com.example.qiplatform_practice1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DeviceListView extends AppCompatActivity {

    private ListView m_oListView = null;
    String result = "";
    String ssn, mac_add, device, result_code;
    ArrayList<ItemData> oData;
    Button delete;
    Intent chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list_view);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("USN", Values.USN);
            Log.d("asdf1", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Values.USN.length() > 0) {
            try {
                Log.d("asdf2", jsonObject.toString());
                result = new PostJSON().execute("http://teame-iot.calit2.net/heartdog/sensor/app/listview", jsonObject.toString()).get();
                Log.d("asdf3", result);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Log.d("asdf411", e.toString());
                e.printStackTrace();
            }
        }
        try {
            oData = new ArrayList<>();
            JSONArray jarray = new JSONArray(result);

            if (!result.equals(null)) {
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject json_data = jarray.getJSONObject(i);

                    ssn = json_data.getString("SSN");
                    mac_add = json_data.getString("MAC_ADD");
                    device = json_data.getString("DEVICE");

                    ItemData oItem = new ItemData(ssn, mac_add, device);

                    oItem.setStrTitle(device);
                    oItem.setStrDate(mac_add);
                    oItem.setStrNumber(ssn);
                    oData.add(oItem);
                }
            } else {
                Toast.makeText(DeviceListView.this, "No registered device", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }

        m_oListView = findViewById(R.id.Listview);
        final ListAdapter oAdapter = new ListAdapter(oData);
        m_oListView.setAdapter(oAdapter);

        m_oListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                ItemData item = (ItemData) adapterView.getItemAtPosition(position);
                TextView number = view.findViewById(R.id.tv_number);
                final String ssn = number.getText().toString().trim();

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // dialog 창을 띄움
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE: // Dialog 에서 yes 버튼을 누른 경우

                                try {
                                    jsonObject.put("USN", Values.USN);
                                    jsonObject.put("SSN", ssn);
                                    Log.d("asdf1", jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (ssn.length() > 0) {
                                    try {
                                        Log.d("asdf2", jsonObject.toString());
                                        result = new PostJSON().execute("http://teama-iot.calit2.net", jsonObject.toString()).get();
                                        Log.d("asdf3", result);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        Log.d("asdf411", e.toString());
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
                                    if (result_code.equals("0")) {
                                        Toast.makeText(DeviceListView.this, "Deregistration complete", Toast.LENGTH_SHORT).show();
                                        oData.remove(position);
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);

                                    } else if (result_code.equals("1")) {
                                        Toast.makeText(DeviceListView.this, "Deregistration error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE: // dialog 창에서 no 버튼을 누른 경우
                                Toast.makeText(DeviceListView.this, "Cancel", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceListView.this);
                builder.setMessage("Really Deregistration device ?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });

        m_oListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                TextView title = view.findViewById(R.id.textTitle);
                Values.DEVICE_LIST = title.getText().toString().trim();

                TextView number = view.findViewById(R.id.tv_number);
                Values.SSN = number.getText().toString().trim();
                final String ssn = number.getText().toString().trim();

//                Log.d("device", Values.DEVICE_LIST);
//                Intent chart = new Intent(getApplicationContext(), HistoryChart.class);
//                startActivity(chart);
            }
        });
    }
}

