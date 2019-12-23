package com.sufe.idledrichfish.ui.publish;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;

/**
 *  用Glide加载NineGridView
 */
class GlideImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
