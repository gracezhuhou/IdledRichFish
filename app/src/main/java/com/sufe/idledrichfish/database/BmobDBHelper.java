package com.sufe.idledrichfish.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.HomeFragment;
import com.sufe.idledrichfish.MainActivity;
import com.sufe.idledrichfish.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static org.litepal.LitePalApplication.getContext;

public class BmobDBHelper {
    /*
    * 用户注册
    */
    public void signUp(BmobStudent student) {
//        BmobStudent bmobStudent = new BmobStudent();
//        bmobStudent.setName(name);
//        bmobStudent.setStudentNumber(student_id);
//        bmobStudent.setPassword(password);
//        bmobStudent.setGender(gender);
//        bmobStudent.setAdminId(admin_id);
// todo: image
//获取图片
//        String picPath = Environment.getExternalStorageDirectory().getPath() + "/cardPic.jpg";
//        Bitmap pic= BitmapFactory.decodeFile(picPath);
//        if (pic != null) {
//            //把图片转换字节流
//            byte[] images = img();
//            card.setPicture(images);    //保存图片
//        }
        student.signUp(new SaveListener<BmobStudent>() {
            @Override
            public void done(BmobStudent student, BmobException e) {
                if(e == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("注册成功")
                            .setMessage("请耐心等待账号审核，谢谢^3^")
                            .setIcon(R.drawable.ic_audit)
                            .create();
                    alertDialog.show();

                    Log.i("BMOB", "Sign Up Success");
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("注册失败")
                            .setIcon(R.drawable.ic_fail)
                            .create();
                    if (e.getErrorCode() == 202)
                        alertDialog.setMessage("这个邮箱已经注册过咯");
                    else if (e.getErrorCode() == 301)
                        alertDialog.setMessage("此邮箱地址无效哦");
                    else
                        alertDialog.setMessage("失败原因:" + e);
                    alertDialog.show();

                    Log.e("BMOB", "Sign Up Fail", e);
                }

            }
        });
    }

    /*
     * 查询用户
     */
    public void queryStudentById(String id) {
        BmobQuery<BmobStudent> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<BmobStudent>() {
            @Override
            public void done(BmobStudent bmobStudent, BmobException e) {
                if (e == null) {
//                    HomeFragment.myHandler = new Handler();
//                    Message msg = HomeFragment.myHandler.obtainMessage();
                    // 保存至本地数据库
                    StudentBLL studentBLL = new StudentBLL();
                    if (studentBLL.storeStudent(bmobStudent)) {
                        // 解决异步的问题
                        Message msg = new Message();
                        //利用bundle对象来传值 (Handler)
                        Bundle b = new Bundle();
                        b.putString("studentId", bmobStudent.getObjectId());
                        msg.setData(b);
                        //msg.sendToTarget();
                        HomeFragment.myHandler.sendMessage(msg);

                        Log.i("BMOB", "Query Student Success");
                    }
                } else {
                    Log.e("BMOB", "Query Student Fail: " + e.getMessage());
                }
            }
        });
    }
}
