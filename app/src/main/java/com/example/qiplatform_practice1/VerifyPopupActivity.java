package com.example.qiplatform_practice1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VerifyPopupActivity extends AppCompatActivity {

    Button   ok_btn;
    TextView getid_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_verifypopup);


        getid_txt    = findViewById(R.id.getid_txt);
        ok_btn = findViewById(R.id.ok_btn);


        ok_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(VerifyPopupActivity.this , SigninActivity.class);
                                                startActivity(intent);
                                            }
                                        }
        );
//이밑으로는 받은 데이터값을 보여주는 과정
//        Intent intent = getIntent();
//
//        Bundle bundle = intent.getExtras();
//        String username = bundle.getString("username");
//        String password = bundle.getString("password");




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //바깥레이어 클릭시 안닫히게 하는 기능
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;

        }
        return true;
    }

    @Override
    public void onBackPressed() { //back 버튼 차단
        return;
    }


}
