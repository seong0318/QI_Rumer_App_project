package com.example.qiplatform_practice1;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class SensorRegistration extends AppCompatActivity {
    final String TAG = "SensorRegistration";
    int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter myBluetoothAdapter;
    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    TextView asdfasdf;
    private String deviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_reg);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothPairing();

        mRecyclerView = findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(SensorRegistration.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new CustomAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        Button buttonInsert = (Button) findViewById(R.id.devicereg_btn);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringTokenizer tokens = new StringTokenizer(deviceInfo, "\n");
                String deviceName = tokens.nextToken();
                String deviceAddr = tokens.nextToken();

                Dictionary data = new Dictionary(deviceName, deviceAddr);

                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
                mArrayList.add(data); // RecyclerView의 마지막 줄에 삽입

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void bluetoothPairing() {
        Button bt_pairing = findViewById(R.id.pairedlist_btn);
        bt_pairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1.블루투스가 활성화 되어 있어야 한다.
                //2.새로운 액티비티를 열어서 페어링된 기기 목록을 보여 준다. 리스트뷰 사용함
                //3.새로운 액티비티에서 기기를 연결한다.
                //4.새로운 액티비티를 닫는다. 원하는 기기와의 연결확인.
                //이미 페어링된 기기가 없으면 새로 기기를 검색해야 한다. 여기서는 다루지 않는다.
                //즉, 다른 기기를 검색하고 페어링하는 단계는 폰의 내장된 블루투스 메뉴에서 하라는 말이다.
                if (myBluetoothAdapter.isEnabled()) {
                    //새로운 액티비티를 연다.
                    Intent pairingIntent = new Intent(SensorRegistration.this, PairingListView.class);
                    startActivityForResult(pairingIntent, REQUEST_ENABLE_BT);
                } else {
                    Toast.makeText(getApplicationContext(), "Activate the Bluetooth,first.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        deviceInfo = data.getStringExtra("result_msg");
        asdfasdf = findViewById(R.id.asdfasdf);

        asdfasdf.setText(deviceInfo);
    }
}