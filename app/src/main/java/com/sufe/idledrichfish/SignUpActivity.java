package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.sufe.idledrichfish.data.LoginDataSource;
import com.sufe.idledrichfish.ui.login.LoginActivity;

import cn.bmob.v3.Bmob;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private  Button button_signUp;
    private EditText text_studentNumber;
    private EditText text_name;
    private EditText text_password;
    private RadioButton btn_male;
    private RadioButton btm_female;
    private ImageView image_student;

    private String imagePath = "";
    static public Handler signUpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.toolbar_sign_up);
        button_signUp = findViewById(R.id.btn_signUp);
        text_studentNumber = findViewById(R.id.text_stuNumber);
        text_name = findViewById(R.id.text_name);
        text_password = findViewById(R.id.text_password);
        btn_male = findViewById(R.id.radioBtn_male);
        btm_female = findViewById(R.id.radioBtn_female);
        image_student = findViewById(R.id.image_student);

        Bmob.initialize(this, "a0ed5f46dbb3be388267b3726f33ca5c");

        setToolbar();
        setHandler();

        /*
         * 点击注册按钮
         */
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo
                LoginDataSource loginDataSource = new LoginDataSource();
                loginDataSource.signUp(text_studentNumber.getText().toString(), text_name.getText().toString(),
                        text_password.getText().toString(), btn_male.isChecked(), imagePath);
            }
        });

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
        /*
         * 返回键监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 返回登录页面
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取Bmob返回的注册ErrorCode
        signUpHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Error Code " + errorCode);

                if (errorCode == 0) {

                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("注册成功")
                            .setMessage("请耐心等待账号审核，谢谢")
                            .setIcon(R.drawable.ic_audit)
//                            .setPositiveButton("登录", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    // 跳转至首页
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            })
                            .create();
                    alertDialog.show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    String fail = "失败原因:" + errorCode;

                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this)
                            .setTitle("注册失败")
                            .setMessage(fail)
                            .setIcon(R.drawable.ic_fail)
                            .create();
//                    if (e.getErrorCode() == 202)
//                        alertDialog.setMessage("这个邮箱已经注册过咯");
//                    else if (e.getErrorCode() == 301)
//                        alertDialog.setMessage("此邮箱地址无效哦");
//                    else
                    alertDialog.show();
                }
            }
        };
    }
}
