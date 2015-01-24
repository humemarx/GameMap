package com.hume.gamemap.Fragment_View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.hume.gamemap.R;

import java.io.InputStream;

public class SuperAwesomeCardFragment extends Fragment {

	private static final String ARG_POSITION = "position";
	private int position;
	private static final int[] drawables= { R.drawable.dota01, R.drawable.lol03};

	public static SuperAwesomeCardFragment newInstance(int position) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream is = getResources().openRawResource(drawables[position]);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);
        fl.setBackgroundDrawable(bd);
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

		TextView v = new TextView(getActivity());
		params.setMargins(margin, margin, margin, margin);
		v.setLayoutParams(params);
		v.setLayoutParams(params);
		v.setGravity(Gravity.BOTTOM);
		v.setText("CARD " + (position + 1));
		fl.addView(v);
		return fl;
	}

	/**
	 * 提供当前Fragment的主色调的Bitmap对象,供Palette解析颜色
	 * 
	 * @return
	 */
	public static Bitmap getBackgroundBitmapPosition(Context mcontext,int selectViewPagerItem) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream is = mcontext.getResources().openRawResource(drawables[selectViewPagerItem]);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
		return bm;
	}

}