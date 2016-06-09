package com.example.awesometic.facetalk;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Awesometic on 2016-05-29.
 * https://github.com/nkzawa/socket.io-android-chat
 */
public class ChatFragment extends Fragment {

    final static String LogTag = "Awe_ChatFragment";
    private Singleton single = Singleton.getInstance();
    private DBConnect dbConn = new DBConnect(getActivity(), getActivity());

    private List<ChatRecyclerViewItem> messages = new ArrayList<>();

    private RecyclerView rvMessage;
    private Button btnSend;
    private EditText etMessage;
    private RecyclerView.Adapter rvMessageAdapter;

    private int useridx;
    private int friendidx;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    public ChatFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        rvMessageAdapter = new ChatRecyclerViewAdapter(context, messages);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        rvMessage = (RecyclerView) rootView.findViewById(R.id.recyclerView_message);
        rvMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMessage.setAdapter(rvMessageAdapter);
        btnSend = (Button) rootView.findViewById(R.id.button_send_message);
        etMessage = (EditText) rootView.findViewById(R.id.editText_message);

        useridx = single.getCurrentUserIdx();
        friendidx = getArguments().getInt("friendidx");

        try {
            JSONArray jsonArr_messages = dbConn.getMessage(useridx, friendidx);
            String nickname;
            String message;

            for (int i = 0; i < jsonArr_messages.length(); i++) {
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
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addMessage(String nickname, String message) {
        messages.add(new ChatRecyclerViewItem.Builder()
                .setNickname(nickname).setMessage(message)
                .build());
        rvMessageAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        rvMessage.scrollToPosition(rvMessageAdapter.getItemCount() - 1);
    }
}
