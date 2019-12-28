package com.sufe.idledrichfish.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class Tool {
    /**
     * 获取网络标准时间
     */
    static Date getNetTime(){
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

    /**
     * file转List<Byte>
     * 压缩图片
     */
    static List<Byte> file2List(String filePath) {
        // 采样率压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10; // 压缩比率
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);

        Log.i("Bitmap", "Size: " + (bm.getByteCount() / 1024 / 1024)
                + ", Width:" + bm.getWidth() + ", Height:" + bm.getHeight());

        // 把图片转换字节流
        byte[] img = img(bm);
            List<Byte> image = new ArrayList<>();
            assert img != null;
            for (Byte b : img) {
                image.add(b);
            }
            return image;
    }

    /**
     * 把Bitmap图片转换为字节
     */
    private static byte[] img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
