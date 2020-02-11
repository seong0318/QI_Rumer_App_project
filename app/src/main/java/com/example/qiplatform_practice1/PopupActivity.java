package com.example.qiplatform_practice1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.qiplatform_practice1.ui.home.HomeFragment;

public class PopupActivity extends AppCompatActivity {

    Button   thankyou_btn;
    TextView getid_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signinpopup);


        getid_txt    = findViewById(R.id.getid_txt);
        thankyou_btn = findViewById(R.id.thankyou_btn);


        thankyou_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(PopupActivity.this , MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
        );
//이밑으로는 받은 데이터값을 보여주는 과정
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String username = bundle.getString("username");
        String password = bundle.getString("password");

        getid_txt.setText("Welcome, "+ username + " !");




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
