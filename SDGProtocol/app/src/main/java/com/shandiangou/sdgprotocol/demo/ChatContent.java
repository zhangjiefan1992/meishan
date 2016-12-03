package com.shandiangou.sdgprotocol.demo;

/**
 * Created by linwb on 16/11/15.
 */
public class ChatContent {
    public static final int MYSELF = 0;
    public static final int GUEST = 1;

    public ChatContent(int chatType, String message) {
        this.chatContent = message;
        this.chatType = chatType;
    }

    private int chatType;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String chatContent;

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }
}
