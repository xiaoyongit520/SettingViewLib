package com.xiaoyongit.settingview.item;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyongit.settingview.R;
import com.xiaoyongit.settingview.entity.SettingModel;

public class BasicItemViewH extends FrameLayout {
	private LayoutInflater mInflater = null;

	private LinearLayout mItemViewContainer = null;

	private ImageView mArrow = null;
	private ImageView mDrawable = null;

	private TextView mTitle = null;
	private TextView mSubTitle = null;

	private View mItemView = null;

	public BasicItemViewH(Context context) {
		super(context);
		init(context);
	}

	public BasicItemViewH(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		readAttrs(context, attrs);
	}

	/**
	 * 初始化控件
     */
	private void init(Context context) {
		mInflater = LayoutInflater.from(context);
		mItemView = mInflater.inflate(R.layout.setting_view_basic_item_h, null);
		addView(mItemView);
		findView();
	}

	/**
	 * 查找控件
	 */
	private void findView() {
		mTitle = (TextView) mItemView.findViewById(R.id.setting_view_basic_item_h_title);
		mSubTitle = (TextView) mItemView.findViewById(R.id.setting_view_basic_item_h_subtitle);
		mDrawable = (ImageView) mItemView.findViewById(R.id.setting_view_basic_item_h_icon);
		mArrow = (ImageView) mItemView.findViewById(R.id.setting_view_basic_item_h_arrow);
		mItemViewContainer = (LinearLayout) mItemView.findViewById(R.id.setting_view_basic_item_h_container);
	}

	/**
	 * 读取自定义属性
     */
	private void readAttrs(Context context, AttributeSet attrs) {
		if (null != attrs) {
			//资源的属性列表
			TypedArray attrAll = context.obtainStyledAttributes(attrs, R.styleable.SettingViewItem);

			//右边的箭头
			if (attrAll.hasValue(R.styleable.SettingViewItem_arrow)) {
				Drawable drawable = attrAll.getDrawable(R.styleable.SettingViewItem_arrow);
				if (null != drawable) {
					mArrow.setImageDrawable(drawable);
				} else {
					mArrow.setImageResource(R.drawable.setting_view_arrow_01);
				}
			}

			//背景
			if (attrAll.hasValue(R.styleable.SettingViewItem_background)) {
				Drawable drawable = attrAll.getDrawable(R.styleable.SettingViewItem_background);
				if (null != drawable) {
					mItemViewContainer.setBackgroundDrawable(drawable);
				} else {
					mItemViewContainer.setBackgroundResource(R.drawable.setting_view_item_selector);
				}
			}

			//Ico图标
			if (attrAll.hasValue(R.styleable.SettingViewItem_drawable)) {
				Drawable drawable = attrAll.getDrawable(R.styleable.SettingViewItem_drawable);
				if (null != drawable) {
					mDrawable.setImageDrawable(drawable);
				} else {
					mDrawable.setVisibility(View.GONE);
				}
			} else {
				mDrawable.setVisibility(View.GONE);
			}

			//副标题
			if (attrAll.hasValue(R.styleable.SettingViewItem_subTitle)) {
				String subTitle = attrAll.getString(R.styleable.SettingViewItem_subTitle);
				if (!TextUtils.isEmpty(subTitle)) {
					mSubTitle.setText(subTitle);
				}
			}

			//副标题字体颜色
			if (attrAll.hasValue(R.styleable.SettingViewItem_subTitleColor)) {
				ColorStateList colors = attrAll.getColorStateList(R.styleable.SettingViewItem_subTitleColor);
				if (null != colors) {
					mSubTitle.setTextColor(colors);
				}
			}
			//副标题字体大小
			if (attrAll.hasValue(R.styleable.SettingViewItem_subTitleSize)) {
				int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, attrAll.getDimensionPixelSize(R.styleable.SettingViewItem_subTitleSize, 14), getResources().getDisplayMetrics());
				mSubTitle.setTextSize(textSize);
			}

			//主标题
			if (attrAll.hasValue(R.styleable.SettingViewItem_title)) {
				String title = attrAll.getString(R.styleable.SettingViewItem_title);
				if (!TextUtils.isEmpty(title)) {
					mTitle.setText(title);
				}
			}

			//主标题字体颜色
			if (attrAll.hasValue(R.styleable.SettingViewItem_titleColor)) {
				ColorStateList colors = attrAll.getColorStateList(R.styleable.SettingViewItem_titleColor);
				if (null != colors) {
					mTitle.setTextColor(colors);
				}
			}

			//主标题字体大小
			if (attrAll.hasValue(R.styleable.SettingViewItem_titleSize)) {
				int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, attrAll.getDimensionPixelSize(R.styleable.SettingViewItem_titleSize, 16), getResources().getDisplayMetrics());
				mTitle.setTextSize(textSize);
			}

			//是否可选中
			if (attrAll.hasValue(R.styleable.SettingViewItem_clickable)) {
				mItemViewContainer.setClickable(attrAll.getBoolean(R.styleable.SettingViewItem_clickable, true));
			} else {
				mItemViewContainer.setClickable(true);
			}
			attrAll.recycle();
		}
	}

	public void dataToStyle(SettingModel data) {
		if (null != data) {
			if (!TextUtils.isEmpty(data.getTitle())) {
				mTitle.setText(data.getTitle());
			} else {
				mTitle.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(data.getSubTitle())) {
				mSubTitle.setText(data.getSubTitle());
			} else {
				mSubTitle.setVisibility(View.GONE);
			}

			if (null != data.getDrawable()) {
				mDrawable.setImageDrawable(data.getDrawable());
			} else {
				mDrawable.setVisibility(View.GONE);
			}

			if (null != data.getArrow()) {
				mArrow.setImageDrawable(data.getArrow());
			} else {
				mArrow.setImageResource(R.drawable.setting_view_arrow_01);
			}

			if (null != data.getBackground()) {
				mItemViewContainer.setBackgroundDrawable(data.getBackground());
			} else {
				mItemViewContainer.setBackgroundResource(R.drawable.setting_view_item_selector);
			}

			if (data.getTitleColor() > 0) {
				mTitle.setTextColor(data.getTitleColor());
			}

			if (data.getSubTitleColor() > 0) {
				mSubTitle.setTextColor(data.getSubTitleColor());
			}

			if (data.getTitleSize() > 0) {
				int titleSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, data.getTitleSize(), getResources().getDisplayMetrics());
				mTitle.setTextSize(titleSize);
			}

			if (data.getSubTitleSize() > 0) {
				int subTitleSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, data.getSubTitleSize(), getResources().getDisplayMetrics());
				mSubTitle.setTextSize(subTitleSize);
			}
		}
	}

	/**
	 * 获取Icon资源
     */
	public ImageView getIconDrawable() {
		return mDrawable;
	}

	/**
	 * 获取主标题TextView
     */
	public TextView getTitle() {
		return mTitle;
	}

	/**
	 * 获取副标题TextView
     */
	public TextView getSubTitle() {
		return mSubTitle;
	}
}
