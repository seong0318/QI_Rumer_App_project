package com.example.qiplatform_practice1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

public class LoadingActivity extends AppCompatActivity {
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joggingbackground);
        activity = this;
        LinearLayout bk = findViewById(R.id.joggingbackground);

        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SigninActivity.class);

                startActivity(intent);
            }
        });

        Handler handler = new Handler();
        Runnable run = new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(activity, SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        };
        handler.postDelayed(run,2000);
    }


}
