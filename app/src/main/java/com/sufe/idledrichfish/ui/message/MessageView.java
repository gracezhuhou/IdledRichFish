package com.sufe.idledrichfish.ui.message;

public class MessageView {

    String name;

    byte[] image;

    String lastMessage;

    String date;

    int status; // 已读 -1 or 未读 - 0


    public MessageView(String name, byte[] image, String lastMessage, String date, int status) {
        this.name = name;
        this.image = image;
        this.lastMessage = lastMessage;
        this.date = date;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
