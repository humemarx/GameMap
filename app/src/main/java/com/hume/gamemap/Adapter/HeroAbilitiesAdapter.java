package com.hume.gamemap.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by tcp on 2015/1/26.
 */
public class HeroAbilitiesAdapter extends BaseAdapter {
    private final class ViewHolder {
        public ImageView image;
        public TextView dname;
        public TextView affects;
        public TextView attrib;
        public TextView desc;
        public TextView cmb;
        public TextView dmg;
        public TextView notes;
        public TextView lore;
        public ImageView image_skill;
        public TextView text_skill;
        public VideoView video_skill;
    }
    private Context mcontext;
    private HeroItem herolist;
    private final LayoutInflater mInflater;
    private final List<AbilityItem> mAbilities;
    public HeroAbilitiesAdapter(Context context,List<AbilityItem> abilities,HeroItem herolist) {
        super();
        mcontext = context;
        this.herolist = herolist;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAbilities = abilities;
    }

    @Override
    public int getCount() {
        return mAbilities.size();
    }
    @Override
    public Object getItem(int position) {
        return mAbilities.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.fragment_herodetail_abilities_list_item,parent, false);

            holder = new ViewHolder();
            holder.affects = Utils.findById(view, R.id.text_abilities_affects);
            holder.attrib = Utils.findById(view, R.id.text_abilities_attrib);
            holder.dname = Utils.findById(view, R.id.text_abilities_dname);
            holder.cmb = Utils.findById(view, R.id.text_abilities_cmb);
            holder.desc = Utils.findById(view, R.id.text_abilities_desc);
            holder.dmg = Utils.findById(view, R.id.text_abilities_dmg);
            holder.notes = Utils.findById(view, R.id.text_abilities_notes);
            holder.lore = Utils.findById(view, R.id.text_abilities_lore);
            holder.image = Utils.findById(view, R.id.image_abilities);

            holder.image_skill = Utils.findById(view,R.id.image_skill_video);
            holder.video_skill = Utils.findById(view,R.id.skill_video);
            holder.text_skill = Utils.findById(view,R.id.text_skill_video);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        final AbilityItem item = (AbilityItem) getItem(position);
        Bitmap bitmap = getImageFromAssetsFile(Utils.getAbilitiesImageUri(item.keyName));
        holder.image.setImageBitmap(bitmap);
        holder.dname.setText(item.dname);
        Utils.bindHtmlTextView(holder.affects, item.affects);
        Utils.bindHtmlTextView(holder.attrib, item.attrib);
        Utils.bindHtmlTextView(holder.cmb, item.cmb, mImageGetter);
        Utils.bindHtmlTextView(holder.dmg, item.dmg);
        Utils.bindHtmlTextView(holder.desc, item.desc);
        Utils.bindHtmlTextView(holder.notes, item.notes);
        Utils.bindHtmlTextView(holder.lore, item.lore);
        holder.image_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String skill_video = herolist.skill_video[position];
                if(skill_video.indexOf("http")!=-1){
                    holder.video_skill.setMediaController(new MediaController(mcontext));
                    holder.video_skill.setVideoURI(Uri.parse(skill_video));
                    holder.image_skill.setVisibility(View.GONE);//设置消失
                    holder.video_skill.setVisibility(View.VISIBLE);//设置可见
                    holder.video_skill.start();
                    Log.v("skill", skill_video);
                    holder.video_skill.requestFocus();
                }else{
                    Toast toast = Toast.makeText(mcontext.getApplicationContext(), "暂无视频", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        holder.text_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.video_skill.setVisibility(View.GONE);
                holder.image_skill.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    /**
     *
     */
    private final Html.ImageGetter mImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            final Resources res = mcontext.getResources();
            Drawable drawable = null;
            if (source.equals("mana"))
                drawable = res.getDrawable(R.drawable.mana);
            else if (source.equals("cooldown"))
                drawable = res.getDrawable(R.drawable.cooldown);

            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
                return drawable;
            } else {
                return null;
            }
        }
    };
    /**
     * 读取图像文件
     * @param fileName
     * @return
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
