package com.example.awesometic.facetalk;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    private ListView lvFriends;
    private Button btnSubmit;
    private EditText etSearchInput;

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

            FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity());

            etSearchInput = (EditText) rootView.findViewById(R.id.friend_manager_input);
            etSearchInput.setOnKeyListener(mKeyUpListener);
            etSearchInput.addTextChangedListener(mTextChangedListener);
            etSearchInput.requestFocus();
            lvFriends = (ListView) rootView.findViewById(R.id.friend_manager_list);
            lvFriends.setAdapter(adapter);
            btnSubmit = (Button) rootView.findViewById(R.id.friend_manager_submit);
            btnSubmit.setOnClickListener(mClickListener);
            btnSubmit.setEnabled(false);

            if (getActivity().getTitle().equals("Add Friends")) {
                btnSubmit.setText("Add");
                JSONArray jsonArr_friends = dbConn.getAllFriends(single.getCurrentUserIdx());

                for (int i = 0; i < jsonArr_friends.length(); i++) {
                    adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                }

            } else if (getActivity().getTitle().equals("Remove Friends")) {
                btnSubmit.setText("Remove");

                JSONArray jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());

                for (int i = 0; i < jsonArr_friends.length(); i++) {
                    adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                }
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
            try {
                FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity());
                lvFriends.setAdapter(adapter);
                String currentMode = getActivity().getTitle().toString();

                if (currentMode.equals("Add Friends")) {

                    if (s.length() == 0) {
                        JSONArray jsonArr_friends = dbConn.getAllFriends(single.getCurrentUserIdx());

                        for (int i = 0; i < jsonArr_friends.length(); i++) {
                            adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                        }

                        lvFriends.setChoiceMode(ListView.CHOICE_MODE_NONE);
                        btnSubmit.setEnabled(false);
                    } else if (s.length() != 0) {
                        JSONArray jsonArr_friends = dbConn.getSearchedFriends_add(single.getCurrentUserIdx(), s.toString());

                        for (int i = 0; i < jsonArr_friends.length(); i++) {
                            adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                        }

                        lvFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        btnSubmit.setEnabled(true);
                    }
                } else if (currentMode.equals("Remove Friends")) {
                    if (s.length() == 0) {
                        JSONArray jsonArr_friends = dbConn.getFriend(single.getCurrentUserIdx());

                        for (int i = 0; i < jsonArr_friends.length(); i++) {
                            adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                        }

                        lvFriends.setChoiceMode(ListView.CHOICE_MODE_NONE);
                        btnSubmit.setEnabled(false);
                    } else if (s.length() != 0) {
                        JSONArray jsonArr_friends = dbConn.getSearchedFriends_remove(single.getCurrentUserIdx(), s.toString());

                        for (int i = 0; i < jsonArr_friends.length(); i++) {
                            adapter.addItem(jsonArr_friends.getJSONObject(i).getString("nickname"), jsonArr_friends.getJSONObject(i).getString("email"));
                        }

                        lvFriends.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        btnSubmit.setEnabled(true);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            FriendsListViewAdapter adapter = new FriendsListViewAdapter(getActivity());
            lvFriends.setAdapter(adapter);
            String currentMode = getActivity().getTitle().toString();

            if (btnSubmit.isEnabled()) {
                if (currentMode.equals("Add Friends")) {

                } else if (currentMode.equals("Remove Friends")) {

                }

//                Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                etSearchInput.setText("");
            }

        }
    };

}
