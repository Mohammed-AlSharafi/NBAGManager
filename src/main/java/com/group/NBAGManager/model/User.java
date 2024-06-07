package com.group.NBAGManager.model;

import java.time.LocalDateTime;

public class User {
        private int userId;
        private String username;
        private String password;
        private byte[] salt;
        private boolean isFirstLogin;
        private String location;

        //used as an enclosure for user(s) coming from db.users (NOT FOR USER CREATION USE)
        public User(int userId, String username, String password, byte[] salt, boolean isFirsLogin, String location) {
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.salt = salt;
            this.isFirstLogin = isFirsLogin;
            this.location = location;
        }
        // constructor to save new registered users
        public User(String username, String password, byte[] salt, boolean isFirsLogin) {
            this.username = username;
            this.password = password;
            this.salt = salt;
            this.isFirstLogin = isFirsLogin;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public byte[] getSalt() {
            return salt;
        }

        public void setSalt(byte[] salt) {
            this.salt = salt;
        }

        public boolean isFirstLogin() {
            return isFirstLogin;
        }

        public void setIsFirstLogin(boolean firstLogin) {
            isFirstLogin = firstLogin;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userId=" + userId +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", salt='" + salt + '\'' +
                    ", isFirstLogin=" + isFirstLogin +
                    '}';
        }
}
