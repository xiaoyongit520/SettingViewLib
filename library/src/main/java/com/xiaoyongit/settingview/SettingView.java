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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

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
    private ImageView mTopDivider;
    private ImageView mBottomDivider;

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

        mTopDivider = new ImageView(context);
        mTopDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.setting_view_item_bg_line));
        //mTopDivider.setBackgroundResource(R.drawable.divider);

        mBottomDivider = new ImageView(context);

        mBottomDivider.setBackgroundColor(ContextCompat.getColor(context, R.color.setting_view_item_bg_line));
        //mBottomDivider.setBackgroundResource(R.drawable.divider);

        readAttrs(attrs);
    }

    private void readAttrs(AttributeSet attrs) {
        TypedArray attrArr = mContext.obtainStyledAttributes(attrs, R.styleable.SettingView);

        if (attrArr.hasValue(R.styleable.SettingView_iOSStyle)) {
            iOSStyleable = attrArr.getBoolean(R.styleable.SettingView_iOSStyle, false);
        }

        attrArr.recycle();
    }

    public void setAdapter(List<SettingViewItem> listData) {
        if (!listData.isEmpty()) {
            mItemViews = listData;

            int size = mItemViews.size();

            //顶部和底部线高度
            LayoutParams dividerLps = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
            // Add Top Divider
            addView(mTopDivider, dividerLps);
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
            addView(mBottomDivider, dividerLps);
        }
    }

    /**
     * 添加分割线
     */
    private void addDivider(boolean iOS_Stylable) {
        ImageView divider = new ImageView(mContext);
        divider.setScaleType(ScaleType.FIT_XY);
        divider.setBackgroundColor(ContextCompat.getColor(mContext, R.color.setting_view_item_bg_line));
        //divider.setImageResource(R.drawable.divider);

        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        lps.gravity = Gravity.RIGHT;


        if (iOS_Stylable) {
            int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.setting_view_min_height), getResources().getDisplayMetrics())
                    + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.setting_view_lr_padding), getResources().getDisplayMetrics());
            lps.leftMargin = paddingLeft;
            //divider.setPadding(paddingLeft, 0, 0, 0);
        }

        addView(divider, lps);
    }

    /**
     * 初始化视图
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
        public void onSwitchChanged(int index, boolean isChecked);
    }

    public void setOnSettingViewItemSwitchListener(onSettingViewItemSwitchListener listener) {
        this.mItemSwitchListener = listener;
    }

    public FrameLayout getItemView(int index) {
        return (FrameLayout) getChildAt(2 * index + 1);
    }

    public void modifyTitle(String title, int index) {
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

    public void modifySubTitle(String subTitle, int index) {
        FrameLayout itemView = getItemView(index);
        if (itemView instanceof BasicItemViewH) {
            ((BasicItemViewH) itemView).getSubTitle().setText(subTitle);
        } else if (itemView instanceof BasicItemViewV) {
            ((BasicItemViewV) itemView).getmSubTitle().setText(subTitle);
        } else if (itemView instanceof CheckItemViewV) {
            ((CheckItemViewV) itemView).getmSubTitle().setText(subTitle);
        }
    }

    public void modifyDrawable(Drawable drawable, int index) {
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
}
