package com.iyuba.adsdk.nativeads;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.iyuba.adsdk.extra.common.AdWebBrowser;
import com.iyuba.adsdk.other.DataSetObserverProxy;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IyubaAdAdapter extends BaseAdapter {
	private static final String TAG = IyubaAdAdapter.class.getSimpleName();
	private static final String STUB = "AD Place Stub";

	private Context mContext;
	private LayoutInflater mInflater;
	private BaseAdapter mOriginalAdapter;
	private List<Integer> originalPos;
	private Set<Integer> actualPos;
	private SparseArray<Integer> mPositionMap;

	private ViewBinder mViewBinder;
	private AdContent mAdContent;

	private int orginalCount;
	private int totalCount;

	public IyubaAdAdapter(Context context, BaseAdapter adapter, List<Integer> adpos) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mPositionMap = new SparseArray<Integer>();
		originalPos = adpos;
		mOriginalAdapter = adapter;
		initMap();
		mOriginalAdapter.registerDataSetObserver(new DataSetObserverProxy(this));
	}

	public void setViewBinder(ViewBinder viewBinder) {
		this.mViewBinder = viewBinder;
	}

	public void setAdContent(AdContent adContent) {
		this.mAdContent = adContent;
	}

	private Set<Integer> generateLegalPosition(int orginalCount, List<Integer> pos) {
		Set<Integer> legalAd = new HashSet<Integer>();
		Iterator<Integer> it = pos.iterator();
		while (it.hasNext()) {
			int p = it.next();
			if (p < orginalCount && p >= 0) {
				legalAd.add(p);
			}
		}
		return legalAd;
	}

	private void initMap() {
		orginalCount = mOriginalAdapter.getCount();
		actualPos = generateLegalPosition(orginalCount, originalPos);
		totalCount = orginalCount + actualPos.size();
		mPositionMap.clear();
		int i = 0, j = 0;
		while (i < totalCount && j < orginalCount) {
			if (isAd(i)) {
				i++;
			} else {
				mPositionMap.put(i, j);
				i++;
				j++;
			}
		}
	}

	public boolean isAd(int position) {
		return actualPos.contains(position);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return (mOriginalAdapter instanceof BaseAdapter) && mOriginalAdapter.areAllItemsEnabled();
	}

	@Override
	public boolean isEnabled(int position) {
		return isAd(position)
				|| ((mOriginalAdapter instanceof BaseAdapter) && mOriginalAdapter.isEnabled(mPositionMap
						.get(position)));
	}

	@Override
	public int getCount() {
		return totalCount;
	}

	@Override
	public Object getItem(int position) {
		if (isAd(position)) {
			return STUB;
		}
		return mOriginalAdapter.getItem(mPositionMap.get(position));
	}

	@Override
	public long getItemId(int position) {
		if (isAd(position)) {
			return (System.identityHashCode(STUB) ^ 0xFFFFFFFF) + 1;
		}
		return mOriginalAdapter.getItemId(mPositionMap.get(position));
	}

	@Override
	public boolean hasStableIds() {
		return mOriginalAdapter.hasStableIds();
	}

	@Override
	public int getViewTypeCount() {
		return mOriginalAdapter.getViewTypeCount() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		return isAd(position) ? mOriginalAdapter.getViewTypeCount() : mOriginalAdapter
				.getItemViewType(mPositionMap.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (isAd(position)) {
			if (convertView == null) {
				convertView = mInflater.inflate(mViewBinder.layoutId, null);
			}
			if (mViewBinder.titleId != 0 && mAdContent.titleContent != null) {
				TextView title_tv = ViewHolder.get(convertView, mViewBinder.titleId);
				title_tv.setText(mAdContent.titleContent);
			}
			if (mViewBinder.textId != 0 && mAdContent.textContent != null) {
				TextView text_tv = ViewHolder.get(convertView, mViewBinder.textId);
				text_tv.setText(mAdContent.textContent);
			}
			if (mViewBinder.mainImageId != 0) {
				ImageView main_iv = ViewHolder.get(convertView, mViewBinder.mainImageId);
				ImageLoader.getInstance().displayImage(mAdContent.mainImageUrl, main_iv);
			}
			if (mViewBinder.iconImageId != 0) {
				ImageView icon_iv = ViewHolder.get(convertView, mViewBinder.iconImageId);
				ImageLoader.getInstance().displayImage(mAdContent.iconImageUrl, icon_iv);
			}
			if (mAdContent.actionUrl != null) {
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, AdWebBrowser.class);
						intent.putExtra("URL", mAdContent.actionUrl);
						mContext.startActivity(intent);
					}
				});
			}
			return convertView;
		} else {
			return mOriginalAdapter.getView(mPositionMap.get(position), convertView, parent);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		initMap();
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		initMap();
		super.notifyDataSetInvalidated();
	}

}
