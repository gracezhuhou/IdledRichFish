package com.sufe.idledrichfish.database;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.SignUpActivity;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.ui.login.LoginActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class BmobDBHelper {
    /*
     * 用户注册
     */
    public void signUp(BmobStudent student) {
//        BmobStudent bmobStudent = new BmobStudent();
//        bmobStudent.setName(name);
//        bmobStudent.setName(student_id);
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
                // 传e给LoginActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    SignUpActivity.signUpHandler.sendMessage(msg);
                    Log.i("BMOB", "Sign Up Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    msg.setData(b);
                    SignUpActivity.signUpHandler.sendMessage(msg);
                    Log.e("BMOB", "Sign Up Fail", e);
                }

            }
        });
    }

    /*
     * 学号密码登录
     */
    public void login(String stuNumber, String password) {
        final BmobStudent user = new BmobStudent();
        //此处替换为你的用户名
        user.setUsername(stuNumber);
        //此处替换为你的密码
        user.setPassword(password);
        user.login(new SaveListener<BmobStudent>() {
            @Override
            public void done(BmobStudent bmobUser, BmobException e) {
                // 传e给LoginActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    LoginActivity.loginHandler.sendMessage(msg);
                    Log.i("BMOB", "Login Success");
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    msg.setData(b);
                    LoginActivity.loginHandler.sendMessage(msg);
                    Log.e("BMOB", "Login Fail");
                }
            }
        });
    }

    /*
     * 用户登出
     */
    public void logOut() {
        BmobStudent.logOut();
    }

    /*
     * 查询用户By Object Id
     */
    public void queryStudentById(String id) {
        BmobQuery<BmobStudent> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<BmobStudent>() {
            @Override
            public void done(BmobStudent bmobStudent, BmobException e) {
                if (e == null) {
//                    HomeFragment.homeHandler = new Handler();
//                    Message msg = HomeFragment.homeHandler.obtainMessage();
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
                        HomeFragment.homeHandler.sendMessage(msg);

                        Log.i("BMOB", "Query Student Success");
                    }
                } else {
                    Log.e("BMOB", "Query Student Fail: " + e.getMessage());
                }
            }
        });
    }
}
