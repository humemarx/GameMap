package com.hume.gamemap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hume.gamemap.Fragment_View.SuperAwesomeCardFragment;
import com.hume.gamemap.MenuClass.PagerSlidingTabStrip;
import com.hume.gamemap.MenuClass.SlideMenu;

import java.io.InputStream;

/**启动主界面
 * Created by tcp on 2015/1/23.
 */
public class MainActivity extends ActionBarActivity implements Toolbar.OnMenuItemClickListener,View.OnClickListener{
    private SlideMenu slideMenu;
    private ImageView dota_image,lol_image;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mtoolbar;
    private ShareActionProvider mShareActionProvider;
    private LinearLayout mslider;
    private static int first_color;
    private TextView text_frist,text_dota,text_lol,text_score,text_video,text_news,text_set,text_myhero,text_quit;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);
        first_color = R.drawable.dota06;
        initviews();
    }

    private void initviews() {
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);//标题栏
        mslider = (LinearLayout)findViewById(R.id.drawer_view);//侧边栏
        text_frist = (TextView)findViewById(R.id.text_menu_first);//首页
        text_score = (TextView)findViewById(R.id.text_menu_score);//分数查询
        text_dota = (TextView)findViewById(R.id.text_menu_dota);//dota数据库
        text_lol = (TextView)findViewById(R.id.text_menu_lol);//lol数据库
        text_myhero = (TextView)findViewById(R.id.text_menu_hero);//我的英雄
        text_news = (TextView)findViewById(R.id.text_menu_news);//新闻资讯
        text_video = (TextView)findViewById(R.id.text_menu_video);//视频直播
        text_set = (TextView)findViewById(R.id.text_menu_setting);//设置
        text_quit = (TextView)findViewById(R.id.text_menu_quit);//退出

        mtoolbar.setTitle("今日英雄");
        setSupportActionBar(mtoolbar);

        /*设置监听*/
        mtoolbar.setOnMenuItemClickListener(this);
        /*设置监听*/
        mtoolbar.setOnMenuItemClickListener(this);
        text_frist.setOnClickListener(this);
        text_dota.setOnClickListener(this);
        text_lol.setOnClickListener(this);
        text_score.setOnClickListener(this);
        text_myhero.setOnClickListener(this);
        text_video.setOnClickListener(this);
        text_set.setOnClickListener(this);
        text_quit.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*设置颜色*/
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream is = getResources().openRawResource(first_color);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener(){
                    @Override
                    public void onGenerated(Palette palette){
                        Palette.Swatch vibrant = palette.getVibrantSwatch();
                        mtoolbar.setBackgroundColor(colorBurn(vibrant.getRgb()));
                        mslider.setBackgroundColor(vibrant.getRgb());
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            Window window = getWindow();
                            // 很明显，这两货是新API才有的。
                            window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                            window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                        }
                    }
                });

		/* findView */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
//        mPagerSlidingTabStrip.setViewPager(mViewPager);
//        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int arg0) {
//                colorChange(arg0);
//            }
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });
//        initTabsValue();
    }
    /**
     * mPagerSlidingTabStrip默认值配置
     *
     */
    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }
    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        Bitmap bitmap = SuperAwesomeCardFragment.getBackgroundBitmapPosition(MainActivity.this,position);
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
                mslider.setBackgroundColor(vibrant.getRgb());
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
                Toast.makeText(MainActivity.this, "action_settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(MainActivity.this, "action_share",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_menu_first:
                Intent intent01 = new Intent(this,MainActivity.class);
                startActivity(intent01);
                this.finish();
                break;
            case R.id.text_menu_score:
                break;
            case R.id.text_menu_dota:
                Intent intent02 = new Intent(this,MainDotaActivity.class);
                startActivity(intent02);
                this.finish();
            case R.id.text_menu_lol:
                break;
            case R.id.text_menu_hero:
                break;
            case R.id.text_menu_video:
                break;
            case R.id.text_menu_setting:
                break;
            case R.id.text_menu_quit:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
		/* ShareActionProvider配置 */
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch (item.getItemId()) {
        // case R.id.action_settings:
        // Toast.makeText(MainActivity.this, "action_settings", 0).show();
        // break;
        // case R.id.action_share:
        // Toast.makeText(MainActivity.this, "action_share", 0).show();
        // break;
        // default:
        // break;
        // }
        return super.onOptionsItemSelected(item);
    }
    /* ***************FragmentPagerAdapter***************** */
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = { "DOTA2", "英雄联盟",};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return SuperAwesomeCardFragment.newInstance(position);
        }
    }
}
