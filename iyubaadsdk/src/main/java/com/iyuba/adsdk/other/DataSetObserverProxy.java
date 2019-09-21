package com.iyuba.adsdk.other;

import com.iyuba.adsdk.nativeads.IyubaAdAdapter;

import android.database.DataSetObserver;

public class DataSetObserverProxy extends DataSetObserver{
	
	private IyubaAdAdapter mCusAdapter;
	
	public DataSetObserverProxy(IyubaAdAdapter adapter){
		this.mCusAdapter = adapter;
	}
	
	@Override
	public void onChanged() {
		this.mCusAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onInvalidated() {
		this.mCusAdapter.notifyDataSetInvalidated();
	}

}
