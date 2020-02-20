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
    private GoogleMap mMap;
    static View view; //프래그먼트의 뷰 인스턴스
    public Context context;
    private BluetoothAdapter mBluetoothAdapter = null;
    TextView heart;

    public HomeFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        this.context = context;
    }

    public void displayHR(int hr) {
        //display on the textview
        Log.e(this.getClass().getName(), "Frag displayHR(): " + hr);
        heart.setText("" + hr);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
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


        return view;
    }

//    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        LatLng location = new LatLng(gpsInfo.getLatitude(), gpsInfo.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(new LatLng(gpsInfo.getLatitude(), gpsInfo.getLongitude()))
//                .title("Your device");
////                .snippet("Co " + Co + "\n" + "SO2 : " + So2 + "\n" + "NO2 : " + no2 + "O3" + o_3 + "\n" +"PM25 : " + pm25);
//        mMap.addMarker(markerOptions).showInfoWindow();
////        mMap.addMarker(new MarkerOptions().position(location));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, mMapZoomLevel));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMapZoomLevel));
////        mMap.setOnCameraIdleListener(this);
    }
}
