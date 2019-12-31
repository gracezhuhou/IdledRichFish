package com.sufe.idledrichfish.ui.chat;

public class ChatView {

    String userId;

    String name;

    byte[] image;

    String lastMessage;

    String date;

    boolean unread;


    public ChatView(String userId, String name, byte[] image, String lastMessage, String date, boolean unread) {
        this.userId = userId;
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.date = date;
        this.unread = unread;
    }

    public ChatView(String userId, String lastMessage, String date, boolean unread) {
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.date = date;
        this.unread = unread;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
