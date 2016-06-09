package com.example.awesometic.facetalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Awesometic on 2016-06-09.
 * http://www.kmshack.kr/2014/10/android-recyclerview/
 * https://github.com/nkzawa/socket.io-android-chat
 */
public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private List<ChatRecyclerViewItem> messages;
    static private int myNicknameColor;

    public ChatRecyclerViewAdapter(Context context, List<ChatRecyclerViewItem> _messages) {
        messages = _messages;
        myNicknameColor = context.getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_listview_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChatRecyclerViewItem item = messages.get(position);
        viewHolder.setNickname(item.getNickname());
        viewHolder.setMessage(item.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Singleton single = Singleton.getInstance();

        public TextView nicknameView;
        public TextView messageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nicknameView = (TextView) itemView.findViewById(R.id.textView_chat_item_nickname);
            messageView = (TextView) itemView.findViewById(R.id.textView_chat_item_message);
        }

        public void setNickname(String _nickname) {
            if (null == nicknameView) return;
            nicknameView.setText(_nickname);
            if (_nickname.equals(single.getCurrentUserNickname()))
                nicknameView.setTextColor(myNicknameColor);
        }

        public void setMessage(String _message) {
            if (null == messageView) return;
            messageView.setText(_message);
        }
    }
}
