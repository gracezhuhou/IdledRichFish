package com.sufe.idledrichfish.data;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class IMHelper {

    private static volatile IMHelper instance;

    public static IMHelper getInstance() {
        if (instance == null) {
            instance = new IMHelper();
        }
        return instance;
    }

    // 注册通信账户
    void createClient(String username, String password){
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username, password);
                    Log.i("IMHelper", "Create Client Success");
//                    login(username, password);
                } catch (final HyphenateException e) {
                    Log.i("IMHelper", "Create Client Fail, " + e.getDescription());
//                    int errorCode=e.getErrorCode();
//                    if(errorCode==EMError.NETWORK_ERROR){
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
//                    }else if(errorCode == EMError.USER_ALREADY_EXIST){
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
//                    }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
//                    }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        }).start();
    }

    // 登录通信账户
    void login(String username, String password) {
        EMClient.getInstance().login(username,password,new EMCallBack() { // 回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.i("IMHelper", "Login Success");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                Log.i("IMHelper", "Login Fail, " + message);
            }
        });
    }

    // 登出通信账户
    void logOut() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("IMHelper", "Log Out Success");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                Log.i("IMHelper", "Log Out Fail, " + message);
            }
        });
    }
}
