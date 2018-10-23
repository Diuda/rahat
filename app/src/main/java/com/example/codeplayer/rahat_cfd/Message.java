package com.example.codeplayer.rahat_cfd;
public class Message {
    private String text;
    private MemberData data;
    private int belongsToCurrentUser;

    public Message(String text, MemberData data, int belongsToCurrentUser) {
        this.text = text;
        this.data = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }

    public MemberData getData() {
        return data;
    }

    public int isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
}
