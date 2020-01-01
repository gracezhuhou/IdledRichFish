package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.CreditActivity;
import com.sufe.idledrichfish.data.model.Credit;
import com.sufe.idledrichfish.data.model.Order;
import com.sufe.idledrichfish.data.model.Student;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreditDataSource {

    /**
     * 上传评分
     */
    void saveCredit(int score, String sellerId, String orderId) {
        Message msg = new Message();
        Bundle b = new Bundle();

        Order order = new Order();
        order.setObjectId(orderId);
        Student seller = new Student();
        seller.setObjectId(sellerId);
        Student buyer = Student.getCurrentUser(Student.class);
        final Credit credit = new Credit();
        credit.setScore(score);
        credit.setBuyer(buyer);
        credit.setSeller(seller);
        credit.setOrder(order);
        credit.setDate(new BmobDate(Tool.getNetTime())); // 获取网络时间

        credit.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                // 反馈给CreditActivity
                if(e == null) {
                    b.putInt("errorCode", 0);
                    b.putString("orderId", objectId);
                    Log.i("BMOB", "Save Credit Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB", "Save Credit Fail", e);
                }
                msg.setData(b);
                CreditActivity.creditHandler.sendMessage(msg);
            }
        });
    }
}
