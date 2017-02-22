package com.haodong.model;

import java.util.Date;

/**
 * Created by h on 17-2-21.
 */
public class LoginTicket {
    private int id;
    private int UserId;
    private Date expired;
    private int status;
    private String ticket;

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", UserId=" + UserId +
                ", expired=" + expired +
                ", status=" + status +
                ", ticket='" + ticket + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
