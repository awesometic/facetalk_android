package com.example.awesometic.facetalk;

import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Awesometic on 2016-05-29.
 * http://recipes4dev.tistory.com/43
 */
public class FriendsFragment extends Fragment {

    final static String LogTag = "Awe_FriendFragment";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(getActivity(), getActivity());

    private ListView lvFriends;
    private Button btnSubmit;
    private EditText etSearchInput;

    private JSONArray jsonArr_friends;

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
            List<FriendsListViewItem> friendsList = new LinkedList<>();

            etSearchInput = (EditText) rootView.findViewById(R.id.friend_manager_input);
            etSearchInput.setOnKeyListener(mKeyUpListener);
            etSearchInput.addTextChangedListener(mTextChangedListener);
            etSearchInput.requestFocus();
            lvFriends = (ListView) rootView.findViewById(R.id.friend_manager_list);
            lvFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            btnSubmit = (Button) rootView.findViewById(R.id.friend_manager_submit);
            btnSubmit.setOnClickListener(mClickListener);
            btnSubmit.setEnabled(true);

            if (getActivity().getTitle().equals("Add Friends")) {
                btnSubmit.setText("Add");
                jsonArr_friends = dbConn.getAllFriends(single.getCurrentUserIdx());

                for (int i = 0; i < jsonArr_friends.length(); i++) {
                    friendsList.add(new FriendsListViewItem(jsonArr_friends.getJSONObject(i).getString("nickname")
                            , jsonArr_friends.getJSONObject(i).getString("email")));
                }

            } else if (getActivity().getTitle().equals("Remove Friends")) {
                btnSubmit.setText("Remove");

                jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());

                for (int i = 0; i < jsonArr_friends.length(); i++) {
                    friendsList.add(new FriendsListViewItem(jsonArr_friends.getJSONObject(i).getString("nickname")
                            , jsonArr_friends.getJSONObject(i).getString("email")));
                }
            }

            FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity(), friendsList);
            lvFriends.setAdapter(adapter);
            return rootView;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateFriends(String currentMode, String searchKeyword) {
        try {

            List<FriendsListViewItem> friendsList = new LinkedList<>();

            if (currentMode.equals("Add Friends")) {
                if (searchKeyword.length() == 0) {
                    jsonArr_friends = dbConn.getAllFriends(single.getCurrentUserIdx());

                    for (int i = 0; i < jsonArr_friends.length(); i++) {
                        friendsList.add(new FriendsListViewItem(jsonArr_friends.getJSONObject(i).getString("nickname")
                                , jsonArr_friends.getJSONObject(i).getString("email")));
                    }
                } else if (searchKeyword.length() != 0) {
                    jsonArr_friends = dbConn.getSearchedFriends_add(single.getCurrentUserIdx(), searchKeyword);

                    for (int i = 0; i < jsonArr_friends.length(); i++) {
                        friendsList.add(new FriendsListViewItem(jsonArr_friends.getJSONObject(i).getString("nickname")
                                , jsonArr_friends.getJSONObject(i).getString("email")));
                    }
                }
            } else if (currentMode.equals("Remove Friends")) {
                if (searchKeyword.length() == 0) {
                    jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());

                    for (int i = 0; i < jsonArr_friends.length(); i++) {
                        friendsList.add(new FriendsListViewItem(jsonArr_friends.getJSONObject(i).getString("nickname")
                                , jsonArr_friends.getJSONObject(i).getString("email")));
                    }
                } else if (searchKeyword.length() != 0) {
                    jsonArr_friends = dbConn.getSearchedFriends_remove(single.getCurrentUserIdx(), searchKeyword);

                    for (int i = 0; i < jsonArr_friends.length(); i++) {
                        friendsList.add(new FriendsListViewItem(jsonArr_friends.getJSONObject(i).getString("nickname")
                                , jsonArr_friends.getJSONObject(i).getString("email")));
                    }
                }
            }

            FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity(), friendsList);
            lvFriends.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // http://stackoverflow.com/questions/10865645/delete-key-is-not-working
    EditText.OnKeyListener mKeyUpListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                return true;
            else
                return false;
        }
    };

    // http://stackoverflow.com/questions/22020266/android-edittext-onkeyup
    TextWatcher mTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String currentMode = getActivity().getTitle().toString();
            updateFriends(currentMode, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String currentMode = getActivity().getTitle().toString();

            try {
                // http://anditstory.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9CListViewCHOICEMODESINGLECHOICEMODEMULTIPLE
                ArrayList<String> alUseridx = new ArrayList<>(lvFriends.getCheckedItemCount());
                SparseBooleanArray booleans = lvFriends.getCheckedItemPositions();
                for (int i = 0; i < jsonArr_friends.length(); i++) {
                    if (booleans.get(i)) {
                        alUseridx.add(jsonArr_friends.getJSONObject(i).getString("idx"));
                    }
                }

                if (currentMode.equals("Add Friends")) {
                    dbConn.addFriends(single.getCurrentUserIdx(), alUseridx);
                    Toast.makeText(getActivity(), alUseridx.size() + " friends added!", Toast.LENGTH_LONG).show();
                } else if (currentMode.equals("Remove Friends")) {
                    dbConn.removeFriends(single.getCurrentUserIdx(), alUseridx);
                    Toast.makeText(getActivity(), alUseridx.size() + " friends removed!", Toast.LENGTH_LONG).show();
                }

                // http://stackoverflow.com/questions/12659747/call-an-activity-method-from-a-fragment
                ((MainActivity)getActivity()).updateNavigationView();

                // http://stackoverflow.com/questions/9754170/listview-selection-remains-persistent-after-exiting-choice-mode
                lvFriends.clearChoices();
                etSearchInput.setText("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
