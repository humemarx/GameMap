package com.hume.gamemap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hume.gamemap.Fragment_View.SimpleGridView;
import com.hume.gamemap.R;
import com.hume.gamemap.Utils;

import java.util.List;

/**
 * Created by tcp on 2015/1/26.
 */
public class HeroSkillupAdapter extends BaseAdapter {
    private final class ViewHolder {
        public TextView groupName;
        public TextView desc;
        public SimpleGridView abilityKeys;
    }

    private final LayoutInflater mInflater;
    private final List<HeroSkillupItem> mAbilities;
    private final Context mContext;

    public HeroSkillupAdapter(Context context, List<HeroSkillupItem> abilities) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAbilities = abilities;
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.fragment_herodetail_skillup_list_item,parent, false);

            holder = new ViewHolder();
            holder.groupName = Utils.findById(view, R.id.text_skillup_groupName);
            holder.desc = Utils.findById(view, R.id.text_skillup_desc);
            holder.abilityKeys = Utils.findById(view, R.id.grid_skillup_abilitys);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        final HeroSkillupItem item = (HeroSkillupItem) getItem(position);
        holder.groupName.setText(item.groupName);
        Utils.bindHtmlTextView(holder.desc, item.desc);
        if (item.abilityKeys != null && holder.abilityKeys != null) {
            holder.abilityKeys.setAdapter(new HeroSkillupAbilityKeysAdapter(mContext, item.abilityKeys));
        }
        return view;
    }
}
