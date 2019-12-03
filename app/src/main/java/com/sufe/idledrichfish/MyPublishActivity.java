package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MyPublishActivity extends AppCompatActivity {

    static public Handler myPublishedHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_published);
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取Bmob返回的信息
        myPublishedHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Error Code " + errorCode);

                if (errorCode == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MyPublishActivity.this)
                            .setTitle("商品删除成功")
                            .setIcon(R.drawable.ic_audit)
                            .create();
                    alertDialog.show();
                }
                else {
                    String fail = "失败原因:" + errorCode + msg.getData().getString("e");

                    AlertDialog alertDialog = new AlertDialog.Builder(MyPublishActivity.this)
                            .setTitle("商品删除失败")
                            .setMessage(fail)
                            .setIcon(R.drawable.ic_fail)
                            .create();
//                    if (e.getErrorCode() == 202)
//                        alertDialog.setMessage("");
//                    else if (e.getErrorCode() == 301)
//                        alertDialog.setMessage("");
                    alertDialog.show();
                }
            }
        };
    }
}
