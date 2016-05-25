package com.example.awesometic.facetalk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Awesometic on 2016-05-24.
 */

public class DBConnect {

    final static String LogTag = "Awe_DBConnect";
    int wrongCode = -9;

    Context context;
    Activity activity;

    public DBConnect(Context _context, Activity _activity) {
        context = _context;
        activity = _activity;
    }

    public boolean addUser(String email, String password, String nickname, int age, String gender) {

        return true;
    }

    public String[] getMessages() {

        return new String[] { };
    }

    public String getFriend() {

        return new String();
    }

    public String[] getAllFriends() {

        return new String[] { };
    }

    public String getNickname() {

        return new String();
    }

    public String[] getAllNicknames() {

        return new String[] { };
    }

    public int loginValidation(String email, String password) {
        try {
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            array.put(jsonObject);
            Log.e("json parser", "=================="+array.toString());

            Log.d(LogTag, "here");
            new PostClass("loginValidation", jsonObject).execute();

            // test
            return 0;

        } catch (JSONException e) {
            e.printStackTrace();
            return wrongCode;
        }
    }

    // http://derveljunit.tistory.com/71
    // http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndroid-AsyncTask-%EC%82%AC%EC%9A%A9%EB%B2%95
    private class PostClass extends AsyncTask<String, Void, Void> {
        StringBuilder responseOutput = new StringBuilder();

        String send_callSign;
        JSONObject send_JSONObject;

        public PostClass(String callSign, JSONObject jsonObj) {
            send_callSign = callSign;
            send_JSONObject = jsonObj;
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                URL url = new URL("http://219.240.6.172:50038/android/app_dbconn.php");
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);

                //Data to post - replace values from textView
                String urlParameters = "callSign=" + send_callSign + "&json=" + send_JSONObject;

                //Post
                DataOutputStream dStream =
                        new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                //Accept response
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = "";

                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                Log.d(LogTag, "responseOutput: " + responseOutput.toString());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, responseOutput.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch (Exception e){ }

            return null;
        }

        protected void onPostExecute() {
            //nothing to place here...
        }
    }
}