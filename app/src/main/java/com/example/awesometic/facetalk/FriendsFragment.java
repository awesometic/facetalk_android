package com.example.awesometic.facetalk;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

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

    public FriendsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View rootView = inflater.inflate(R.layout.fragment_friends_management, container, false);

            ListView lvFriends;
            Button btnSubmit;

            FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity());

            lvFriends = (ListView) rootView.findViewById(R.id.friend_manager_list);
            lvFriends.setAdapter(adapter);
            btnSubmit = (Button) rootView.findViewById(R.id.friend_manager_submit);
            btnSubmit.setOnClickListener(mClickListener);

            if (getActivity().getTitle().equals("Add Friends")) {
                btnSubmit.setText("Add");

            } else if (getActivity().getTitle().equals("Remove Friends")) {
                btnSubmit.setText("Remove");

                JSONArray jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());

                for (int i = 0; i < jsonArr_friends.length(); i++) {
                    adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                }
            } else {

            }

            return rootView;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Button clicked: ", Toast.LENGTH_LONG).show();
        }
    };

}
