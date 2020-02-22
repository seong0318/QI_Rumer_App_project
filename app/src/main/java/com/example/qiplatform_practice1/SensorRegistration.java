package com.example.qiplatform_practice1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Bundle;



import java.util.Arrays;
import java.util.List;

public class SensorRegistration extends AppCompatActivity {

    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_reg);

        init();

        getData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recycler1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
        List<String> listContent = Arrays.asList(
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다.",
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다.",
                "이 동물은 펭귄입니다.",
                "이 꽃은 튤립입니다."
        );
        List<Integer> listResId = Arrays.asList();
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
          adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

}