package com.example.awesometic.facetalk;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesometic on 2016-05-29.
 * https://github.com/nkzawa/socket.io-android-chat
 */
public class ChatFragment extends Fragment {

    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(getActivity(), getActivity());

    private List<ChatRecyclerViewItem> messages = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView rvMessage;
    private Button btnSend;
    private EditText etMessage;

    private int useridx;
    private int friendidx;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    public ChatFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        http://stackoverflow.com/questions/26580723/how-to-scroll-to-the-bottom-of-a-recyclerview-scrolltoposition-doesnt-work
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        rvMessage = (RecyclerView) rootView.findViewById(R.id.recyclerView_message);
        rvMessage.setLayoutManager(linearLayoutManager);
        btnSend = (Button) rootView.findViewById(R.id.button_send_message);
        btnSend.setOnClickListener(mClickListener);
        etMessage = (EditText) rootView.findViewById(R.id.editText_message);
        etMessage.setOnKeyListener(mKeyUpListener);
        etMessage.requestFocus();

        useridx = single.getCurrentUserIdx();
        friendidx = getArguments().getInt("friendidx");

        refreshMessage();

        return rootView;
    }

    private void refreshMessage() {
        try {
            messages.clear();

            JSONArray jsonArr_messages = dbConn.getMessage(useridx, friendidx);
            String nickname;
            String message;

            for (int i = (jsonArr_messages.length() - 1); i >= 0; i--) {
                if (jsonArr_messages.getJSONObject(i).getString("user").equals(String.valueOf(single.getCurrentUserIdx()))) {
                    nickname = single.getCurrentUserNickname();
                } else {
                    nickname = single.getFriendInfo(friendidx).getString("nickname");
                }
                message = jsonArr_messages.getJSONObject(i).getString("message");

                addMessage(nickname, message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(String nickname, String message) {
        RecyclerView.Adapter rvMessageAdapter = new ChatRecyclerViewAdapter(getActivity(), messages);
        rvMessage.setAdapter(rvMessageAdapter);

        messages.add(new ChatRecyclerViewItem.Builder()
                .setNickname(nickname).setMessage(message)
                .build());
        rvMessageAdapter.notifyItemInserted(messages.size() - 1);
    }

    private void attemptSend() {
        String message = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            etMessage.requestFocus();
            return;
        }

        if (dbConn.addMessage(useridx, friendidx, message) == 1) {
            etMessage.setText("");
            addMessage(single.getCurrentUserNickname(), message);

            refreshMessage();
        } else {
            Toast.makeText(getActivity(), "Add message to server fail!", Toast.LENGTH_LONG).show();
        }
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            attemptSend();
        }
    };

    EditText.OnKeyListener mKeyUpListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                attemptSend();
                return true;
            }
            else
                return false;
        }
    };
}
