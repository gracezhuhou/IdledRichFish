package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.data.model.Tag;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.ui.myHistory.MyHistoryActivity;
import com.sufe.idledrichfish.ui.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class StudentDataSource {

    /**
     * 查询用户
     */
    //TODO
   public void queryStudentForUser(String objectId) {
       BmobQuery<Student> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<Student>() {
            @Override
            public void done(Student student, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putString("id", student.getObjectId());
                    b.putString("name", student.getName());
                    b.putFloat("credit", student.getCredit());
                    if (student.getImage() != null) {
                        b.putByteArray("image", Bytes.toArray(student.getImage()));
                    } else {
                        b.putString("image", "");
                    }
                    msg.setData(b);
                    UserActivity.StudentHandler.sendMessage(msg);
                    Log.i("BMOB", "Query Student Success");
                } else {
                    Log.e("BMOB", "Query Student Fail", e);
                }
            }
        });
    }

    /**
     * 获取用户足迹
     */
    void queryHistoryProducts() {
        BmobQuery<Product> query = new BmobQuery<Product>();
        Student student = Student.getCurrentUser(Student.class);
        query.addWhereRelatedTo("history", new BmobPointer(student));
        query.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> object,BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if(e == null){
                    Log.i("BMOB", "Query My History Success");
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (Product product: object) {
                        Bundle b = new Bundle();
                        Student seller = product.getSeller();
                        b.putString("productId", product.getObjectId());
                        b.putString("name", product.getName());
                        b.putDouble("price", product.getPrice());
                        b.putString("sellerId", seller.getObjectId());
                        b.putString("sellerName", seller.getName());
                        b.putFloat("credit", seller.getCredit());
                        if (product.getImage1() != null) {
                            b.putByteArray("image1", Bytes.toArray(product.getImage1()));
                        }
                        if (product.getImage2() != null) {
                            b.putByteArray("image2", Bytes.toArray(product.getImage2()));
                        }
                        if (product.getImage3() != null) {
                            b.putByteArray("image3", Bytes.toArray(product.getImage3()));
                        }
                        if (product.getImage4() != null) {
                            b.putByteArray("image4", Bytes.toArray(product.getImage4()));
                        }
                        if (seller.getImage() != null) {
                            b.putByteArray("sellerImage", Bytes.toArray(seller.getImage()));
                        }
                        bundles.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                }else{
                    Log.e("BMOB", "Query My History Fail", e);
                    bundles.putInt("errorCode", e.getErrorCode());
                    bundles.putString("e", e.toString());
                }
                msg.setData(bundles);
                MyHistoryActivity.historyHandler.sendMessage(msg);
            }
        });
    }


    /**
     * 添加足迹
     */
    void addHistory(String productId) {
        Product history = new Product();
        history.setObjectId(productId);
        Student student = Student.getCurrentUser(Student.class);
        BmobRelation relation = new BmobRelation();
        // 将当前product添加到多对多关联中
        relation.add(history);
        // 多对多关联指向`student`的`history`字段
        student.setHistory(relation);
        student.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.i("bmob","Add History Success");
                }else{
                    Log.i("bmob","Add History Fail: " + e.getMessage());
                }
            }
        });
    }

    /**
     * 根据本学生的History来智能推荐
     */
    public void queryStudentHistory() {
        List<Tag> historyTags = new ArrayList<>();
        List<Integer> historyTabsNum = new ArrayList<>();

        // 查询学生浏览过的商品，因此查询的是Product表
        BmobQuery<Product> query = new BmobQuery<Product>();
        Student student = Student.getCurrentUser(Student.class);
        // products是Student表中的字段，用来存储所有浏览过的商品
        query.addWhereRelatedTo("products", new BmobPointer(student));
        query.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> object,BmobException e) {
                if(e == null){
                    for (Product product: object) {
                        Log.i("BMOB","Query Student History Products Success: " + object.size());

                        BmobQuery<Tag> query = new BmobQuery<Tag>();
                        query.addWhereRelatedTo("tabs", new BmobPointer(product));
                        query.findObjects(new FindListener<Tag>() {
                            @Override
                            public void done(List<Tag> object, BmobException e) {
                                if(e==null){
                                    Log.i("BMOB","Query Product Tabs Success: " + object.size());

                                    for (Tag tag : object) {
                                        boolean flag = true;
                                        int i = 0;
                                        int size = historyTags.size();
                                        for (i = 0; i < size; ++i) {
                                            if (tag.getObjectId().equals(historyTags.get(i).getObjectId())) {
                                                historyTabsNum.set(i, historyTabsNum.get(i) + 1);
                                                flag = false;
                                            }
                                        }
                                        if (flag) {
                                            historyTags.set(i, tag);
                                            historyTabsNum.set(i, 1);
                                        }
                                    }

                                    // 排序
                                    quickSort(historyTags, historyTabsNum, 0, historyTabsNum.size() - 1);

                                    // todo : 利用historyTabs来智能推荐
                                    String tabId = historyTags.get(0).getObjectId(); // 这是点击最多的tab的ID
                                    String tabId2 = historyTags.get(1).getObjectId(); // 这是点击第二多的tab的ID， 依次类推

                                    // 传回Activity
//                                    MessageView msg = new MessageView();
//                                    Bundle bundles = new Bundle();
//                                    int size = historyTabsNum.size();
//                                    for (int i = 0; i < size; ++i) {
//                                        Bundle b = new Bundle();
//                                        b.putString("tabId", historyTags.get(i).getObjectId());
//                                        b.putString("tabName", historyTags.get(i).getName());
//                                        b.putInt("tabNum", historyTabsNum.get(i));
//                                        bundles.putBundle(String.valueOf(i), b);
//                                    }
//                                    msg.setData(bundles);
//                                    HomeFragment.homeStudentHandler.sendMessage(msg);
//                                    Log.i("BMOB", "Query Student Success");

                                }else{
                                    Log.i("BMOB","Query Product Tabs Fail", e);
                                }
                            }
                        });
                    }
                }else{
                    Log.i("BMOB","Query Student History Products Fail", e);
                }
            }
        });
    }


    // 获取本学生tabs计数的排序
    private static void quickSort(List<Tag> arr, List<Integer> arrNum, int low, int high){
        int i, j, tempNum, tNum;
        Tag temp, t;
        if(low > high){
            return;
        }
        i = low;
        j = high;
        // temp就是基准位
        temp = arr.get(low);
        tempNum = (int)arrNum.get(low);

        while (i < j) {
            // 先看右边，依次往左递减
            while (tempNum <= arrNum.get(j) && i < j) {
                j--;
            }
            // 再看左边，依次往右递增
            while (tempNum >= arrNum.get(i) && i < j) {
                i++;
            }
            // 如果满足条件则交换
            if (i < j) {
                tNum = arrNum.get(j);
                arrNum.set(j, arrNum.get(i));
                arrNum.set(i, tNum);

                t = arr.get(j);
                arr.set(j, arr.get(i));
                arr.set(i, t);
            }

        }
        // 最后将基准为与i和j相等位置的数字交换
        arrNum.set(low, arrNum.get(i));
        arrNum.set(i, tempNum);
        arr.set(low, arr.get(i));
        arr.set(i, temp);
        // 递归调用左半数组
        quickSort(arr, arrNum, low, j - 1);
        // 递归调用右半数组
        quickSort(arr, arrNum, j + 1, high);
    }

}
