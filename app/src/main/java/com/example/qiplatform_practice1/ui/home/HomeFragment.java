package com.example.qiplatform_practice1.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.qiplatform_practice1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class HomeFragment extends Fragment
        implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d("Map","onMapReady");
        mMap = googleMap;

        LatLng sandiego = new LatLng(32.88, -117.23);
//32.89 -117.23
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(sandiego);
        markerOptions.title("Sandiego");
        markerOptions.snippet("IOTPlatform design");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sandiego,14F));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}