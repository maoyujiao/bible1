package com.iyuba.core.discover.sqlite.mode;

public class FreshContent {

    public String id = "";
    public String uid = "";
    public String body = "";
    public String feedid = "";
    public String title = "";
    public String username = "";
    public String idtype = "";
    public String image = "";
    public String hot = "";
    public String longitude = "";
    public String latitude = "";
    public String dateline = "";

    public FreshContent(String id, String uid, String body, String feedid,
                        String title, String username, String idtype, String image,
                        String hot, String longitude, String latitude, String dateline) {
        super();
        this.id = id;
        this.uid = uid;
        this.body = body;
        this.feedid = feedid;
        this.title = title;
        this.username = username;
        this.idtype = idtype;
        this.image = image;
        this.hot = hot;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateline = dateline;
    }

    public FreshContent() {
        super();
    }


}
