package com.example.awesometic.facetalk;

import android.graphics.drawable.Drawable;

/**
 * Created by Awesometic on 2016-06-02.
 * http://recipes4dev.tistory.com/43
 */
public class FriendsListViewItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String descStr;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setDesc(String desc) {
        descStr = desc;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
