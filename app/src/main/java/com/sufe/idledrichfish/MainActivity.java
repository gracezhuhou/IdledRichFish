package com.sufe.idledrichfish;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.ui.chat.ChatFragment;

import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener,
        MyFragment.OnFragmentInteractionListener {

    private FrameLayout mainFrame;
    private BottomNavigationView navView;
    private HomeFragment homeFragment;
    private ChatFragment chatFragment;
    private MyFragment myFragment;
    private Fragment[] fragments;
    private int lastFragment = 0;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private AlertDialog dialog;

    private EMMessageListener msgListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (lastFragment != 0) {
                            switchFragment(lastFragment, 0);
                            lastFragment = 0;
                        }
                        return true;
                    case R.id.navigation_message:
                        if (lastFragment != 1) {
                            switchFragment(lastFragment, 1);
                            lastFragment = 1;
                        }
                        return true;
                    case R.id.navigation_personal_info:
                        if (lastFragment != 2) {
                            switchFragment(lastFragment, 2);
                            lastFragment = 2;
                        }
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPermission();
//        initBmob();
        initView();

        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                // 收到消息
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                // 收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                // 收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                // 收到已送达回执
            }
            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                // 消息被撤回
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                // 消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    void initView() {
        homeFragment = new HomeFragment();
        chatFragment = new ChatFragment();
        myFragment = new MyFragment();
        fragments = new Fragment[]{homeFragment, chatFragment, myFragment};
        mainFrame = findViewById(R.id.mainFrame);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, homeFragment).show(homeFragment).commit();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    /**
     *切换fragment
     */
    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.mainFrame, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //Toast.makeText(this,"交流", Toast.LENGTH_LONG).show();
    }

    /**
     * 全屏并且隐藏状态栏
     */
    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    /**
     * 获取权限
     */
    private void setPermission() {

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED) {
                startRequestPermission();
            }
        }
    }

    /**
     * 开始提交请求权限
     */
    private void startRequestPermission(){
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    /**
     * 用户权限 申请 的回调方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    // 以前是!b
                    if (b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSetting();
                    } else{
                        finish();
                    }
                } else {
                    //获取权限成功提示，可以不要
                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    }

    /**
     * 提示用户去应用设置界面手动开启权限
     */
    private void showDialogTipUserGoToAppSetting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许应用使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                }).setCancelable(false).show();
    }

    /**
     * 跳转到当前应用的设置界面
     */
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //权限管理
        if (requestCode == 123) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSetting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initBmob() {
        // 初始化BmobSDK
        Bmob.initialize(this, "a0ed5f46dbb3be388267b3726f33ca5c");
        // 开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }

}
