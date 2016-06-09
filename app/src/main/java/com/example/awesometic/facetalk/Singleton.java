package com.example.awesometic.facetalk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesometic on 2016-05-22.
 *
 * http://zenithmrp.blogspot.com/2014/04/java_26.html
 */
public class Singleton {
    private volatile static Singleton uniqueInstance;

    private int currentUserIdx;
    private String currentUserEmail;
    private String currentUserNickname;
    private JSONArray currentFriends;

    private Singleton() {
        currentUserIdx = 0;
        currentUserEmail = "";
        currentUserNickname = "";
        currentFriends = null;
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

    protected int getCurrentUserIdx() {
        return currentUserIdx;
    }

    protected String getCurrentUserEmail() {
        return currentUserEmail;
    }

    protected String getCurrentUserNickname() {
        return currentUserNickname;
    }

    protected JSONArray getCurrentFriends() {
        return currentFriends;
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

    protected void setCurrentFriends(JSONArray newFriendsList) {
        currentFriends = newFriendsList;
    }

    /* Other functions */
    public JSONObject getFriendInfo(int friendidx) {
        try {
            JSONObject jsonObj_friendInfo = null;

            for (int i = 0; i < getCurrentFriends().length(); i++) {
                if (Integer.valueOf(getCurrentFriends().getJSONObject(i).getString("idx")) == friendidx) {
                    jsonObj_friendInfo = getCurrentFriends().getJSONObject(i);
                }
            }

            return jsonObj_friendInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}