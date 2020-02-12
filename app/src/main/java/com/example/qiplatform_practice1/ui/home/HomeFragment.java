package com.example.qiplatform_practice1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.qiplatform_practice1.MainActivity;
import com.example.qiplatform_practice1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback {
    private static final int REQUEST_CODE_PERMISSIONS = 1000;

    Button currentlocation_btn;

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker userMarker = null;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        currentlocation_btn = view.findViewById(R.id.currentlocation_btn);
        currentlocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS
                );


                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            if (userMarker != null) userMarker.remove();
                            userMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Now Location"));
                            mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLng(myLocation));
                            mMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.zoomTo(17.0f));

                        }

                    }
                });
            }


        });
        return view;
    }
    //public void onClick


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d("Map", "onMapReady");
        mMap = googleMap;


        MarkerOptions markerOptions = new MarkerOptions();

        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

//    public onLastLocationButtonClicked(View)


}
