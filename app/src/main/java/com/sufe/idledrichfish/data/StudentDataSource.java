package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.home.HomeFragment;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class StudentDataSource {

    /**
     * 查询用户
     */
    public void queryStudentForHome(String ObjectId, int position) {
        BmobQuery<Student> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(ObjectId, new QueryListener<Student>() {
            @Override
            public void done(Student student, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putInt("position", position);
                if (e == null) {
                    b.putString("id", student.getObjectId());
                    b.putString("name", student.getName());
                    b.putFloat("credit", student.getCredit());
                    if (student.getImage() != null) {
                        b.putString("image", student.getImage().getUrl());
                    } else {
                        b.putString("image", "");
                    }
                    msg.setData(b);
                    HomeFragment.homeStudentHandler.sendMessage(msg);
                    Log.i("BMOB", "Query Student Success");
                } else {
                    Log.e("BMOB", "Query Student Fail", e);
                }

            }
        });
    }
}
