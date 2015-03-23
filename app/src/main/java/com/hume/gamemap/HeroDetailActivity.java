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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hume.gamemap.Adapter.HeroAbilitiesAdapter;
import com.hume.gamemap.Adapter.HeroDetailItem;
import com.hume.gamemap.Adapter.HeroItem;
import com.hume.gamemap.Adapter.HeroSkillupAdapter;
import com.hume.gamemap.Adapter.Hero_base_fragment;
import com.hume.gamemap.Adapter.ItemsImagesAdapter;
import com.hume.gamemap.Adapter.ItemsItem;
import com.hume.gamemap.Adapter.MyViewPagerAdapter;
import com.hume.gamemap.Fragment_View.SimpleGridView;
import com.hume.gamemap.Fragment_View.SimpleListView;
import com.hume.gamemap.MenuClass.PagerSlidingTabStrip;

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
    private Hero_base_fragment herobase;

    private View mView;
    private HeroDetailItem herodata = null;
    private int level_hero = 1;//初始等级
    private int init_hp,init_mp;//初始血量和魔法
    private int init_int,init_agi,init_str;//初始属性
    private int init_dmg_max,init_dmg_min;
    private double init_armor;
    private double lv_int,lv_agi,lv_str;//属性成长
    private double lv_dmg,lv_armor,lv_mp,lv_hp;

    private int hero_int,hero_agi,hero_str;//英雄属性
    private int hero_armor,hero_dmg_max,hero_dmg_min;//护甲，攻击，移速
    private int hero_hp,hero_mp;
    private int hpIndex;
    private TextView text_hero_hp,text_hero_mp,text_level,text_hero_int,text_hero_agi,text_hero_str;
    private TextView text_hero_name_l,text_hero_name,text_hero_roles,text_hero_hp_faction_atk;
    private TextView text_hero_bio,text_hero_shiye,text_hero_dandao,text_hero_dansudu;
    private TextView text_hero_ms,text_hero_dmg,text_hero_armor,text_lv_fu,text_lv_jia;
    private ImageView image_prime,image_hero;
    private String hero_nickname,hero_role,hero_att,hero_faction;
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
        LayoutInflater inflater = getLayoutInflater();
        mView = inflater.inflate(R.layout.activity_herodetail, null);//载入基本介绍界面布局;
        setContextview();//载入基本介绍界面布局;
//        initviews();
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
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_dotadetail);
        //mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mtoolbar, R.string.drawer_open,R.string.drawer_close);
        //mDrawerToggle.syncState();
        //mDrawerLayout.setDrawerListener(mDrawerToggle);
//        titleList.add("基本介绍");
//        titleList.add("技能加点");
//        titleList.add("出装推荐");
//        titleList.add("英雄攻略");
//        titleList.add("教学视频");
//        View view2 = new Hero_skill_fragment(this,inflater,hero_keyname).setContextview();//载入技能加点界面布局
//        View view3 = new Hero_item_fragment(this,inflater,hero_keyname).setContextview();//载入出装推荐界面布局
//        viewList.add(view1);
//        viewList.add(view2);
//        viewList.add(view3);
        //mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs_dotadetail);
//        mViewPager = (ViewPager) findViewById(R.id.pager_dotadetail);
//        mViewPager.setAdapter(new MyViewPagerAdapter(viewList,titleList));
        //mPagerSlidingTabStrip.setViewPager(mViewPager);
