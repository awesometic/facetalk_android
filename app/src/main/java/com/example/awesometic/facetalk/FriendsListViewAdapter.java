package com.example.awesometic.facetalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Awesometic on 2016-06-02.
 * http://recipes4dev.tistory.com/43
 * http://blog.clockworkbits.com/2014/05/26/list-with-checkable-items/
 */
public class FriendsListViewAdapter extends ArrayAdapter<FriendsListViewItem> {
    public FriendsListViewAdapter(Context context, List<FriendsListViewItem> friends) {
        super(context, 0, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.fragment_friends_listview_item, parent, false);
        }

        FriendsListViewItem friend = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.frag_friends_lv_item_textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.frag_friends_lv_item_textView2);

        titleTextView.setText(friend.getTitle());
        descTextView.setText(friend.getDesc());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}