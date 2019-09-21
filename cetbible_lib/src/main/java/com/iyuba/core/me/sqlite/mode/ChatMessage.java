package com.iyuba.core.me.sqlite.mode;

/**
 * 私信 chatadapter用
 *
 * @author 陈彤
 */
public class ChatMessage {

    public static final int MESSAGE_FROM = 0;
    public static final int MESSAGE_TO = 1;

    private int direction;
    private String content;

    public ChatMessage(int direction, String content) {
        super();
        this.direction = direction;
        this.content = content;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public CharSequence getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
