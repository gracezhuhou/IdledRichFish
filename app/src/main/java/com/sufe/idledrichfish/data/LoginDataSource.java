package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.SignUpActivity;
import com.sufe.idledrichfish.data.model.BmobStudent;
import com.sufe.idledrichfish.database.StudentBLL;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.ui.login.LoginActivity;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    /*
     * 用户注册
     */
    public void signUp(String studentNumber, String name, String password, boolean gender, String imagePath) {
        BmobStudent student = new BmobStudent();
        student.setUsername(studentNumber);
        student.setName(name);
        student.setPassword(password);
        if (gender)
            student.setGender("male");
        else
            student.setGender("female");
        // todo: 图片
        //获取图片
//        String picPath = Environment.getExternalStorageDirectory().getPath() + "/cardPic.jpg";
//        Bitmap pic= BitmapFactory.decodeFile(picPath);
//        if (pic != null) {
//            //把图片转换字节流
//            byte[] images = img();
//            card.setPicture(images);    //保存图片

        student.signUp(new SaveListener<BmobStudent>() {
            @Override
            public void done(BmobStudent student, BmobException e) {
                // 反馈给LoginActivity
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
                    b.putString("e", e.toString()); // 错误信息
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
     * 查询用户是否已注册
     */
    public boolean isLoggedIn() {
        return BmobStudent.isLogin();
    }

    /*
     * 用户登出
     */
    public void logOut() {
        BmobStudent.logOut();
    }
}
