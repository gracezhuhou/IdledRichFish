package com.sufe.idledrichfish.database;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.MyPublishActivity;
import com.sufe.idledrichfish.data.model.BmobProduct;
import com.sufe.idledrichfish.ui.publish.PublishFragment;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BmobProductDAL {
    /*
     * 上传商品数据
     */
    static public void saveProduct(BmobProduct bmobProduct) {
        bmobProduct.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                // 反馈给PublishmentFragment
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.i("BMOB", "Save Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.e("BMOB", "Save Product Fail", e);
                }
            }
        });
    }

    /**
     * 更新商品数据
     */
    static public void updateProduct(BmobProduct bmobProduct) {
        bmobProduct.update(bmobProduct.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // 反馈给 todo:某activity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    //PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.i("BMOB", "Update Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    //PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.e("BMOB", "Update Product Fail", e);
                }
            }
        });
    }

    /**
     * 删除一个对象(根据ObjectId)
     */
    static public void deleteProduct(String ProductId) {
        BmobProduct bmobProduct = new BmobProduct();
        bmobProduct.delete(ProductId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // 反馈给MyPublishedActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    MyPublishActivity.myPublishedHandler.sendMessage(msg);
                    Log.i("BMOB", "Delete Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    MyPublishActivity.myPublishedHandler.sendMessage(msg);
                    Log.e("BMOB", "Delete Product Fail", e);
                }
            }
        });
    }

}
