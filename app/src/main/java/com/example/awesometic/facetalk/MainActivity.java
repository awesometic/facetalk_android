package com.example.awesometic.facetalk;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static String LogTag = "Awe_MainActivity";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(MainActivity.this, MainActivity.this);

    /* NavigationView References:
    Android navigation drawer reference project
    http://www.android4devs.com/2015/06/navigation-view-material-design-support.html
    http://www.technotalkative.com/part-4-playing-with-navigationview/
     */

    private DrawerLayout drawer;

    private ChatFragment fragChat;
    private FriendsFragment fragFriends;
    private SetupFragment fragSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fragments
        fragChat = ChatFragment.newInstance();
        fragFriends = FriendsFragment.newInstance();
        fragSetup = SetupFragment.newInstance();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        String[] mDrawerTitles = getFriendList(single.getCurrentUserIdx());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Change nickname and email at navigation header
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView tvNavHeadNick = (TextView) navHeaderView.findViewById(R.id.nav_head_nickname);
        TextView tvNavHeadEmail = (TextView) navHeaderView.findViewById(R.id.nav_head_email);
        tvNavHeadNick.setText(single.getCurrentUserNickname());
        tvNavHeadEmail.setText(single.getCurrentUserEmail());

        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu("Friends: " + dbConn.getFriendCount(single.getCurrentUserIdx()));
        for (int i = 0; i < mDrawerTitles.length; i++) {
            subMenu.add(mDrawerTitles[i]);
        }
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(MainActivity.this, "logout", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("logout", true);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_add_friend:
                Toast.makeText(MainActivity.this, "add friend", Toast.LENGTH_LONG).show();

                getFragmentManager().beginTransaction().detach(fragFriends).attach(fragFriends).commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragFriends)
                        .commit();
                break;
            case R.id.nav_remove_friend:
                Toast.makeText(MainActivity.this, "remove friend", Toast.LENGTH_LONG).show();

                getFragmentManager().beginTransaction().detach(fragFriends).attach(fragFriends).commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragFriends)
                        .commit();
                break;
            case R.id.nav_setup:
                Toast.makeText(MainActivity.this, "setup", Toast.LENGTH_LONG).show();

                getFragmentManager().beginTransaction().detach(fragSetup).attach(fragSetup).commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragSetup)
                        .commit();
                break;
            default:
                Toast.makeText(MainActivity.this, "friend nickname: " + item.getTitle(), Toast.LENGTH_LONG).show();

                getFragmentManager().beginTransaction().detach(fragChat).attach(fragChat).commit();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragChat)
                        .commit();
                break;
        }

        if (id == R.id.nav_home)
            setTitle(R.string.app_name);
        else
            setTitle(item.getTitle());

        drawer.closeDrawers();
        return true;
    }

    private String[] getFriendList(int useridx) {
        try {
            JSONArray jsonArr_friends = dbConn.getFriend(useridx);

            List<String> list_friends = new ArrayList<>();
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
