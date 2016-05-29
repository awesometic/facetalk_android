package com.example.awesometic.facetalk;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final static String LogTag = "Awe_MainActivity";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(MainActivity.this, MainActivity.this);

    /* Drawer Layout */
    private String[] mFriendsTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFriendsTitles = getFriendList(single.getCurrentUserIdx());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mFriendsTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        /* The code getting friend list from the server
        JSONArray tempFriends = dbConn.getFriend(single.getCurrentUserIdx());
        int tempFriendCount = dbConn.getFriendCount(single.getCurrentUserIdx());
        try {
            Log.d(LogTag, "Friends: " + tempFriends.getJSONObject(0).getString("nickname") + "\nFriendCount: " + tempFriendCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mFriendsTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private String[] getFriendList(int useridx) {
        try {
            JSONArray jsonArr_friends = dbConn.getFriend(useridx);

            List<String> list_friends = new ArrayList<String>();
            for (int i = 0; i < jsonArr_friends.length(); i++) {
                list_friends.add(jsonArr_friends.getJSONObject(i).getString("nickname"));
            }

            return list_friends.toArray(new String[list_friends.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
