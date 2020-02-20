package com.example.qiplatform_practice1;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragAqi extends Fragment {
    private View view;

    public static FragAqi newinstance() {
        FragAqi fragAqi = new FragAqi();
        return fragAqi;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_aqi, container, false);

        return view;
    }
}
