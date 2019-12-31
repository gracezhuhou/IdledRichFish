package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.SignUpActivity;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.login.LoginActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    /**
     * 用户注册
     */
    void signUp(String studentNumber, String name, String password, boolean gender, String imagePath) {
        Student student = new Student();
        student.setUsername(studentNumber);
        student.setName(name);
        student.setPassword(password);
        if (gender)
            student.setGender("male");
        else
            student.setGender("female");
        if (imagePath != null) {
            student.setImage(Tool.file2List(imagePath));
        }
        student.setCredit(10);
        student.setLastLoginDate(new BmobDate(Tool.getNetTime()));

        student.signUp(new SaveListener<Student>() {
            @Override
            public void done(Student student, BmobException e) {
                // 反馈给LoginActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    SignUpActivity.signUpHandler.sendMessage(msg);
                    Log.i("BMOB", "Sign Up Success");
                    // 注册通信账户
                    IMHelper.getInstance().createClient(student.getObjectId(), password);
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

    /**
     * 学号密码登录
     */
    void login(String stuNumber, String password) {
        final Student user = new Student();
        //此处替换为你的用户名
        user.setUsername(stuNumber);
        //此处替换为你的密码
        user.setPassword(password);
        user.login(new SaveListener<Student>() {
            @Override
            public void done(Student student, BmobException e) {
                // 传e给LoginActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    LoginActivity.loginHandler.sendMessage(msg);
                    Log.i("BMOB", "Login Success");
                    // 登录通信账户
                    IMHelper.getInstance().login(student.getObjectId(), password);
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    msg.setData(b);
                    LoginActivity.loginHandler.sendMessage(msg);
                    Log.e("BMOB", "Login Fail");
                }
            }
        });
    }

    /**
     * 查询用户是否已注册
     */
    boolean isLoggedIn() {
        return Student.isLogin();
    }

    /**
     * 用户登出
     */
    void logOut() {
        Student.logOut();
        // 登出通信账户
        IMHelper.getInstance().logOut();
    }


}
