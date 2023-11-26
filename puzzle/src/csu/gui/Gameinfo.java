package csu.gui;

import java.io.Serial;
import java.io.Serializable;

public class Gameinfo implements Serializable {

    private static final long serialVersionUID = -4499197583888430243L;
    private long elapsedTimeInSeconds;
    private User user;

    public Gameinfo() {
    }

    public Gameinfo(long elapsedTimeInSeconds, User user) {
        this.elapsedTimeInSeconds = elapsedTimeInSeconds;
        this.user = user;
    }

    public long getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }

    public void setElapsedTimeInSeconds(long elapsedTimeInSeconds) {
        this.elapsedTimeInSeconds = elapsedTimeInSeconds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString()
    {
         return "username="+user.getUsername()+"&password="+user.getPassword()+"&time="+elapsedTimeInSeconds;
    }
}
