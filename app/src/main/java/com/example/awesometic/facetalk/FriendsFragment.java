package com.example.awesometic.facetalk;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesometic on 2016-05-29.
 * http://recipes4dev.tistory.com/43
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
        View rootView = inflater.inflate(R.layout.fragment_friends_management, container, false);

        ListView lvFriends;
        FriendsListViewAdapter adapter = new FriendsListViewAdapter();
        lvFriends = (ListView) rootView.findViewById(R.id.friend_manager_list);
        lvFriends.setAdapter(adapter);

        try {
            JSONArray jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());

            List<String> list_friends = new ArrayList<>();
            for (int i = 0; i < jsonArr_friends.length(); i++) {
                if (jsonArr_friends.getJSONObject(i).getString("gender").equals("male"))
                    adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.facebook_default_profile_pic_male),
                            jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                else
                    adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.facebook_default_profile_pic_female),
                            jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
            }

            return rootView;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
