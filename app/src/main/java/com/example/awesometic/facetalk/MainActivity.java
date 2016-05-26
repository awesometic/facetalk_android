package com.example.awesometic.facetalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final static String LogTag = "Awe_MainActivity";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(MainActivity.this, MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONArray tempFriends = dbConn.getFriend(single.getCurrentUserIdx());
        int tempFriendCount = dbConn.getFriendCount(single.getCurrentUserIdx());
        try {
            Log.d(LogTag, "Friends: " + tempFriends.getJSONObject(0).getString("nickname") + "\nFriendCount: " + tempFriendCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
