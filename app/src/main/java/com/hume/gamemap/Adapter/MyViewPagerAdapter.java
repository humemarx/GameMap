package com.hume.gamemap.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hume.gamemap.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by humemarx on 2015/1/24.
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private List<View> mListViews;
    private List<String> mListTitle;
    public MyViewPagerAdapter(List<View> mListViews,List<String> mListTitle) {
        this.mListViews = mListViews;
        this.mListTitle = mListTitle;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mListTitle.get(position);// 设置标题

    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(mListViews.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        container.addView(mListViews.get(position), 0);//添加页卡
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return  mListViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;      //官方提示这样写
    }

    /**
     * 提供当前Fragment的主色调的Bitmap对象,供Palette解析颜色
     *
     * @return
     */
    public static Bitmap getBackgroundBitmapPosition(Context mcontext,int selectViewPagerItem) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        int bgres;
        if(selectViewPagerItem==0){
            bgres = R.drawable.dota06;
        }else{
            bgres = R.drawable.dota07;
        }
        InputStream is = mcontext.getResources().openRawResource(bgres);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        return bm;
    }
}
