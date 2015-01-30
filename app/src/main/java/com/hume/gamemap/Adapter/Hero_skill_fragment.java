package com.hume.gamemap.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hume.gamemap.DataManager;
import com.hume.gamemap.Fragment_View.SimpleListView;
import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tcp on 2015/1/26.
 */
public class Hero_skill_fragment {
    private View mView;
    private Context mcontext;
    private String hero_keyname;
    private HeroDetailItem herodata = null;
    private HeroItem herolist = null;
    private TextView text_hero_name_l,text_hero_name,text_hero_roles,text_hero_hp_faction_atk;
    private ImageView image_hero;
    private String hero_nickname,hero_role,hero_att,hero_faction;
    private Bitmap bitmap;
    public Hero_skill_fragment(Context mcontext,LayoutInflater inflater,String keyname){
        this.mView = inflater.inflate(R.layout.fragment_heroskill, null);//载入技能加点界面布局
        this.mcontext = mcontext;
        this.hero_keyname = keyname;
    }
    public View setContextview(){
        try {
            herodata = DataManager.getHeroDetailItem(mcontext, hero_keyname);//获取详细信息
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
                break;
            case "agility":
                hero_att = "敏捷";
                break;
            default:
                hero_att = "力量";
                break;
        }
        if(herolist.faction.equals("radiant")){
            hero_faction = "天辉";
        }else {
            hero_faction = "夜魇";
        }
        init_dota_set();

        // 绑定视图——技能
        if (herodata.abilities != null && herodata.abilities.size() > 0) {
            final HeroAbilitiesAdapter adapter = new HeroAbilitiesAdapter(mcontext, herodata.abilities,herolist);
            final SimpleListView list = Utils.findById(mView, R.id.list_hero_abilities);
            list.setAdapter(adapter);
            // list.setOnItemClickListener(this);
        }
        else {
            mView.findViewById(R.id.llayout_hero_abilities).setVisibility(View.GONE);
        }

        // 绑定视图——技能加点
        if (herodata.skillup != null && herodata.skillup.size() > 0) {
            final HeroSkillupAdapter adapter = new HeroSkillupAdapter(mcontext, herodata.skillup);
            final SimpleListView list = Utils.findById(mView, R.id.list_hero_skillup);
            list.setAdapter(adapter);
            // list.setOnItemClickListener(this);
        }
        else {
            mView.findViewById(R.id.llayout_hero_skillup).setVisibility(View.GONE);
        }
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
