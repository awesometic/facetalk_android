package com.example.awesometic.facetalk;

/**
 * Created by Awesometic on 2016-05-22.
 *
 * http://zenithmrp.blogspot.com/2014/04/java_26.html
 */
public class Singleton {
    private volatile static Singleton uniqueInstance;

    private String DBName;
    private int DBVersion;
    private int currentUserIdx;
    private String currentUserEmail;
    private String currentUserNickname;

    private Singleton() {
        DBName = "FaceTalk.db";
        DBVersion = 1;
        currentUserIdx = 0;
        currentUserEmail = "";
        currentUserNickname = "";
    }

    public static Singleton getInstance(){
        if(uniqueInstance == null){
            synchronized(Singleton.class){
                if(uniqueInstance == null){
                    uniqueInstance = new Singleton();
                }
            }
        }

        return uniqueInstance;
    }

    protected String getDBName() {
        return DBName;
    }

    protected int getDBVersion() {
        return DBVersion;
    }

    protected int getCurrentUserIdx() {
        return currentUserIdx;
    }

    protected String getCurrentUserEmail() {
        return currentUserEmail;
    }

    protected String getCurrentUserNickname() {
        return currentUserNickname;
    }

    protected void setDBName(String newDBName) {
        DBName = newDBName;
    }

    protected void setDBVersion(int newDBVersion) {
        DBVersion = newDBVersion;
    }

    protected void setCurrentUserIdx(int newIdx) {
        currentUserIdx = newIdx;
    }

    protected void setCurrentUserEmail(String newEmail) {
        currentUserEmail = newEmail;
    }

    protected void setCurrentUserNickname(String newNickname) {
        currentUserNickname = newNickname;
    }
}