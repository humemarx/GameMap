package com.hume.gamemap;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.hume.gamemap.Adapter.HeroItem;
import com.hume.gamemap.Adapter.Hero_base_fragment;
import com.hume.gamemap.Adapter.Hero_skill_fragment;
import com.hume.gamemap.Adapter.MyViewPagerAdapter;
import com.hume.gamemap.MenuClass.PagerSlidingTabStrip;
import com.hume.gamemap.ViewClass.Hero_item_fragment;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcp on 2015/1/26.
 */
public class HeroDetailActivity extends ActionBarActivity implements Toolbar.OnMenuItemClickListener{
    private String hero_keyname;
    private HeroItem herolist = null;
    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private List<View> viewList = new ArrayList<>();// 分页视图
    private List<String>titleList = new ArrayList<>();//分页标题
    private Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herodetail);
        Slidr.attach(this);//右滑退出
        /*数据初始化*/
        hero_keyname = this.getIntent().getStringExtra("heroitem");//获取英雄数据名称
        try {
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        bitmap = getImageFromAssetsFile(Utils.getHeroImageUri(herolist.keyName));
        initviews();
        initColors();
    }

    private void initviews() {
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);//标题栏
        mtoolbar.setTitle(herolist.name_l);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         /*设置监听*/
        mtoolbar.setOnMenuItemClickListener(this);
        /* findView */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_dotadetail);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        LayoutInflater inflater = getLayoutInflater();
        titleList.add("基本介绍");
        titleList.add("技能加点");
        titleList.add("出装推荐");
//        titleList.add("英雄攻略");
//        titleList.add("教学视频");

        View view1 = new Hero_base_fragment(this,inflater,hero_keyname).setContextview();//载入基本介绍界面布局;
        View view2 = new Hero_skill_fragment(this,inflater,hero_keyname).setContextview();//载入技能加点界面布局
        View view3 = new Hero_item_fragment(this,inflater,hero_keyname).setContextview();//载入出装推荐界面布局
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs_dotadetail);
        mViewPager = (ViewPager) findViewById(R.id.pager_dotadetail);
        mViewPager.setAdapter(new MyViewPagerAdapter(viewList,titleList));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                colorChange(arg0);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }
    /**
     * 设置初始颜色
     *
     */
    private void initColors() {
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5, getResources().getDisplayMetrics()));
         /*设置颜色初始*/
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                // tab背景
                mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                //正常文字颜色
                mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                // 选中的文字颜色
                mPagerSlidingTabStrip.setSelectedTextColor(Color.BLACK);
                //底部游标颜色
                mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));
                //标题栏和侧边栏颜色
                mtoolbar.setBackgroundColor(colorBurn(vibrant.getRgb()));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                    window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                }
            }
        });
    }
    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = MyViewPagerAdapter.getBackgroundBitmapPosition(this, position);
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
				/* 界面颜色UI统一性处理,看起来更Material一些 */
                mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
                mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));
                mtoolbar.setBackgroundColor(colorBurn(vibrant.getRgb()));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                    window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                }
            }
        });
    }
    /**
     * 颜色加深处理
     *
     * @param RGBValues
     *            RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *            Android中我们一般使用它的16进制，
     *            例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *            red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *            所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }
    /*监听响应*/
    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "action_settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(this, "action_share",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
    /**
     * 读取图像文件
     * @param fileName
     * filename
     * @return
     * bitmap
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
