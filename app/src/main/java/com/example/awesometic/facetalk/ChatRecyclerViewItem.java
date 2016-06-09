package com.example.awesometic.facetalk;

/**
 * Created by Awesometic on 2016-06-09.
 * https://github.com/nkzawa/socket.io-android-chat
 */
public class ChatRecyclerViewItem {
    private String nickname;
    private String message;

    public ChatRecyclerViewItem() {

    }

    public String getNickname(){
        return this.nickname;
    }
    public String getMessage(){
        return this.message;
    }

    public static class Builder {
        private String nickname;
        private String message;

        public Builder() {

        }

        public Builder setNickname(String _nickname) {
            this.nickname = _nickname;
            return this;
        }

        public Builder setMessage(String _message) {
            this.message = _message;
            return this;
        }

        public ChatRecyclerViewItem build() {
            ChatRecyclerViewItem item = new ChatRecyclerViewItem();
            item.nickname = nickname;
            item.message = message;
            return item;
        }
    }
}

