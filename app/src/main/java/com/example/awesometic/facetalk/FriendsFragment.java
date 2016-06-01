package com.example.awesometic.facetalk;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesometic on 2016-05-29.
 */
public class FriendsFragment extends Fragment {

    final static String LogTag = "Awe_FriendFragment";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(getActivity(), getActivity());

    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    public FriendsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            JSONArray jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());
            List<String> list_friends = new ArrayList<>();
            for (int i = 0; i < jsonArr_friends.length(); i++) {
                list_friends.add(jsonArr_friends.getJSONObject(i).getString("nickname"));
            }

            String[] friendList = list_friends.toArray(new String[list_friends.size()]);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        

        View rootView = inflater.inflate(R.layout.fragment_friends_management, container, false);

        return rootView;
    }
}
