package com.example.awesometic.facetalk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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

    public int addUser(String email, String password, String nickname, int age, String gender) {
        try {
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("callSign", "addUser");
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("nickname", nickname);
            jsonObject.put("age", String.valueOf(age));
            jsonObject.put("gender", gender);
            array.put(jsonObject);

            StringBuilder result = new PostClass(jsonObject).execute().get();
            int resultCode = Integer.parseInt(result.toString());

            return resultCode;

        } catch (JSONException e) {
            e.printStackTrace();
            return wrongCode;
        } catch (Exception e) {
            e.printStackTrace();
            return wrongCode;
        }
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
            jsonObject.put("callSign", "loginValidation");
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            array.put(jsonObject);

            StringBuilder result = new PostClass(jsonObject).execute().get();
            int useridx = Integer.parseInt(result.toString());

            return useridx;

        } catch (JSONException e) {
            e.printStackTrace();
            return wrongCode;
        } catch (Exception e) {
            e.printStackTrace();
            return wrongCode;
        }
    }

    // http://derveljunit.tistory.com/71
    // http://arabiannight.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CAndroid-AsyncTask-%EC%82%AC%EC%9A%A9%EB%B2%95
    private class PostClass extends AsyncTask<String, Void, StringBuilder> {
        StringBuilder responseOutput = new StringBuilder();

        JSONObject send_JSONObject;

        public PostClass(JSONObject jsonObj) {
            send_JSONObject = jsonObj;
        }

        @Override
        protected StringBuilder doInBackground(String... params) {
            //http://egloos.zum.com/javalove/v/147447
            try {
                URL url = new URL("http://219.240.6.172:50038/android/app_dbconn.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setDoOutput(true);

                //Data to post - replace values from textView
                String urlParameters = "json=" + URLEncoder.encode(send_JSONObject.toString(), "UTF-8");
                Log.d(LogTag, urlParameters);

                //Post
                DataOutputStream dStream =
                        new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                int responseCode = connection.getResponseCode();
                Log.d(LogTag, "responseCode: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Accept response
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line = "";

                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();

                    Log.d(LogTag, "responseOutput: " + responseOutput.toString());
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, responseOutput.toString(),Toast.LENGTH_SHORT).show();
//                    }
//                });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                return null;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }

            return responseOutput;
        }

        protected void onPostExecute() {
            //nothing to place here...
        }
    }
}