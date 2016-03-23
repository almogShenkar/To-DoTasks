package com.almog;

/**
 * Created by almog on 3/8/16.
 */
public class User {
    private String id;
    private String username;
    private String phoneUser;
    private String mailUser;

    public User() {}

    public User(String id,String username, String phoneUser, String mailUser) {
        this.id=id;
        this.username = username;
        this.phoneUser = phoneUser;
        this.mailUser = mailUser;
    }

    public String getIdUser() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }
}
