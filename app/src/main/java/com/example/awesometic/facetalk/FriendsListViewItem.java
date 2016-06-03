package com.example.awesometic.facetalk;

import android.graphics.drawable.Drawable;

/**
 * Created by Awesometic on 2016-06-02.
 * http://recipes4dev.tistory.com/43
 */
public class FriendsListViewItem {
    private String titleStr;
    private String descStr;

    public void setTitle(String title) {
        titleStr = title;
    }
    public void setDesc(String desc) {
        descStr = desc;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
