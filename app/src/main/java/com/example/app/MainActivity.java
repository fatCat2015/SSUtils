package com.example.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Consumer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cat.sutils.ColorTransition;
import com.cat.sutils.PaletteHelper;
import com.cat.sutils.StatusBarUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private android.support.design.widget.TabLayout tabLayout;
    private android.support.v4.view.ViewPager viewPager;



    private int[] images={R.mipmap.icon_1,R.mipmap.icon_2,R.mipmap.icon_3,R.mipmap.icon_4,R.mipmap.icon_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);


        viewPager.setAdapter(new Adapter());
        tabLayout.setupWithViewPager(viewPager);
        updateViewStyle(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                updateViewStyle(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }



    private void updateViewStyle(int currentPosition){
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),images[currentPosition]);
        PaletteHelper.generate(bitmap,ContextCompat.getColor(this,R.color.colorPrimary),(color)->{
            Drawable tabLayoutBackground= tabLayout.getBackground();
            if(tabLayout.getBackground() instanceof  ColorDrawable){
                int startColor=((ColorDrawable)tabLayoutBackground).getColor();
                ColorTransition.builder()
                        .duration(200)
                        .startColor(startColor)
                        .endColor(color)
                        .build()
                        .translate((currentColor)->setTabLayoutAndStatusBarBackgroundColor(currentColor));
            }




        });
    }

    private void setTabLayoutAndStatusBarBackgroundColor(int color){
        tabLayout.setBackgroundColor(color);
        StatusBarUtils.setStatusBarColor(MainActivity.this,color);
    }



    private class Adapter extends PagerAdapter{





        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView=new ImageView(MainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(images[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,Main2Activity.class));
                }
            });
            return imageView;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "title:"+(++position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }



}
