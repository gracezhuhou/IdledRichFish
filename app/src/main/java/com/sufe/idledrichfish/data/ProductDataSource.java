package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.ui.chat.ChatActivity;
import com.sufe.idledrichfish.ui.myPublish.MyPublishActivity;
import com.sufe.idledrichfish.ProductInfoActivity;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.ui.myPublish.MyPublishRecyclerViewAdapter;
import com.sufe.idledrichfish.ui.publish.PublishActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ProductDataSource {

    /**
     * 上传商品数据
     */
    void saveProduct(String productName, String description, boolean isNew, boolean canBargain,
                     double price, double oldPrice, String category, List<String> imagePath,
                     List<String> tags) {
        Message msg = new Message();
        Bundle b = new Bundle();
        // 保存Product
        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);
        product.setNew(isNew);
        product.setCanBargain(canBargain);
        product.setPrice(price);
        product.setOldPrice(oldPrice);
        product.setCategory(category);
        product.setSeller(Student.getCurrentUser(Student.class)); // 获取当前用户
        product.setPublishDate(new BmobDate(Tool.getNetTime())); // 获取网络时间

        if (imagePath != null) {
            List<List<Byte>> images = new ArrayList<>();

            for (String path: imagePath) {
                images.add(Tool.file2List(path));
            }
            // image的byte保存到表中
            switch(imagePath.size()) {
                case 9: product.setImage9(images.get(8));
                case 8: product.setImage8(images.get(7));
                case 7: product.setImage7(images.get(6));
                case 6: product.setImage6(images.get(5));
                case 5: product.setImage5(images.get(4));
                case 4: product.setImage4(images.get(3));
                case 3: product.setImage3(images.get(2));
                case 2: product.setImage2(images.get(1));
                case 1: product.setImage1(images.get(0));
            }
        }

        product.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                // 反馈给PublishFragment
                if(e == null) {
                    TagRepository.getInstance(new TagDataSource()).saveTags(tags, objectId);
                    Log.i("BMOB", "Save Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    PublishActivity.publishHandler.sendMessage(msg);
                    Log.e("BMOB", "Save Product Fail", e);
                }
            }
        });
    }

    /**
     * 更新商品数据
     */
    void updateProduct(Product product) {
        product.update(product.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // 反馈给 todo:某activity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    //PublishFragment.publishHandler.sendMessage(msg);
                    Log.i("BMOB", "Update Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    //PublishFragment.publishHandler.sendMessage(msg);
                    Log.e("BMOB", "Update Product Fail", e);
                }
            }
        });
    }

    /**
     * 删除一个对象(根据ObjectId)
     */
    void deleteProduct(String objectId) {
        Product product = new Product();
        product.delete(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // 反馈给MyPublishedActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    MyPublishRecyclerViewAdapter.deleteHandler.sendMessage(msg);
                    Log.i("BMOB", "Delete Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    MyPublishRecyclerViewAdapter.deleteHandler.sendMessage(msg);
                    Log.e("BMOB", "Delete Product Fail", e);
                }
            }
        });
    }

    /**
     * 根据Id查商品
     * 获取全部属性值
     */
    void queryProductAllInfo(String objectId) {
        BmobQuery<Product> bmobQuery = new BmobQuery<>();
        bmobQuery.include("seller");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY); // 缓存+网络
        bmobQuery.getObject(objectId, new QueryListener<Product>() {
            @Override
            public void done(Product product, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);
                    b.putString("name", product.getName());
                    b.putString("description", product.getDescription());
                    b.putDouble("price", product.getPrice());
                    b.putDouble("oldPrice", product.getOldPrice());
                    b.putBoolean("isNew", product.isNew());
                    b.putBoolean("canBargain", product.isCanBargain());
                    b.putString("publishDate", product.getPublishDate().getDate()); // String 存放BmobData
                    b.putString("sellerId", product.getSeller().getObjectId());
                    b.putString("sellerName", product.getSeller().getName());
                    b.putFloat("credit", product.getSeller().getCredit());
                    b.putString("gender", product.getSeller().getGender());
                    b.putString("lastLoginDate", product.getSeller().getLastLoginDate().getDate());
                    if (product.getSeller().getImage() != null) {
                        b.putByteArray("studentImage", Bytes.toArray(product.getSeller().getImage()));
                    }
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
                    if (product.getImage5() != null) {
                        b.putByteArray("image5", Bytes.toArray(product.getImage5()));
                    }
                    if (product.getImage6() != null) {
                        b.putByteArray("image6", Bytes.toArray(product.getImage6()));
                    }
                    if (product.getImage7() != null) {
                        b.putByteArray("image7", Bytes.toArray(product.getImage7()));
                    }
                    if (product.getImage8() != null) {
                        b.putByteArray("image8", Bytes.toArray(product.getImage8()));
                    }
                    if (product.getImage9() != null) {
                        b.putByteArray("image9", Bytes.toArray(product.getImage9()));
                    }
                    Log.i("BMOB", "Query Product By Id Success");
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB", "Query Product By Id Fail", e);
                }
                msg.setData(b);
                ProductInfoActivity.productInfoHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 查询商品ForHome
     */
    void queryProductsForHome(boolean policy) {
        BmobQuery<Product> bmobQuery = new BmobQuery<>();
        bmobQuery.include("seller");
        if (policy) {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);   // 网络
        }else {
            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 先从缓存获取数据，如果没有，再从网络获取
        }
        bmobQuery.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> products, BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if (e == null) {
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (Product product : products) {
                        Bundle b = new Bundle();
                        b.putString("objectId", product.getObjectId());
                        b.putString("name", product.getName());
                        b.putDouble("price", product.getPrice());
                        b.putBoolean("isNew", product.isNew());
                        b.putBoolean("canBargain", product.isCanBargain());
                        b.putString("sellerId", product.getSeller().getObjectId());
                        if (product.getImage1() != null) {
                            b.putByteArray("productImage", Bytes.toArray(product.getImage1()));
                        }
                        b.putString("sellerId", product.getSeller().getObjectId());
                        b.putString("sellerName", product.getSeller().getName());
                        b.putFloat("credit", product.getSeller().getCredit());
                        if (product.getSeller().getImage() != null) {
                            b.putByteArray("studentImage", Bytes.toArray(product.getSeller().getImage()));
                        }
                        bundles.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                        msg.setData(bundles);
                    HomeFragment.homeProductsHandler.sendMessage(msg);

                    Log.i("BMOB", "Query Products Success");
                } else {
                    bundles.putInt("errorCode", e.getErrorCode());
                    msg.setData(bundles);
                    HomeFragment.homeProductsHandler.sendMessage(msg);
                    Log.e("BMOB", "Query Products Fail", e);
                }
            }
        });
    }

    /**
     * 根据Id查商品ForChat
     */
    void queryProductForChat(String objectId) {
        BmobQuery<Product> bmobQuery = new BmobQuery<>();
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY); // 先从缓存获取数据
        bmobQuery.getObject(objectId, new QueryListener<Product>() {
            @Override
            public void done(Product product, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);
                    b.putString("name", product.getName());
                    b.putDouble("price", product.getPrice());
                    if (product.getImage1() != null) {
                        b.putByteArray("image", Bytes.toArray(product.getImage1()));
                    }
                    Log.i("BMOB", "Query Product By Id Success");
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB", "Query Product By Id Fail", e);
                }
                msg.setData(b);
                ChatActivity.productHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 查询我发布的商品
     */
    void queryMyPublishProducts() {
        BmobQuery<Product> query = new BmobQuery<Product>();
        Student student = Student.getCurrentUser(Student.class);
        query.addWhereEqualTo("seller",new BmobPointer(student));
        query.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> objects,BmobException e) {
                Message msg = new Message();
                Bundle bs = new Bundle();
                if (e == null) {
                    bs.putInt("errorCode", 0);
                    int i = 0;
                    for (Product product : objects) {
                        Bundle b = new Bundle();
                        b.putString("objectId", product.getObjectId());
                        b.putString("name", product.getName());
                        b.putDouble("price", product.getPrice());
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
                        bs.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                    msg.setData(bs);
                    MyPublishActivity.myPublishHandler.sendMessage(msg);
                    Log.i("BMOB", "Query Products Success");
                } else {
                    bs.putInt("errorCode", e.getErrorCode());
                    bs.putString("e", e.toString());
                    msg.setData(bs);
                    MyPublishActivity.myPublishHandler.sendMessage(msg);
                    Log.e("BMOB", "Query Products Fail", e);
                }
            }
        });

    }
}
