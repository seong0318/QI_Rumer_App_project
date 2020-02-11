package com.example.qiplatform_practice1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class ForgtpwActivity extends AppCompatActivity {

    TextView fgsignup_txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);

        fgsignup_txt = findViewById(R.id.forgotpwd_txt);

        fgsignup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetpwActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }
}
