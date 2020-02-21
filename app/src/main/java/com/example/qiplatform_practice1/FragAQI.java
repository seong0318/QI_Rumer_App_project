package com.example.qiplatform_practice1;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class FragAQI extends Fragment {
    private View view;
    private TextView fromdate_txt2, todate_txt2;
    private DatePickerDialog.OnDateSetListener callbackMethod;


    public static FragAQI newinstance() {
        FragAQI fragAQI = new FragAQI();
        return fragAQI;
    }
//    LineChart mChart;
//    ArrayList<Entry> values = new ArrayList<>();
//    int xvalues=1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_aqi, container, false);

        LineChart lineChart = view.findViewById(R.id.chart);


        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));


        LineDataSet lineDataSet = new LineDataSet(entries, "User");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF0015"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FF0015"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(10, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();


        this.InitializeView();
        this.InitializeListener();

//        this.InitializeView();
//        this.InitializeListener();
        return view;

    }


    public void InitializeView() {

        fromdate_txt2 = view.findViewById(R.id.fromdate_txt2);
        fromdate_txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                whichDate=true;
                DatePickerDialog dialog = new DatePickerDialog(getContext(), callbackMethod, 2020 ,1 , 1);

                dialog.show();
            }
        });
        todate_txt2 = view.findViewById(R.id.todate_txt2);
        todate_txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichDate=false;
                DatePickerDialog dialoge = new DatePickerDialog(getContext(), callbackMethod, 2020,1 , 1);

                dialoge.show();
            }
        });
    }

    Boolean whichDate=true;
    public void InitializeListener() {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(whichDate){
                    fromdate_txt2.setText(monthOfYear + " / " + dayOfMonth + " / " + year);
                }else{
                    todate_txt2.setText(monthOfYear + " / "+dayOfMonth+" / "+year);
                }
            }

        };

    }


}

