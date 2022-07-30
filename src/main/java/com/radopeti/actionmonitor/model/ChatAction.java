package com.radopeti.actionmonitor.model;

import java.time.LocalDateTime;

public class ChatAction {

    private Long id;

    private LocalDateTime time;

    private String action;

    public ChatAction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "ChatAction{" +
                "id=" + id +
                ", localDateTime=" + time +
                ", action='" + action + '\'' +
                '}';
    }
}
