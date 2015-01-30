package com.hume.gamemap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hume.gamemap.DataManager;
import com.hume.gamemap.Fragment_View.SimpleGridView;
import com.hume.gamemap.HeroDetailActivity;
import com.hume.gamemap.ItemsDetailActivity;
import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tcp on 2015/1/27.
 */
public class Item_fragment implements SimpleGridView.OnItemClickListener{
    private Context mcontext;
    private View mView;
    private String item_keyname,item_parentname;
    private ImageView item_icon;
    private TextView item_dname_l,item_dname,item_cost,item_mc,item_cd,item_attrib,item_desc,item_notes,item_lore;
    private ItemsItem itemslist = null;
    public Item_fragment(Context context,LayoutInflater inflater,String keyname,String keyname_parent){
        this.mcontext = context;
        this.mView = inflater.inflate(R.layout.fragment_itemsdetail, null);//载入物品界面布局;
        this.item_keyname = keyname;
        this.item_parentname = keyname_parent;
    }
    public View setContextview(){
        final boolean isrecipe = item_keyname.equals(DataManager.KEY_NAME_RECIPE_ITEMS_KEYNAME);//判断是否是卷轴
        if(isrecipe){
            item_keyname = item_parentname;
        }
        try {
            itemslist = DataManager.getItemsItem(mcontext, item_keyname);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        item_icon = (ImageView)mView.findViewById(R.id.image_items);//物品图标
        item_dname_l = (TextView)mView.findViewById(R.id.text_items_dname_l);//物品名称
        item_dname = (TextView)mView.findViewById(R.id.text_items_dname);//物品简称
        item_cost = (TextView)mView.findViewById(R.id.text_items_cost);//物品金钱
        item_mc = (TextView)mView.findViewById(R.id.text_items_mana);//魔法消耗
        item_cd = (TextView)mView.findViewById(R.id.text_items_cd);//冷却时间
        item_attrib = (TextView)mView.findViewById(R.id.text_items_attrib);//属性加成
        item_desc = (TextView)mView.findViewById(R.id.text_items_desc);
        item_notes = (TextView)mView.findViewById(R.id.text_items_notes);
        item_lore = (TextView)mView.findViewById(R.id.text_items_lore);

        /*基本信息处理*/
        if(isrecipe){//卷轴
            Bitmap bitmap = getImageFromAssetsFile(Utils.getItemsImageUri(DataManager.KEY_NAME_RECIPE_ITEMS_KEYNAME));//获取头像
            item_icon.setImageBitmap(bitmap);//设置头像
            item_dname_l.setText(itemslist.dname_l+itemslist.components_i.get(itemslist.components_i.size() - 1).dname_l);//设置名称
            item_dname.setText(itemslist.dname+itemslist.components_i.get(itemslist.components_i.size() - 1).dname);//设置简称
            item_cost.setText(String.valueOf(itemslist.components_i.get(itemslist.components_i.size() - 1).cost));//设置金钱
        }else{
            Bitmap bitmap = getImageFromAssetsFile(Utils.getItemsImageUri(itemslist.keyName));//获取头像
            item_icon.setImageBitmap(bitmap);//设置头像
            item_dname_l.setText(itemslist.dname_l);//设置名称
            item_dname.setText(itemslist.dname);//设置简称
            item_cost.setText(String.valueOf(itemslist.cost));//设置金钱
        }

        /*合成卷轴处理*/
        if (isrecipe) {
            final View layout_items_desc = mView.findViewById(R.id.layout_items_desc);
            layout_items_desc.setVisibility(View.GONE);
        }
        Utils.bindHtmlTextView(item_desc,itemslist.desc);
        Utils.bindHtmlTextView(item_attrib,itemslist.attrib);
        Utils.bindHtmlTextView(item_notes,itemslist.notes);
        Utils.bindHtmlTextView(item_lore,itemslist.lore);
        /*魔法消耗处理-mc*/
        // mc
        if (!TextUtils.isEmpty(itemslist.mc)) {
            item_mc.setText(itemslist.mc);
        } else {
            mView.findViewById(R.id.rlayout_items_mana).setVisibility(View.GONE);
        }
        /*冷却时间处理-cd*/
        if (itemslist.cd > 0) {
            item_cd.setText(String.valueOf(itemslist.cd));
        } else {
            mView.findViewById(R.id.rlayout_items_cd).setVisibility(View.GONE);
        }

               /*合成所需处理*/
        if (itemslist.components != null && itemslist.components.length > 0) {
            final ItemsImagesAdapter adapter = new ItemsImagesAdapter(mcontext, itemslist.components_i);
            final SimpleGridView grid = (SimpleGridView) mView.findViewById(R.id.grid_items_components);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(this);
            mView.findViewById(R.id.llayout_items_components).setVisibility(View.VISIBLE);
        } else {
            mView.findViewById(R.id.llayout_items_components).setVisibility(View.GONE);
        }

        /*可以合成物品*/
        if (itemslist.tocomponents != null && itemslist.tocomponents.length > 0) {
            final ItemsImagesAdapter adapter = new ItemsImagesAdapter(mcontext, itemslist.tocomponents_i);
            final SimpleGridView grid = (SimpleGridView) mView.findViewById(R.id.grid_items_tocomponents);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(this);//设置监听
            mView.findViewById(R.id.llayout_items_tocomponents).setVisibility(View.VISIBLE);
        } else {
            mView.findViewById(R.id.llayout_items_tocomponents).setVisibility(View.GONE);
        }

        /*推荐使用英雄*/
        if (itemslist.toheros != null && itemslist.toheros.length > 0) {
            final HeroImagesAdapter adapter = new HeroImagesAdapter(mcontext, itemslist.toheros_i);
            final SimpleGridView grid = (SimpleGridView) mView.findViewById(R.id.grid_items_toheros);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(this);//设置监听
            mView.findViewById(R.id.llayout_items_toheros).setVisibility(View.VISIBLE);
        } else {
            mView.findViewById(R.id.llayout_items_toheros).setVisibility(View.GONE);
        }
        return mView;
    }

    /**
     * 获取文件图像
     * @param fileName
     * fileName
     * @return
     * return
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = mcontext.getResources().getAssets();
        try{
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    /**
     *
     * @param parent
     *            The AdapterView where the click happened.
     * @param view
     *            The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position
     *            The position of the view in the adapter.
     * @param id
     *            The id of the view in the adapter.
     */
    @Override
    public void onItemClick(ListAdapter parent, View view, int position, long id) {
        Intent intent = null;
        if(parent instanceof ItemsImagesAdapter){
            final ItemsItem cItem = (ItemsItem)parent.getItem(position);
            intent = new Intent(mcontext,ItemsDetailActivity.class);//跳转物品界面
            intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_KEY_NAME,cItem.keyName);//传递数据
            if (!TextUtils.isEmpty(cItem.parent_keyName)) {
                intent.putExtra(ItemsDetailActivity.KEY_ITEMS_DETAIL_PARENT_KEY_NAME,cItem.parent_keyName);//合成名称
            }
        }else if (parent instanceof HeroImagesAdapter){
            final HeroItem cItem = (HeroItem)parent.getItem(position);
            intent = new Intent(mcontext,HeroDetailActivity.class);//跳转英雄界面
            intent.putExtra("heroitem",cItem.keyName);//传递数据
        }
        mcontext.startActivity(intent);//启动新的活动
    }
}
