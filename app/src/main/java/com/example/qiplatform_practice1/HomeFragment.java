package com.example.qiplatform_practice1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import polar.com.sdk.api.model.PolarHrData;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class HomeFragment<latitude, LAT> extends Fragment implements OnMapReadyCallback {
    static GpsInfo gpsInfo;
    private static final String TAG = "HomeFragment";
    private MyPolarBleReceiver mPolarBleUpdateReceiver;
    private float mMapZoomLevel = 14;
    private GoogleMap mMap;
    private View view; //프래그먼트의 뷰 인스턴스
    private Context context;
    private ImageView heart_img;
    private ImageButton ibBluetooth;
    private TextView pm, no2, o3, co, so2, temp, status;
    private String AQI_PM25, CO, AQI_SO2, AQI_O3, AQI_NO2, SO2, O3, NO2, TEMPERATURE, AQI_CO, PM25, MAC_ADD, TIMESTAMP, LAT, LNG;
    private String result;
    private StringBuffer mOutStringBuffer;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private TextView heart;

    HomeFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        gpsInfo = new GpsInfo(context);
        gpsInfo.getLocation();
        if (!gpsInfo.isGetLocation()) {
            gpsInfo.showSettingsAlert();     // GPS setting Alert
        }
    }

    void displayHR(int hr) {
        //display on the textview
        Log.e(this.getClass().getName(), "Frag displayHR(): " + hr);
        heart.setText("" + hr);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {

        }// Inflate the layout for this fragment
        final MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

        heart = view.findViewById(R.id.tv_heart);
        heart_img = view.findViewById(R.id.heart_img);
        heart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMacAddressAndStartPolar();
            }
        });
        return view;
    }

    public void setMacAddressAndStartPolar() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle("NOTICE");
        ad.setMessage("Input mac address");
        final EditText et = new EditText(getContext());
        ad.setView(et);

        ad.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mac = et.getText().toString();
                mPolarBleUpdateReceiver = new MyPolarBleReceiver(mac, true);
                activatePolar();
                dialog.dismiss();
            }
        });

        ad.setNegativeButton("Disconnect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext().getApplicationContext(), "Disconnect Polar sensor", Toast.LENGTH_LONG).show();
                deactivatePolar();
                dialog.dismiss();
            }
        });
        ad.show();
    }

    protected void activatePolar() {
        getContext().registerReceiver(mPolarBleUpdateReceiver, makePolarGattUpdateIntentFilter());
        mPolarBleUpdateReceiver.setCaller(MainActivity.getInstace());
    }

    protected void deactivatePolar() {
        try {
            getContext().unregisterReceiver(mPolarBleUpdateReceiver);
        } catch (IllegalArgumentException e) {

        }
    }

    private static IntentFilter makePolarGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyPolarBleReceiver.ACTION_GATT_CONNECTED);
        intentFilter.addAction(MyPolarBleReceiver.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(MyPolarBleReceiver.ACTION_HR_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        status = view.findViewById(R.id.tv_status);
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
        }
    }

    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(gpsInfo.getLatitude(), gpsInfo.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(gpsInfo.getLatitude(), gpsInfo.getLongitude()))
                .title("Your device");
//                .snippet("Co " + Co + "\n" + "SO2 : " + So2 + "\n" + "NO2 : " + no2 + "O3" + o_3 + "\n" +"PM25 : " + pm25);
        mMap.addMarker(markerOptions).showInfoWindow();
//        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, mMapZoomLevel));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMapZoomLevel));
//        mMap.setOnCameraIdleListener(this);
    }
}