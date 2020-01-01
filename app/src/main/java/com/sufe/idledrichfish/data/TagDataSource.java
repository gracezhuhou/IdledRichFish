package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.data.model.Tag;
import com.sufe.idledrichfish.ui.publish.PublishActivity;
import com.sufe.idledrichfish.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

public class TagDataSource {

    /**
     * 批量添加标签
     */
    void saveTags(List<String> tagNames, String productId) {
        Product product = new Product();
        product.setObjectId(productId);
        List<BmobObject> tags = new ArrayList<>();
        for (String name: tagNames) {
            Tag tag = new Tag();
            tag.setName(name);
            tag.setProduct(product);
            tags.add(tag);
        }
        new BmobBatch().insertBatch(tags).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> results, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);

                    for (int i = 0; i < results.size(); i++) {
                        BatchResult result = results.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.i("BMOB","Save Tag Success: " + i + ", " +
                                    result.getCreatedAt() + ", " + result.getObjectId() + ", " + result.getUpdatedAt());
                        } else {
                            b.putInt("errorCode", ex.getErrorCode());
                            b.putString("e", ex.toString());
                            Log.e("BMOB","Save Tag Fail: " + i, e);
                        }
                    }
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB","Save Tag Fail", e);
                }
                msg.setData(b);
                PublishActivity.publishHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 根据标签查商品
     */
    void queryProductsByTag(String tag) {
        BmobQuery<Tag> query = new BmobQuery<Tag>();
        query.addWhereEqualTo("name", tag);
        query.include("product.seller");
        query.findObjects(new FindListener<Tag>() {
            @Override
            public void done(List<Tag> objects, BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if (e == null) {
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (Tag tag : objects) {
                        Bundle b = new Bundle();
                        Product product = tag.getProduct();
                        b.putString("productId", product.getObjectId());
                        b.putString("name", product.getName());
                        b.putDouble("price", product.getPrice());
                        b.putBoolean("isNew", product.isNew());
                        b.putBoolean("canBargain", product.isCanBargain());
                        b.putString("sellerId", product.getSeller().getObjectId());
                        if (product.getImage1() != null) {
                            b.putByteArray("productImage", Bytes.toArray(product.getImage1()));
                        }
                        Student seller = product.getSeller();
                        b.putString("sellerId", seller.getObjectId());
                        b.putString("sellerName", seller.getName());
                        b.putFloat("credit", seller.getCredit());
                        if (seller.getImage() != null) {
                            b.putByteArray("studentImage", Bytes.toArray(seller.getImage()));
                        }
                        bundles.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                    Log.i("BMOB", "Query Tags Success");
                } else {
                    bundles.putInt("errorCode", e.getErrorCode());
                    bundles.putString("e", e.toString());
                    Log.e("BMOB", "Query Tags Fail", e);
                }
                msg.setData(bundles);
                SearchActivity.searchHandler.sendMessage(msg);
            }
        });
    }
}
