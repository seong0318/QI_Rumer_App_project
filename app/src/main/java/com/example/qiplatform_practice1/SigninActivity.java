package com.example.qiplatform_practice1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class SigninActivity extends AppCompatActivity {

    EditText usn_edttxt, pwd_edttxt;
    Button signin_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        usn_edttxt = findViewById(R.id.usn_edttxt);
        pwd_edttxt = findViewById(R.id.pwd_edttxt);
        signin_btn = findViewById(R.id.signin_btn);


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgotpwd_txt: {
                Intent intent = new Intent(SigninActivity.this, ForgotpwActivity.class);
                startActivity(intent);
                return;
            }
            case R.id.signup_txt: {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
                return;
            }

            case R.id.signin_btn: {

            }
            String username = usn_edttxt.getText().toString();
            String password = pwd_edttxt.getText().toString();

            Intent intent = new Intent(SigninActivity.this, PopupActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivityForResult(intent, 1);
            break;
        }

    }

    public void onBackPressed() {


        //int i=90;
        finish();
    }
}