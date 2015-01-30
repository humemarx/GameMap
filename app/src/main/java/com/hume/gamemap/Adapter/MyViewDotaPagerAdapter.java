package com.hume.gamemap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hume.gamemap.HeroDetailActivity;
import com.hume.gamemap.ItemsDetailActivity;
import com.hume.gamemap.R;

import java.io.InputStream;
import java.util.List;

/**
 * Created by tcp on 2015/1/26.
 */
public class MyViewDotaPagerAdapter extends PagerAdapter{
    private List<View> mListViews;
    private List<String> mListTitle;
    private Context mcontext;
    private List<HeroItem> mHeroList;
    private List<ItemsItem> mItemList;
    public MyViewDotaPagerAdapter(Context context,List<HeroItem>HeroList,List<ItemsItem>ItemList,
                                  List<View> mListViews,List<String> mListTitle) {
        this.mListViews = mListViews;
        this.mListTitle = mListTitle;
        this.mcontext = context;
        this.mHeroList = HeroList;
        this.mItemList = ItemList;
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
        switch (position){
            case 0:
                HeroImagesAdapter heroadapter = new HeroImagesAdapter(mcontext, mHeroList);
                GridView gridview1 = (GridView)mListViews.get(position).findViewById(R.id.herodata_grid);
                gridview1.setAdapter(heroadapter);
                gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(mcontext,HeroDetailActivity.class);//跳转英雄详细界面
                        intent.putExtra("heroitem",mHeroList.get(position).keyName);//传递数据
                        mcontext.startActivity(intent);//启动新的活动
                    }
                });
                break;
            case 1:
                ItemsImagesAdapter itemadapter = new ItemsImagesAdapter(mcontext, mItemList);
                GridView gridview2 = (GridView)mListViews.get(position).findViewById(R.id.itemsdata_grid);
                gridview2.setAdapter(itemadapter);
                gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(mcontext,ItemsDetailActivity.class);//跳转物品详细界面
                        intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_KEY_NAME,mItemList.get(position).keyName);//传递数据,该名称
                        if (!TextUtils.isEmpty(mItemList.get(position).parent_keyName)) {
                            intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_PARENT_KEY_NAME,mItemList.get(position).parent_keyName);//合成名称
                        }
                        mcontext.startActivity(intent);//启动新的活动
                    }
                });
                break;
            default:
                break;
        }
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

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent;
//        Toast.makeText(mcontext,"nothing",Toast.LENGTH_LONG).show();
//        switch (view.getId()){
//            case R.id.herodata_grid:
//                Toast.makeText(mcontext,"dota_grid",Toast.LENGTH_LONG).show();
//                intent = new Intent(mcontext,HeroDetailActivity.class);//跳转英雄详细界面
//                intent.putExtra("heroitem",mHeroList.get(position).keyName);//传递数据
//                mcontext.startActivity(intent);//启动新的活动
//                break;
//            case R.id.itemsdata_grid:
//                Toast.makeText(mcontext,"item_grid",Toast.LENGTH_LONG).show();
//                intent = new Intent(mcontext,ItemsDetailActivity.class);//跳转物品详细界面
//                intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_KEY_NAME,mItemList.get(position).keyName);//传递数据,该名称
//                if (!TextUtils.isEmpty(mItemList.get(position).parent_keyName)) {
//                    intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_PARENT_KEY_NAME,mItemList.get(position).parent_keyName);//合成名称
//                }
//                mcontext.startActivity(intent);//启动新的活动
//                break;
//            default:
//                Toast.makeText(mcontext,"default",Toast.LENGTH_LONG).show();
//                break;
//        }
//    }
}
