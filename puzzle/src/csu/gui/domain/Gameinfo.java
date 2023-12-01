package csu.gui.domain;

import csu.gui.domain.User;

import java.io.Serializable;

public class Gameinfo implements Serializable {

    private static final long serialVersionUID = -4499197583888430243L;
    private long finalScore;
    private User user;
    private long finaltime;

    public long getFinaltime() {
        return finaltime;
    }

    public void setFinaltime(long finaltime) {
        this.finaltime = finaltime;
    }

    public Gameinfo() {
    }

    public Gameinfo(long finalScore, User user,long finaltime) {
        this.finalScore = finalScore;
        this.user = user;
        this.finaltime=finaltime;
    }

    public long getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(long elapsedTimeInSeconds) {
        this.finalScore = elapsedTimeInSeconds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString()
    {
         return "username="+user.getUsername()+"&password="+user.getPassword()+"&time="+finaltime+"&score="+finalScore;
    }
}
