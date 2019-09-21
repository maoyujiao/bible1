package com.iyuba.adsdk.nativeads;

import java.util.HashMap;
import java.util.Map;

public final class ViewBinder {

	final int layoutId;
	public final int titleId;
	public final int textId;
	public final int callToActionId;
	public final int mainImageId;
	public final int iconImageId;
	public final Map<String, Integer> extras;

	private ViewBinder(Builder builder) {
		this.layoutId = builder.layoutId;
		this.titleId = builder.titleId;
		this.textId = builder.textId;
		this.callToActionId = builder.callToActionId;
		this.mainImageId = builder.mainImageId;
		this.iconImageId = builder.iconImageId;
		this.extras = builder.extras;
	}

	public static final class Builder {
		private final int layoutId;
		private int titleId;
		private int textId;
		private int callToActionId;
		private int mainImageId;
		private int iconImageId;
		private Map<String, Integer> extras;

		public Builder(int layoutId) {
			this.layoutId = layoutId;
			this.extras = new HashMap();
		}

		public final Builder titleId(int titleId) {
			this.titleId = titleId;
			return this;
		}

		public final Builder textId(int textId) {
			this.textId = textId;
			return this;
		}

		public final Builder callToActionId(int callToActionId) {
			this.callToActionId = callToActionId;
			return this;
		}

		public final Builder mainImageId(int paramInt) {
			this.mainImageId = paramInt;
			return this;
		}

		public final Builder iconImageId(int paramInt) {
			this.iconImageId = paramInt;
			return this;
		}

		public final Builder addExtras(Map<String, Integer> paramMap) {
			this.extras = new HashMap(paramMap);
			return this;
		}

		public final Builder addExtra(String paramString, int paramInt) {
			this.extras.put(paramString, Integer.valueOf(paramInt));
			return this;
		}

		public final ViewBinder build() {
			return new ViewBinder(this);
		}
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.nativeads.ViewBinder JD-Core Version: 0.6.2
 */