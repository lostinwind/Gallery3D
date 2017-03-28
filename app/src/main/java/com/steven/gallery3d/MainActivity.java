package com.steven.gallery3d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private GalleryAdapter adapter;
    private List<ImageView> imageViews;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDatas();
        initViewPager();
    }

    private void initViewPager() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                screenWidth/2, DensityUtil.dip2px(this, 360)
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        viewPager.setLayoutParams(layoutParams);
        viewPager.setPageTransformer(true, new GalleryPageTransformer());
        adapter = new GalleryAdapter(imageViews);
        viewPager.setAdapter(adapter);
        //默认后台加载一个，要改成后台加载两个
        viewPager.setOffscreenPageLimit(2);
    }

    private void initDatas() {
        screenWidth = DensityUtil.getScreenWidth(this);
        imageViews = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(createBitmap(R.drawable.saber1+i));
            imageViews.add(imageView);
        }
    }

    private Bitmap createBitmap(int resId) {
        //大小就按照实际ImageView的大小来
        Bitmap originBitmap = BitmapUtils.decodeBitmapFromResource(getResources(), resId,
                DensityUtil.dip2px(this, 160), DensityUtil.dip2px(this, 220));
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        Bitmap shadeBitmap = Bitmap.createBitmap(originBitmap, 0, originBitmap.getHeight()*2/3,
                originBitmap.getWidth(), originBitmap.getHeight()/3, matrix, false);

        Paint paint = new Paint();
        //创建一个空的位图
        //多余的8是用来制造点缝隙
        Bitmap targetBitmap = Bitmap.createBitmap(originBitmap.getWidth(),
                originBitmap.getHeight()+shadeBitmap.getHeight()+8, Bitmap.Config.ARGB_8888);
        //根据位图大小创建画布
        Canvas canvas = new Canvas(targetBitmap);
        canvas.drawBitmap(originBitmap, 0, 0, paint);
        canvas.drawBitmap(shadeBitmap, 0, originBitmap.getHeight() + 8, paint);
        //处理下面的阴影效果
        LinearGradient linearGradient = new LinearGradient(0, originBitmap.getHeight()+8,
                0, targetBitmap.getHeight(), 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, originBitmap.getHeight()+5, originBitmap.getWidth(), targetBitmap.getHeight(),
                paint);
        return targetBitmap;
    }
}
