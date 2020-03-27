package com.remote_teacher.deneme002;

import java.util.ArrayList;

/**
 * Created by mpekmezci on 25.05.2015.
 */
public class SessionList {
    private ArrayList<Session> sessionList=new ArrayList<Session>();
    private Session selectedSession=null;

    public void addSession(Session session){
        sessionList.add(session);
    }

    public String[] getAsStringList(){
        String[] sessionStringList=new String [sessionList.size()];
        for(int i=0;i<sessionStringList.length;i++){
            sessionStringList[i]=sessionList.get(i).getHour();
        }
        return sessionStringList;
    }

    public void select(String hour){
        for (int i=0;i<sessionList.size();i++){
            if(sessionList.get(i).getHour().equals(hour)){
                selectedSession=sessionList.get(i);
                break;
            }
        }
    }

    public Session getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(Session selectedSession) {
        this.selectedSession = selectedSession;
    }

    public int size(){
        return sessionList.size();
    }
}
