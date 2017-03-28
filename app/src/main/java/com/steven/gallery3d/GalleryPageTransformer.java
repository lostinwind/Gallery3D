package com.steven.gallery3d;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by Steven on 2017/3/28.
 */

public class GalleryPageTransformer implements ViewPager.PageTransformer{
    public static final String TAG = GalleryPageTransformer.class.getSimpleName();
    public static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(View page, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1-Math.abs(position));
        float rotate = 20 * position;
        Log.e(TAG, "transformPage: position = " + position);
        Log.e(TAG, "transformPage: page = " + page);
        if (position < -1) {
        } else {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);
        }
    }
}
