package com.example.awesometic.facetalk;

/**
 * Created by Awesometic on 2016-06-02.
 * http://recipes4dev.tistory.com/43
 */
public class FriendsListViewItem {
    private String title;
    private String desc;

    public FriendsListViewItem(String _title, String _desc) {
        this.title = _title;
        this.desc = _desc;
    }

    public String getTitle() {
        return this.title ;
    }
    public String getDesc() {
        return this.desc ;
    }
}
