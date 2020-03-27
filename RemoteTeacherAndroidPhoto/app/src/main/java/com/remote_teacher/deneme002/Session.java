package com.remote_teacher.deneme002;

/**
 * Created by mpekmezci on 25.05.2015.
 */
public class Session {
    private String hour;
    private String sessionId;
    private String ip;

    public String getHour() {
        return hour;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}
