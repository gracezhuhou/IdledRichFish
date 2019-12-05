package com.sufe.idledrichfish.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * 获取网络标准时间
 */
public class Tool {

    public static Date getNetTime(){
        String webUrl = "http://www.ntsc.ac.cn"; // 中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            return new Date(correctTime);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static File drawableToFile(int drawableId){
        InputStream is = getApplicationContext().getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        String defaultPath = getApplicationContext().getFilesDir().getAbsolutePath() + "/defaultImage";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            return file;
        }
        String defaultImgPath = defaultPath + "/image.jpg";
        file = new File(defaultImgPath);
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut);
            is.close();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
