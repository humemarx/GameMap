package com.hume.gamemap.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 推荐使用英雄 Adapter
 * 
 * @author tcp
 */
public final class HeroImagesAdapter extends BaseAdapter {
    private final class ViewHolder {
        public TextView text;
        public ImageView image;
    }
    private Context mContext = null;
    private final LayoutInflater mInflater;
    private final List<HeroItem> mComponents;

    public HeroImagesAdapter(Context context, List<HeroItem> items) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mComponents = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mComponents.size();
    }
    @Override
    public Object getItem(int position) {
        return mComponents.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.grid_hero_detail_view, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.text_hero_name);
            holder.image = (ImageView) view.findViewById(R.id.image_hero);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final HeroItem item = (HeroItem) getItem(position);
        Bitmap bitmap = getImageFromAssetsFile(Utils.getHeroImageUri(item.keyName));//获取头像
        holder.image.setImageBitmap(bitmap);
        holder.text.setText(item.name_l);
        return view;
    }

    /**
     * 获取文件图像
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = mContext.getResources().getAssets();
        try{
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
