package com.iyuba.core.manager;

public class LogoutEvent {
    public int getUid() {
        return uid;
    }

    int uid ;

    public LogoutEvent(int uid) {
        this.uid = uid;
    }
}
