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
 * 物品 Adapter
 * 
 * @author tupunco
 */
public final class ItemsImagesAdapter extends BaseAdapter {
    private final class ViewHolder {
        public TextView name;
        public TextView cost;
        public ImageView image;
    }

    private Context mContext = null;
    private final LayoutInflater mInflater;
    private final List<ItemsItem> mComponents;

    public ItemsImagesAdapter(Context context, List<ItemsItem> items) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mComponents = items;
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
            view = mInflater.inflate(R.layout.grid_item_detail_view,parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.text_items_name);
            holder.cost = (TextView) view.findViewById(R.id.text_items_cost);
            holder.image = (ImageView) view.findViewById(R.id.image_items);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (holder != null) {
            final ItemsItem item = (ItemsItem) getItem(position);
            Bitmap bitmap = getImageFromAssetsFile(Utils.getItemsImageUri(item.keyName));//获取头像
            holder.image.setImageBitmap(bitmap);
            holder.name.setText(item.dname_l);
            holder.cost.setText(String.valueOf(item.cost));
        }
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
