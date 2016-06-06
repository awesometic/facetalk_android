package com.example.awesometic.facetalk;

/**
 * Created by Awesometic on 2016-06-02.
 * http://recipes4dev.tistory.com/43
 */
public class FriendsListViewItem {
    private String titleStr;
    private String descStr;

    public FriendsListViewItem(String title, String desc) {
        titleStr = title;
        descStr = desc;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
