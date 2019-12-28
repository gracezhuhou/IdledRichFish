package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.sufe.idledrichfish.data.LoginDataSource;
import com.sufe.idledrichfish.data.LoginRepository;
import com.sufe.idledrichfish.ui.login.LoginActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import cn.bmob.v3.Bmob;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private Button button_sign_up;
    private EditText text_stu_number;
    private EditText text_name;
    private EditText text_password;
    private RadioButton btn_male;
    private RadioButton btm_female;
    private CircleImageView image_student;

    private String imagePath;
    static public Handler signUpHandler;
    private final int REQUEST_CODE_CHOOSE_PHOTO_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        button_sign_up = findViewById(R.id.button_sign_up);
        text_stu_number = findViewById(R.id.text_stuNumber);
        text_name = findViewById(R.id.text_name);
        text_password = findViewById(R.id.text_password);
        btn_male = findViewById(R.id.radioBtn_male);
        btm_female = findViewById(R.id.radioBtn_female);
        image_student = findViewById(R.id.image_student);

        Bmob.initialize(this, "a0ed5f46dbb3be388267b3726f33ca5c");

        setToolbar();
        setHandler();

        image_student.setOnClickListener(view -> choosePhoto());
        button_sign_up.setOnClickListener(view -> signUp());

    }

    /**
     * 获取选择的照片
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO_ALBUM && resultCode == RESULT_OK) {
            // 图片路径 根据requestCode
            List<String> pathList = Matisse.obtainPathResult(data);
            if (pathList.size() == 1) {
                imagePath = pathList.get(0);
                Glide.with(this).load(imagePath).into(image_student);
                Log.i("ImageByte", imagePath);
            }
        }
        Log.i("ImageByte", "Result Fail");
    }

    /**
     * 设置Toolbar
     */
    private void setToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
        }
        // 返回键监听
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

    /**
     * 返回Bmob信息，注册成功与否
     */
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

    /**
     * 选择照片
     * Matisse
     */
    private void choosePhoto() {
        Matisse
                .from(this)
                // 选择图片  （ofAll选择视频和图片、ofVideo选择视频）
                .choose(MimeType.ofImage())
                // 是否只显示选择的类型的缩略图（就不会把所有图片视频都放在一起，而是需要什么展示什么）
                .showSingleMediaType(true)
                // 这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
                // 有序选择图片 123456...
                .countable(true)
                // 最大选择数量为9
                .maxSelectable(1)
                // 选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                // 界面中缩略图的质量
                .thumbnailScale(0.8f)
                // 黑色主题 （蓝色主题 Matisse_Zhihu）
                .theme(R.style.Matisse_Dracula)
                // Glide加载方式
                .imageEngine(new GlideImageEngine())
                //请求码
                .forResult(REQUEST_CODE_CHOOSE_PHOTO_ALBUM);
    }

    /**
     * 点击注册按钮,注册账号
     */
    private void signUp() {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("确定信息无误？")
                .withMessage("注册等待2-3工作日账号审核")
                .withDialogColor(getResources().getColor(R.color.orange))
                .withIcon(getResources().getDrawable(R.drawable.ic_sign_up))
                .withDuration(700) // def
                .withEffect(Effectstype.SlideBottom)
                .withButton1Text("确定")
                .withButton2Text("取消")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginRepository.getInstance(new LoginDataSource())
                                .signUp(text_stu_number.getText().toString(), text_name.getText().toString(),
                                        text_password.getText().toString(), btn_male.isChecked(), imagePath);
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }
}
