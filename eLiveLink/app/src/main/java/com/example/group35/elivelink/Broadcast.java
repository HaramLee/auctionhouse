package com.example.group35.elivelink;

/**
 * Created by Haram on 5/17/2016.
 */
public class Broadcast {

    private int bcID;
    private String broadcastName;
    private String youtubeVidID;
    private String bio;
    private String schedule;
    private int isBroadcasting;
    private double subscribeCost;

    private int userID;
    private String userName;

    /**
     * Default
     */
    public Broadcast() {

    }

    public void setUserID(int userID) { this.userID = userID; }

    public int getUserID () {return userID;}

    public String getUserName(){ return userName; }

    public void setUserName(String userName){
        if(userName == null) {
            this.userName = " ";
        }else{
            this.userName = userName;
        }
    }


    public int getBcID() {
        return bcID;
    }
    public void setBcID(int bcID) {
        this.bcID = bcID;
    }

    public String getBroadcastName() {
        return broadcastName;
    }

    public void setBroadcastName(String broadcastName) {
        this.broadcastName = broadcastName;
    }

    public String getYoutubeVidID() {
        return youtubeVidID;
    }

    public void setYoutubeVidID(String youtubeVidID) {
        this.youtubeVidID = youtubeVidID;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        if(bio.equals("null")) {
            this.bio = "";
        }else{
            this.bio = bio;
        }
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        if(schedule.equals("null")) {
            this.schedule = "";
        }else{
            this.schedule = schedule;
        }
    }

    public int getIsBroadcasting() {
        return isBroadcasting;
    }

    public void setIsBroadcasting(int isBroadcasting) {
        this.isBroadcasting = isBroadcasting;
    }

    public double getSubscribeCost() {
        return subscribeCost;
    }

    public void setSubscribeCost(double subscribeCost) {
        this.subscribeCost = subscribeCost;
    }
}
