package com.example.qiplatform_practice1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Usermanagement extends AppCompatActivity {
ImageView changepw_btn1, idcancellation_btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanagement);




    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changepw_btn1: {
                Intent intent = new Intent(Usermanagement.this, ChangepwActivity.class);
                startActivity(intent);
                return; }

            case R.id.idcancellation_btn1: {
                Intent intent = new Intent(Usermanagement.this, Idcancellation.class);
                startActivity(intent);
                return; }

        }
    }
}
