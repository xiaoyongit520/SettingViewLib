package com.xiaoyongit.settingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyongit.settingview.entity.SettingViewItem;
import com.xiaoyongit.settingview.item.BasicItemViewH;
import com.xiaoyongit.settingview.item.BasicItemViewV;
import com.xiaoyongit.settingview.item.CheckItemViewH;
import com.xiaoyongit.settingview.item.CheckItemViewV;
import com.xiaoyongit.settingview.item.ImageItemView;
import com.xiaoyongit.settingview.item.SwitchItemView;

import java.util.ArrayList;
import java.util.List;


/**
 * 设置界面常用的一种View基于github上的项目改进而来
 * 感谢原作者
 */
public class SettingView extends LinearLayout {

    private Context mContext;
/*    private View mTopDivider;
    private View mBottomDivider;*/
    private TextView mTextViewTopTitle;

    //attrs
    private boolean mIsShowTopTitle;
    private String mTopTitle = "";

    /**
     * 是否IOS样式
     */
    private boolean iOSStyleable = true;
    private List<SettingViewItem> mItemViews = null;

    private onSettingViewItemClickListener mItemClickListener;
    private onSettingViewItemSwitchListener mItemSwitchListener;

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mItemViews = new ArrayList<SettingViewItem>();
        setOrientation(LinearLayout.VERTICAL);
/*
        mTopDivider = new View(context);
        mTopDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.setting_view_item_bg_line));

        mBottomDivider = new View(context);
        mBottomDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.setting_view_item_bg_line));*/
        readAttrs(attrs);
    }

    private void readAttrs(AttributeSet attrs) {
        TypedArray attrArr = mContext.obtainStyledAttributes(attrs, R.styleable.SettingView);

        if (attrArr.hasValue(R.styleable.SettingView_iOSStyle)) {
            iOSStyleable = attrArr.getBoolean(R.styleable.SettingView_iOSStyle, false);
        }
        if (attrArr.hasValue(R.styleable.SettingView_IsShowTopTitle)) {
            mIsShowTopTitle = attrArr.getBoolean(R.styleable.SettingView_IsShowTopTitle, true);
        }
        if (attrArr.hasValue(R.styleable.SettingView_TopTitle)) {
            mTopTitle = attrArr.getString(R.styleable.SettingView_TopTitle);
        }

        attrArr.recycle();
    }

    public void setAdapter(List<SettingViewItem> listData) {
        if (!listData.isEmpty()) {
            mItemViews = listData;
            int size = mItemViews.size();

            //是否显示标题
            if (mIsShowTopTitle) {
                this.addTopTitle(mTopTitle);
            } else {
                //添加头部分割线
                this.addDivider(0);
            }

            // Add Content
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    SettingViewItem item = mItemViews.get(i);
                    initItemView(item, i);
                    if (i != (size - 1)) {
                        addDivider(iOSStyleable);
                    }
                }
            }
            // Add Bottom Divider
            this.addDivider(0);
        }
    }

    /**
     * 添加分割线
     */
    private void addDivider(boolean iOS_Stylable) {
        View divider = new View(mContext);
        divider.setBackgroundColor(ContextCompat.getColor(mContext, R.color.setting_view_item_bg_line));
        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        lps.gravity = Gravity.RIGHT;

        if (iOS_Stylable) {
            int paddingLeft = resDip2Px(R.dimen.setting_view_min_height) + resDip2Px(R.dimen.setting_view_lr_padding);
            lps.leftMargin = paddingLeft;
        }
        addView(divider, lps);
    }

    /**
     * 添加分割线 指定左边缩进的方式
     */
    private void addDivider(int leftPadding) {
        View divider = new View(mContext);
        divider.setBackgroundColor(ContextCompat.getColor(mContext, R.color.setting_view_item_bg_line));
        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        lps.gravity = Gravity.RIGHT;
        lps.leftMargin = leftPadding;
        addView(divider, lps);
    }

    private void addTopTitle(String title) {
        //顶部线条
        this.addDivider(false);
        mTextViewTopTitle = new TextView(mContext);
        mTextViewTopTitle.setText(title);
        mTextViewTopTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.setting_view_toptitle_size));
        mTextViewTopTitle.setPadding(resDip2Px(R.dimen.setting_view_lr_padding), 0, 0, 0);
        mTextViewTopTitle.setGravity(Gravity.CENTER_VERTICAL);
        mTextViewTopTitle.setTextColor(ContextCompat.getColor(mContext, R.color.setting_view_item_toptitle_text));
        mTextViewTopTitle.setBackgroundColor(ContextCompat.getColor(mContext, R.color.setting_view_item_bg_normal));
        LayoutParams txtLps = new LayoutParams(LayoutParams.MATCH_PARENT, resDip2Px(R.dimen.setting_view_top_title_height_size));
        addView(mTextViewTopTitle, txtLps);
        //textView底部缩进线
        if(iOSStyleable){
            //ios缩进
            this.addDivider(resDip2Px(R.dimen.setting_view_lr_padding));
        }else {
            //其它不缩进
            this.addDivider(0);
        }
    }

    private int resDip2Px(int resId) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(resId), getResources().getDisplayMetrics());
    }

    /**
     * 初始化Item视图
     */
    private void initItemView(SettingViewItem data, final int index) {
        final FrameLayout itemView = data.getItemView();

        if (itemView instanceof SwitchItemView) {
            final SwitchItemView switchItemView = (SwitchItemView) itemView;
            switchItemView.dataToStyle(data.getData());
            switchItemView.setOnSwitchItemChangedListener(new SwitchItemView.onSwitchItemChangedListener() {

                @Override
                public void onSwitchItemChanged(boolean isChecked) {
                    if (null != mItemSwitchListener) {
                        mItemSwitchListener.onSwitchChanged(index, isChecked);
                    }
                }
            });
            //itemView.setClickable(false);
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mItemSwitchListener) {
                        switchItemView.getSwitchButton().setChecked(!switchItemView.getIsChecked());
                        //mItemClickListener.onItemClick(index);
                    }
                }
            });
        } else {
            itemView.setClickable(data.isClickable());
            itemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mItemClickListener) {
                        mItemClickListener.onItemClick(index);
                    }
                }
            });

            if (itemView instanceof BasicItemViewH) {
                ((BasicItemViewH) itemView).dataToStyle(data.getData());
            } else if (itemView instanceof BasicItemViewV) {
                ((BasicItemViewV) itemView).dataToStyle(data.getData());
            } else if (itemView instanceof ImageItemView) {
                ((ImageItemView) itemView).dataToStyle(data.getData());
            } else if (itemView instanceof CheckItemViewH) {
                ((CheckItemViewH) itemView).dataToStyle(data.getData());
            } else if (itemView instanceof CheckItemViewV) {
                ((CheckItemViewV) itemView).dataToStyle(data.getData());
            }
        }

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.setting_view_min_height), getResources().getDisplayMetrics());
        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, height);

        addView(itemView, lps);
    }

    public interface onSettingViewItemClickListener {
        void onItemClick(int index);
    }

    public void setOnSettingViewItemClickListener(onSettingViewItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface onSettingViewItemSwitchListener {
        void onSwitchChanged(int index, boolean isChecked);
    }

    public void setOnSettingViewItemSwitchListener(onSettingViewItemSwitchListener listener) {
        this.mItemSwitchListener = listener;
    }

    public FrameLayout getItemView(int index) {
        return (FrameLayout) getChildAt(2 * index + 1);
    }

    public void setItemTitle(String title, int index) {
        FrameLayout itemView = getItemView(index);
        if (itemView instanceof SwitchItemView) {
            ((SwitchItemView) itemView).getmTitle().setText(title);
        } else {
            if (itemView instanceof BasicItemViewH) {
                ((BasicItemViewH) itemView).getTitle().setText(title);
            } else if (itemView instanceof BasicItemViewV) {
                ((BasicItemViewV) itemView).getmTitle().setText(title);
            } else if (itemView instanceof ImageItemView) {
                ((ImageItemView) itemView).getmTitle().setText(title);
            } else if (itemView instanceof CheckItemViewH) {
                ((CheckItemViewH) itemView).getmTitle().setText(title);
            } else if (itemView instanceof CheckItemViewV) {
                ((CheckItemViewV) itemView).getmTitle().setText(title);
            }
        }
    }

    public void setItemSubTitle(String subTitle, int index) {
        FrameLayout itemView = getItemView(index);
        if (itemView instanceof BasicItemViewH) {
            ((BasicItemViewH) itemView).getSubTitle().setText(subTitle);
        } else if (itemView instanceof BasicItemViewV) {
            ((BasicItemViewV) itemView).getmSubTitle().setText(subTitle);
        } else if (itemView instanceof CheckItemViewV) {
            ((CheckItemViewV) itemView).getmSubTitle().setText(subTitle);
        }
    }

    public void setItemDrawable(Drawable drawable, int index) {
        FrameLayout itemView = getItemView(index);
        if (itemView instanceof SwitchItemView) {
            ((SwitchItemView) itemView).getmDrawable().setImageDrawable(drawable);
        } else {
            if (itemView instanceof BasicItemViewH) {
                ((BasicItemViewH) itemView).getIconDrawable().setImageDrawable(drawable);
            } else if (itemView instanceof BasicItemViewV) {
                ((BasicItemViewV) itemView).getmDrawable().setImageDrawable(drawable);
            } else if (itemView instanceof ImageItemView) {
                ((ImageItemView) itemView).getmDrawable().setImageDrawable(drawable);
            } else if (itemView instanceof CheckItemViewH) {
                ((CheckItemViewH) itemView).getmDrawable().setImageDrawable(drawable);
            } else if (itemView instanceof CheckItemViewV) {
                ((CheckItemViewV) itemView).getmDrawable().setImageDrawable(drawable);
            }
        }
    }

    public void modifyInfo(Drawable drawable, int index) {
        FrameLayout itemView = getItemView(index);
        if (itemView instanceof ImageItemView) {
            ((ImageItemView) itemView).getmImage().setImageDrawable(drawable);
        }
    }



    public void setTopTitleTextColor(int color) {
        if (mTextViewTopTitle != null) {
            mTextViewTopTitle.setTextColor(color);
        }
    }

    public void setTopTitleTextSize(int size) {
        if(mTextViewTopTitle != null){
            mTextViewTopTitle.setTextSize(size);
        }
    }

    public void setTopTitleText(String title) {
        if(mTextViewTopTitle != null){
            mTextViewTopTitle.setText(title);
        }
    }
}
