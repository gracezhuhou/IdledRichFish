package com.sufe.idledrichfish;

public class MSG {
    public static final int TYPE_RECEIVED = 0;//消息的类型:接收
    public static final int TYPE_SEND = 1;    //消息的类型:发送

    private String content;//消息的内容
    private int type;      //消息的类型

    public MSG(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
