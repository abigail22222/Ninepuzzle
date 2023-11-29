package csu.gui;

import java.io.Serial;
import java.io.Serializable;

public class Gameinfo implements Serializable {

    private static final long serialVersionUID = -4499197583888430243L;
    private long finalScore;
    private User user;

    public Gameinfo() {
    }

    public Gameinfo(long finalScore, User user) {
        this.finalScore = finalScore;
        this.user = user;
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
         return "username="+user.getUsername()+"&password="+user.getPassword()+"&time="+finalScore;
    }
}
