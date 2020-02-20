package com.example.qiplatform_practice1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    // 프래그먼트 교체를 보여주는 처리를 구현한 곳
    @NonNull
    @Override

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragHeart.newinstance();
            case 1:
                return FragAqi.newinstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    // 상단의 탭 레이아웃 인디케이터 쪽에 텍스트를 선언해 주는곳
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Heart-rate";
            case 1:
                return "AQI";
            default:
                return null;
        }
    }
}
