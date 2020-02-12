package com.example.qiplatform_practice1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    TextView Signinhere;
    EditText usn_edttxt;
    Button signup_btn;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        int a = 0;
        activity=this;
        Signinhere = findViewById(R.id.gosignin_txt);
        usn_edttxt = findViewById(R.id.usn_edttxt);
        signup_btn = findViewById(R.id.signup_btn);

        Signinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SigninActivity.class);

                startActivityForResult(intent, 1);
                return;

            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(SignupActivity.this, VerifyPopupActivity.class);
            startActivityForResult(intent,1);
            return;

        }
        });
    }
}
