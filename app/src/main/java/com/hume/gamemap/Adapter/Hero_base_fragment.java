package com.hume.gamemap.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hume.gamemap.DataManager;
import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**英雄基本介绍界面
 * Created by tcp on 2015/1/26.
 */
public class Hero_base_fragment{
    private View mView;
    private Context mcontext;
    private String hero_keyname;
    private HeroDetailItem herodata = null;
    private HeroItem herolist = null;
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
    private Bitmap bitmap;
    public Hero_base_fragment(Context mcontext,LayoutInflater inflater,String keyname){
        this.mView = inflater.inflate(R.layout.fragment_herodetail, null);//载入基本介绍界面布局;
        this.mcontext = mcontext;
        this.hero_keyname = keyname;
    }

    public View setContextview(){
        try {
            herodata = DataManager.getHeroDetailItem(mcontext,hero_keyname);//获取详细信息
            herolist = DataManager.getHeroItem(mcontext,hero_keyname);//获取基本信息
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
        return mView;
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
     * 读取图像文件
     * @param fileName
     * filename
     * @return
     * bitmap
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = mcontext.getResources().getAssets();
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