//        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int arg0) {
//                colorChange(arg0);
//            }
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {}
//            @Override
//            public void onPageScrollStateChanged(int arg0) {}
//        });
    }
    public void setContextview(){
        try {
            herodata = DataManager.getHeroDetailItem(this,hero_keyname);//获取详细信息
            herolist = DataManager.getHeroItem(this,hero_keyname);//获取基本信息
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        init_dota_info();//初始化控件
        /*基本信息——图像，名称，定位*/
        bitmap = getImageFromAssetsFile(Utils.getHeroImageUri(herolist.keyName));
        hero_nickname = herolist.nickname_l[0];
        hero_role = herolist.roles_l[0];
        for(int i=1; i<herolist.nickname_l.length; ++i){
            hero_nickname += "/"+herolist.nickname_l[i];
        }
        for(int i=1; i<herolist.roles_l.length; ++i){
            hero_role += "/"+herolist.roles_l[i];
        }
        switch (herolist.hp) {
            case "intelligence":
                hero_att = "智力";
                hpIndex = 0;
                image_prime = (ImageView)mView.findViewById(R.id.image_hero_int_icon_primary);
                break;
            case "agility":
                hero_att = "敏捷";
                hpIndex = 1;
                image_prime = (ImageView)mView.findViewById(R.id.image_hero_agi_icon_primary);
                break;
            default:
                hero_att = "力量";
                hpIndex = 2;
                image_prime = (ImageView)mView.findViewById(R.id.image_hero_str_icon_primary);
                break;
        }
        if(herolist.faction.equals("radiant")){
            hero_faction = "天辉";
        }else {
            hero_faction = "夜魇";
        }
        dota_lv_init();//数据初始化
        init_dota_set();//初始设置
        /*设置等级减少的监听*/
        text_lv_fu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level_hero -= 1;
                if(level_hero==0){
                    level_hero = 1;
                }
                dota_lv_up(level_hero);
                dota_lv_stats();
            }
        });
        /*设置等级增加的监听*/
        text_lv_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level_hero += 1;
                if(level_hero==26){
                    level_hero = 25;
                }
                dota_lv_up(level_hero);
                dota_lv_stats();
            }
        });
        dota_lv_up(level_hero);//获取属性值
        dota_lv_stats();//属性设置

        // 绑定视图——技能
        if (herodata.abilities != null && herodata.abilities.size() > 0) {
            final HeroAbilitiesAdapter adapter = new HeroAbilitiesAdapter(this, herodata.abilities,herolist);
            final SimpleListView list = Utils.findById(mView, R.id.list_hero_abilities);
            list.setAdapter(adapter);
            // list.setOnItemClickListener(this);
        }
        else {
            mView.findViewById(R.id.llayout_hero_abilities).setVisibility(View.GONE);
        }

        // 绑定视图——技能加点
        if (herodata.skillup != null && herodata.skillup.size() > 0) {
            final HeroSkillupAdapter adapter = new HeroSkillupAdapter(this, herodata.skillup);
            final SimpleListView list = Utils.findById(mView, R.id.list_hero_skillup);
            list.setAdapter(adapter);
            // list.setOnItemClickListener(this);
        }
        else {
            mView.findViewById(R.id.llayout_hero_skillup).setVisibility(View.GONE);
        }
        /* 出装推荐视图绑定 */
        bindItembuildsItems(mView, herodata, "Starting",R.id.llayout_hero_itembuilds_starting,R.id.grid_hero_itembuilds_starting);
        bindItembuildsItems(mView, herodata, "Early",R.id.llayout_hero_itembuilds_early,R.id.grid_hero_itembuilds_early);
        bindItembuildsItems(mView, herodata, "Core",R.id.llayout_hero_itembuilds_core,R.id.grid_hero_itembuilds_core);
        bindItembuildsItems(mView, herodata, "Luxury",R.id.llayout_hero_itembuilds_luxury,R.id.grid_hero_itembuilds_luxury);
        //return mView;
    }
    /**
     * 初始化界面控件
     */
    private void init_dota_info(){
        image_hero = (ImageView)mView.findViewById(R.id.image_hero);//英雄图像
        text_hero_name_l = (TextView)mView.findViewById(R.id.text_hero_name_l);//中文名称
        text_hero_name = (TextView)mView.findViewById(R.id.text_hero_name);//英文名称
        text_hero_roles = (TextView)mView.findViewById(R.id.text_hero_roles);//角色定位
        text_hero_hp_faction_atk = (TextView)mView.findViewById(R.id.text_hero_hp_faction_atk);//英雄类型
        text_hero_bio = (TextView)mView.findViewById(R.id.text_hero_bio);//英雄介绍
        text_hero_shiye = (TextView)mView.findViewById(R.id.text_hero_shiye);//视野范围
        text_hero_dandao = (TextView)mView.findViewById(R.id.text_hero_dandao);//攻击距离
        text_hero_dansudu = (TextView)mView.findViewById(R.id.text_hero_dansudu);//弹道速度
        text_hero_ms = (TextView)mView.findViewById(R.id.text_hero_ms_value);//移速
        text_lv_jia = (TextView)mView.findViewById(R.id.lv_jia);//等级增加
        text_lv_fu = (TextView)mView.findViewById(R.id.lv_fu);//等级减少

        text_hero_hp = (TextView)mView.findViewById(R.id.hp_text);//英雄血量
        text_hero_mp = (TextView)mView.findViewById(R.id.mp_text);//英雄蓝量
        text_level = (TextView)mView.findViewById(R.id.text_level);//英雄等级

        text_hero_int = (TextView)mView.findViewById(R.id.text_hero_int_value);//智力
        text_hero_agi = (TextView)mView.findViewById(R.id.text_hero_agi_value);//敏捷
        text_hero_str = (TextView)mView.findViewById(R.id.text_hero_str_value);//力量
        text_hero_dmg = (TextView)mView.findViewById(R.id.text_hero_dmg_value);//攻击
        text_hero_armor = (TextView)mView.findViewById(R.id.text_hero_armor_value);//护甲
    }

    /**
     * 固定位置的文本设置
     */
    private void init_dota_set(){
        image_hero.setImageBitmap(bitmap);
        text_hero_name_l.setText(herolist.name_l);
        text_hero_name.setText(hero_nickname);
        text_hero_roles.setText(hero_role);
        text_hero_hp_faction_atk.setText(herolist.atk_l+"/"+hero_faction+"/"+hero_att);
        text_hero_bio.setText("       "+herodata.bio_l);
        text_hero_shiye.setText(herodata.detailstats2.get(0)[1]);
        text_hero_dandao.setText(herodata.detailstats2.get(1)[1]);
        text_hero_dansudu.setText(herodata.detailstats2.get(2)[1]);
        image_prime.setVisibility(View.VISIBLE);
        text_hero_ms.setText(herodata.stats1.get(4)[2]);
    }
    /**
     * 模拟英雄等级
     * @param level_hero
     * level of hero
     */
    private void dota_lv_up(int level_hero) {
        hero_int = (int)(init_int+(level_hero-1)*lv_int);
        hero_agi = (int)(init_agi+(level_hero-1)*lv_agi);
        hero_str = (int)(init_str+(level_hero-1)*lv_str);
        hero_dmg_max = (int)(init_dmg_max+(level_hero-1)*lv_dmg);
        hero_dmg_min = (int)(init_dmg_min+(level_hero-1)*lv_dmg);
        hero_armor = (int)(init_armor+(level_hero-1)*lv_agi*lv_armor);
        hero_hp = (int)(init_hp+(level_hero-1)*lv_str*lv_hp);
        hero_mp = (int)(init_mp+(level_hero-1)*lv_int*lv_mp);
    }

    /**
     * 初始化英雄属性
     */
    private void dota_lv_init() {
        /*智力*/
        init_int = (int)herolist.statsall.init_int;
        lv_int = herolist.statsall.lv_int;
        /*敏捷*/
        init_agi = (int)herolist.statsall.init_agi;
        lv_agi = herolist.statsall.lv_agi;
         /*力量*/
        init_str = (int)herolist.statsall.init_str;
        lv_str = herolist.statsall.lv_str;
        /*攻击*/
        init_dmg_min = (int)herolist.statsall.init_min_dmg;
        init_dmg_max = (int)herolist.statsall.init_max_dmg;
        lv_dmg = herolist.statsall.lv_dmg;
        /*护甲*/
        init_armor = herolist.statsall.init_armor;
        lv_armor = herolist.statsall.lv_armor;
        /*血量*/
        init_hp = (int)herolist.statsall.init_hp;
        lv_hp = herolist.statsall.lv_hp;
        /*蓝量*/
        init_mp = (int)herolist.statsall.init_mp;
        lv_mp = herolist.statsall.lv_mp;
    }

    /*设置属性数值*/
    private void dota_lv_stats(){
        text_hero_hp.setText("HP:" + String.valueOf(hero_hp) + "/" + String.valueOf(hero_hp));
        text_hero_mp.setText("MP:"+String.valueOf(hero_mp)+"/"+String.valueOf(hero_mp));
        text_level.setText(String.valueOf(level_hero));
        text_hero_int.setText(String.valueOf(hero_int)+" + "+String.valueOf(lv_int));
        text_hero_agi.setText(String.valueOf(hero_agi)+" + "+String.valueOf(lv_agi));
        text_hero_str.setText(String.valueOf(hero_str)+" + "+String.valueOf(lv_str));
        text_hero_dmg.setText(String.valueOf(hero_dmg_min)+" - "+String.valueOf(hero_dmg_max));
        text_hero_armor.setText(String.valueOf(hero_armor));
        switch (hpIndex){
            case 0:
                text_hero_int.setTextColor(Color.RED);
                break;
            case 1:
                text_hero_agi.setTextColor(Color.RED);
                break;
            case 2:
                text_hero_str.setTextColor(Color.RED);
                break;
            default:
                break;
        }
    }
    /**
     * 绑定视图——推荐出装
     * @param cView
     * cview
     * @param cItem
     * cItem
     * @param cItembuildsKey
     * cItembuildsKey
     * @param layoutResId
     * layoutResId
     * @param itemsGridResId
     * itemsGridResId
     */
    private void bindItembuildsItems(View cView, HeroDetailItem cItem,String cItembuildsKey,int layoutResId, int itemsGridResId) {
        if(cItem.itembuilds != null && cItem.itembuilds.size() > 0)
        {
            final ArrayList<ItemsItem> cItembuilds = new ArrayList<>();
            final String[] cItemsb = cItem.itembuilds.get(cItembuildsKey);
            for (String aCItemsb : cItemsb) {
                ItemsItem itemsItem = null;
                try {
                    itemsItem = DataManager.getItemsItem(this, aCItemsb);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                cItembuilds.add(itemsItem);
            }
            final ItemsImagesAdapter adapter = new ItemsImagesAdapter(this, cItembuilds);
            final SimpleGridView gridview = (SimpleGridView)cView.findViewById(itemsGridResId);
            gridview.setAdapter(adapter);
//            gridview.setOnItemClickListener(this);//设置监听
            cView.findViewById(layoutResId).setVisibility(View.VISIBLE);
        }
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
