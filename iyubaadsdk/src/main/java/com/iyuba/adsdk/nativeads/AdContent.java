package com.iyuba.adsdk.nativeads;


public class AdContent {
	
	public String titleContent;
	public String textContent;
	public String mainImageUrl;
	public String iconImageUrl;
	public String actionUrl;
	
	private AdContent(Builder builder){
		this.titleContent = builder.titleContent;
		this.textContent = builder.textContent;
		this.mainImageUrl = builder.mainImageUrl;
		this.iconImageUrl = builder.iconImageUrl;
		this.actionUrl = builder.actionUrl;
	}
	
	public static final class Builder {
		private String titleContent;
		private String textContent;
		private String mainImageUrl;
		private String iconImageUrl;
		private String actionUrl;
		
		public final Builder titleContent(String title){
			this.titleContent = title;
			return this;
		}
		
		public final Builder textContent(String text){
			this.textContent = text;
			return this;
		}
		
		public final Builder mainImageUrl(String url){
			this.mainImageUrl = url;
			return this;
		}
		
		public final Builder iconImageUrl(String url){
			this.iconImageUrl = url;
			return this;
		}
		
		public final Builder actionUrl(String url){
			this.actionUrl = url;
			return this;
		}
		
		public final AdContent build() {
			return new AdContent(this);
		}
	}

}
