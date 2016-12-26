package com.xiaoyongit.settingview.entity;

import android.widget.FrameLayout;


/**
 * ItemView数据
 */
public class SettingViewItem {

	private int id;
	private FrameLayout itemView;
	private SettingModel data;
	private boolean isClickable = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FrameLayout getItemView() {
		return itemView;
	}

	public void setItemView(FrameLayout itemView) {
		this.itemView = itemView;
	}

	public SettingModel getData() {
		return data;
	}

	public void setData(SettingModel data) {
		this.data = data;
	}

	public boolean isClickable() {
		return isClickable;
	}

	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
}
