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

import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tcp on 2015/1/26.
 */
public class HeroSkillupAbilityKeysAdapter extends BaseAdapter {private final class ViewHolder {
    public ImageView image;
}

    private final LayoutInflater mInflater;
    private final String[] mAbilities;
    private Context mcontext;
    public HeroSkillupAbilityKeysAdapter(Context context, String[] abilities) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAbilities = abilities;
        mcontext = context;
    }

    @Override
    public int getCount() {
        return mAbilities.length;
    }
    @Override
    public Object getItem(int position) {
        return mAbilities[position];
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
            view = mInflater.inflate(R.layout.fragment_herodetail_skillup_ability_item,parent, false);
            holder = new ViewHolder();
            holder.image = Utils.findById(view, R.id.image_skillup_ability);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        Bitmap bitmap = getImageFromAssetsFile(Utils.getAbilitiesImageUri((String) getItem(position)));
        holder.image.setImageBitmap(bitmap);
        return view;
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
