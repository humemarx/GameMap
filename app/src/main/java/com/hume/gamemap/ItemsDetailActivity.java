package com.hume.gamemap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import com.hume.gamemap.Adapter.Item_fragment;
import com.hume.gamemap.Adapter.ItemsItem;
import com.hume.gamemap.Adapter.MyViewDetailPager;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcp on 2015/1/26.
 */
public class ItemsDetailActivity extends ActionBarActivity{
    /* 物品名称 Intent 参数 */
    public final static String KEY_ITEMS_DETAIL_KEY_NAME = "KEY_ITEMS_DETAIL_KEY_NAME";
    /* 父物品名称(合成卷轴使用) Intent 参数 */
    public final static String KEY_ITEMS_DETAIL_PARENT_KEY_NAME = "KEY_ITEMS_DETAIL_PARENT_KEY_NAME";
    private String item_keyname,item_parentname;
    private ItemsItem itemslist = null;
    private Toolbar mtoolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mViewPager;
    private List<View> viewList = new ArrayList<>();// 分页视图
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdetail);
        Slidr.attach(this);//右滑退出
        /*数据初始化*/
        item_keyname = this.getIntent().getStringExtra(KEY_ITEMS_DETAIL_KEY_NAME);//获取物品数据名称
        item_parentname = this.getIntent().getStringExtra(KEY_ITEMS_DETAIL_PARENT_KEY_NAME);//父物品名称
        final boolean isrecipe = item_keyname.equals(DataManager.KEY_NAME_RECIPE_ITEMS_KEYNAME);//判断是否是卷轴
        try {
            if(isrecipe){
                itemslist = DataManager.getItemsItem(this, item_parentname);
            }else{
                itemslist = DataManager.getItemsItem(this, item_keyname);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        initviews();
        initColors();

    }
    /*初始化*/
    private void initviews(){
        mtoolbar = (Toolbar)findViewById(R.id.toolbar);//标题栏
        mtoolbar.setTitle(itemslist.dname_l);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_itemdetail);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        LayoutInflater inflater = getLayoutInflater();
        View view1 = new Item_fragment(this,inflater,item_keyname,item_parentname).setContextview();//载入布局;
        viewList.add(view1);
        mViewPager = (ViewPager) findViewById(R.id.pager_itemdetail);
        mViewPager.setAdapter(new MyViewDetailPager(viewList));
    }

    /**
     * 设置初始颜色
     *
     */
    private void initColors() {
         /*设置颜色初始*/
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        int bg = R.drawable.dota06;
        Bitmap bitmap = BitmapFactory.decodeStream(getResources().openRawResource(bg), null, opt);
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                //标题栏和侧边栏颜色
                mtoolbar.setBackgroundColor(vibrant.getRgb());
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(vibrant.getRgb());
                    window.setNavigationBarColor(vibrant.getRgb());
                }
            }
        });
    }
}
