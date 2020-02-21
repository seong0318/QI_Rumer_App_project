package com.example.qiplatform_practice1;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
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

    private float mMapZoomLevel = 14;
    GpsInfo gpsInfo;
    GoogleMap mMap;
    static View view; //프래그먼트의 뷰 인스턴스
    Context context;
    Button call;
    String device, data;
    String Co, So2, No2, o_3, pm25;
//@@//

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String result_code;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        this.context = context;
        this.gpsInfo = new GpsInfo(context);
        this.gpsInfo.getLocation();
        if (!this.gpsInfo.isGetLocation()) {
            this.gpsInfo.showSettingsAlert();     // GPS setting Alert
        }
    }

    private static final String TAG = "HomeFragment";

    // Intent request codes
//    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
//    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
//    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ImageButton ibBluetooth;
    TextView pm, no2, o3, co, so2, temp, status;
    String AQI_PM25, CO, AQI_SO2, AQI_O3, AQI_NO2, SO2, O3, NO2, TEMPERATURE, AQI_CO, PM25, MAC_ADD, TIMESTAMP, LAT, LNG;

    Double latitude;
    Double longitude;
    String result;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    private StringBuffer mOutStringBuffer;

    private BluetoothAdapter mBluetoothAdapter = null;

    private BluetoothChatService mChatService = null;
    TextView heart;

    MapView mapView = null;

    JSONObject json;


    public void displayHR(int hr) {
        //display on the textview
        Log.e(this.getClass().getName(), "Frag displayHR(): "+hr);
        heart.setText(""+hr);

    }

//
//    }

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
//        heart.setText(PolarHrDataTest.hr);


//        final Switch sw = view.findViewById(R.id.sw_alert);
//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    heart.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                            String Heart = heart.getText().toString().trim();
//                            final int rate = Integer.parseInt(Heart);
//                            if(rate >= 100) {
//                                Toast.makeText(getActivity(), "Warning", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                        @Override
//                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                            String Heart = heart.getText().toString().trim();
//                            final int rate = Integer.parseInt(Heart);
//                            if(rate >= 100) {
//                                Toast.makeText(getActivity(), "Warning", Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable editable) {
//
//                        }
//                    });
//                }
//                else {
//                }
//            }
//        });
        return view;
    }



    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        no2 = view.findViewById(R.id.tv_no2_data);
        o3 = view.findViewById(R.id.tv_o3_data);
        co = view.findViewById(R.id.tv_co_data);
        so2 = view.findViewById(R.id.tv_so2_data);
        temp = view.findViewById(R.id.tv_temp_data);
        pm = view.findViewById(R.id.tv_pm_data);
        status = view.findViewById(R.id.tv_status);
    }

    /**
     * Set up the UI and background operations for chat.
     */


    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
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

    /**
     * The action listener for the EditText widet, to listen for the return key
     */
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

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
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

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
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

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            FragmentActivity activity = getActivity();
//            switch (msg.what) {
//                case Constants1.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) { // 상단에 뜨는 블루투스 연결 상태 변경하는 부분
//                        case BluetoothChatService.STATE_CONNECTED:
//                            status.setText("connected to " + mConnectedDeviceName);
//                            Values.DEVICE = mConnectedDeviceName;
//                            Values.bluetooth_status = "1";
//                            break;
//                        case BluetoothChatService.STATE_CONNECTING:
//                            status.setText("connecting");
//                            break;
//                        case BluetoothChatService.STATE_LISTEN:
//                        case BluetoothChatService.STATE_NONE:
//                            status.setText("not connected");
//                            Values.bluetooth_status = "2";
//                            break;
//                    }
//                    break;
//                case Constants1.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    String writeMessage = new String(writeBuf);
//                    break;
//
//                // 센서로부터 블루투스 데이터를 받아오는 부분
//                case Constants1.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    String readMessage = new String(readBuf, 0, msg.arg1);
//
//                    longitude = gpsInfo.getLongitude();
//                    latitude = gpsInfo.getLatitude();
//
//                    try {
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateTime = dateFormat.format(new Date()); // Find todays date
//
//                        json = new JSONObject(readMessage);
//
//                        json.put("MAC_ADD", Values.MAC);
//                        json.put("TIMESTAMP", currentDateTime);
//                        json.put("LAT", latitude);
//                        json.put("LNG", longitude);
//
//                        Log.d("asdfasdf", json.toString());
//
//                        AQI_PM25 = json.getString("AQI_PM25");
//                        CO = json.getString("CO");
//                        AQI_SO2 = json.getString("AQI_SO2");
//                        AQI_O3 = json.getString("AQI_O3");
//                        AQI_NO2 = json.getString("AQI_NO2");
//                        SO2 = json.getString("SO2");
//                        O3 = json.getString("O3");
//                        NO2 = json.getString("NO2");
//                        TEMPERATURE = json.getString("TEMPERATURE");
//                        AQI_CO = json.getString("AQI_CO");
//                        PM25 = json.getString("PM25");
//                        MAC_ADD = json.getString("MAC_ADD");
//                        TIMESTAMP = json.getString("TIMESTAMP");
//                        LAT = json.getString("LAT");
//                        LNG = json.getString("LNG");
//
//                        pm.setText(AQI_PM25);
//                        so2.setText(AQI_SO2);
//                        o3.setText(AQI_O3);
//                        no2.setText(AQI_NO2);
//                        temp.setText(TEMPERATURE);
//                        co.setText(AQI_CO);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (Values.MAC.length() > 0) {
//                        try {
//                            Log.d("asdf2", json.toString());
//                            result = new PostJSON().execute("http://teama-iot.calit2.net/rumer", json.toString()).get();
//                            Log.d("asdf3", result);
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }catch (Exception e) {
//                            Log.d("asdf411", e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                    break;
//                case Constants1.MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants1.DEVICE_NAME);
//                    if (null != activity) {
//                        Toast.makeText(activity, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case Constants1.MESSAGE_TOAST:
//                    if (null != activity) {
//                        Toast.makeText(activity, msg.getData().getString(Constants1.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//            }
//        }
//    };

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE_SECURE:
//                // When DeviceListActivity returns with a device to connect
//                Log.d("SECURE BEFORE", "INSECURE IN RESULT IS CALLED");
//                if (resultCode == Activity.RESULT_OK) {
//                    Log.d("SECURE", "SECURE IN RESULT IS CALLED");
//                    connectDevice(data, true);
//                }
//                break;
//            case REQUEST_CONNECT_DEVICE_INSECURE:
//                // When DeviceListActivity returns with a device to connect
//                Log.d("INSECURE BEFORE", "INSECURE IN RESULT IS CALLED");
//                if (resultCode == Activity.RESULT_OK) {
//                    Log.d("INSECURE AFTER", "INSECURE IN RESULT IS CALLED");
//                    connectDevice(data, false);
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    // Bluetooth is now enabled, so set up a chat session
//
//                } else {
//                    // User did not enable Bluetooth or an error occurred
//                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
//                            Toast.LENGTH_SHORT).show();
//                    getActivity().finish();
//                }
//        }
//    }

//    /**
//     * Establish connection with other device
//     *
//     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
//     * @param secure Socket Security type - Secure (true) , Insecure (false)
//     */
//    private void connectDevice(Intent data, boolean secure) {
//        // Get the device MAC address
//        String address = data.getExtras()
//                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//
//        Values.MAC = data.getExtras()
//                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//        // Get the BluetoothDevice object
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//        // Attempt to connect to the device
//        mChatService.connect(device, secure);
//    }

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
