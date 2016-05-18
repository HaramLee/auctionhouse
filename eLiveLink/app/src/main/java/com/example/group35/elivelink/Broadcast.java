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

    /**
     * Default
     */
    public Broadcast() {

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
}
