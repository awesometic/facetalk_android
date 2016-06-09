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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(MainActivity.this, MainActivity.this);

    /* NavigationView References:
    Android navigation drawer reference project
    http://www.android4devs.com/2015/06/navigation-view-material-design-support.html
    http://www.technotalkative.com/part-4-playing-with-navigationview/
     */

    private DrawerLayout drawer;

    private MainFragment fragMain;
    private FriendsFragment fragFriends;
    private SetupFragment fragSetup;

    private NavigationView navigationView;
    private SubMenu subMenu;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Close when press back button twice
        backPressCloseHandler = new BackPressCloseHandler(this);

        // Fragments
        fragMain = MainFragment.newInstance();
        fragFriends = FriendsFragment.newInstance();
        fragSetup = SetupFragment.newInstance();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavigationView();

        // Change nickname and email at navigation header
        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView tvNavHeadNick = (TextView) navHeaderView.findViewById(R.id.nav_head_nickname);
        TextView tvNavHeadEmail = (TextView) navHeaderView.findViewById(R.id.nav_head_email);
        tvNavHeadNick.setText(single.getCurrentUserNickname());
        tvNavHeadEmail.setText(single.getCurrentUserEmail());

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragMain)
                .detach(fragMain).attach(fragMain)
                .commit();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            backPressCloseHandler.onBackPressed();
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
        } else if (id == R.id.action_exit) {
            moveTaskToBack(true);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragMain)
                        .detach(fragMain).attach(fragMain)
                        .commit();
                break;
            case R.id.nav_add_friend:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragFriends)
                        .detach(fragFriends).attach(fragFriends)
                        .commit();
                break;
            case R.id.nav_remove_friend:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragFriends)
                        .detach(fragFriends).attach(fragFriends)
                        .commit();
                break;
            case R.id.nav_setup:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragSetup)
                        .detach(fragSetup).attach(fragSetup)
                        .commit();
                break;
            default:
                // http://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
                ChatFragment fragChat = ChatFragment.newInstance();
                Bundle bundle = new Bundle();

                int index = -1;
                for (int i = 0; i < subMenu.size(); i++) {
                    if (subMenu.getItem(i).getTitle().toString().contains(item.getTitle().toString())
                            && i != subMenu.size() - 1) {
                        index = Integer.valueOf(subMenu.getItem(i + 1).getTitle().toString().split("::")[1]);
                        break;
                    }
                }
                bundle.putInt("friendidx", index);
                fragChat.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragChat)
                        .detach(fragChat).attach(fragChat)
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
            single.setCurrentFriends(jsonArr_friends);

            List<String> list_friends = new ArrayList<>();
            for (int i = 0; i < jsonArr_friends.length(); i++) {
                JSONObject jsonObj_friend = jsonArr_friends.getJSONObject(i);
                list_friends.add(jsonObj_friend.getString("nickname") + "::" + jsonObj_friend.getString("idx"));
            }

            return list_friends.toArray(new String[list_friends.size()]);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateNavigationView() {
        String[] mDrawerTitles = getFriendList(single.getCurrentUserIdx());

        final Menu menu = navigationView.getMenu();
        menu.clear();

        subMenu = menu.addSubMenu("Friends: " + dbConn.getFriendCount(single.getCurrentUserIdx()));
        for (int i = 0; i < mDrawerTitles.length; i++) {
            subMenu.add(mDrawerTitles[i].split("::")[0]);
            subMenu.add(mDrawerTitles[i]).setVisible(false);
        }

        navigationView.inflateMenu(R.menu.nav_list_item);
    }
}
