package com.school.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaohao on 17-11-7.
 */

public class ConversationRecording  extends DataSupport{

    private String localUser;
    private String chatUser;
    private String Date;
    private String header;
    private String nickname; //对方的nickname
    private String last;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocalUser() {
        return localUser;
    }

    public void setLocalUser(String localUser) {
        this.localUser = localUser;
    }

    public String getChatUser() {
        return chatUser;
    }

    public void setChatUser(String chatUser) {
        this.chatUser = chatUser;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    @Override
    public String toString() {
        return "ConversationRecording{" +
                "localUser='" + localUser + '\'' +
                ", chatUser='" + chatUser + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
