package com.example.qiplatform_practice1;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairingListView extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ListView mListView;
    List<String> pairingList;
    BluetoothAdapter myBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pairing_list_view);

        mListView =  findViewById(R.id.listview);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //블루투스 어댑터를 사용하기위에 BluetoothAdapter라는 기존 안드로이드블루투스 기능에서 DefaultAdapter를 가져온다

        //list data
        pairingList = new ArrayList<>();
        //이건 이미 페어링되어 있는 기기 목록을 가져 오는것이다.
        final Set<BluetoothDevice> pairedDevices = myBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // 루프를 돌면서 리스트뷰에 출력할 array에 계속 추가한다.
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                pairingList.add(device.getName() + "\n" + device.getAddress());//기기 이름과 맥어드레스를 추가한다.
            }
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, pairingList);//두번째 인자는 미리 정의된 레이아웃을 사용하는 것
        mListView.setAdapter(adapter);

//        //액티비티 닫기 버튼
//        bt_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //액티비티 닫기
//                finish();
//            }
//        });

        //리스트 항목 클릭시
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent pairingIntent = new Intent();
                pairingIntent.putExtra("result_msg", pairingList.get(i));
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getApplicationContext(), "연결기기: " + selectedItem, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,pairingIntent);

                finish();
                // 선택한 기기와 연결을 하자.

            }
        });

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) { //바깥레이어 클릭시 안닫히게 하는 기능
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;

        }
        return true;
    }

    @Override
    public void onBackPressed() { //back 버튼 차단
        return;
    }
}
