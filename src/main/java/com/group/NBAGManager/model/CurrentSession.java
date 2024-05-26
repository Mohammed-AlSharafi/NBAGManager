package com.group.NBAGManager.model;

public class CurrentSession {
    private static CurrentSession instance = null;
    private User loggedInUser;

    private CurrentSession() {
    }

    public static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
