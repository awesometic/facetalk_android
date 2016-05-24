package com.example.awesometic.facetalk;

/**
 * Created by Awesometic on 2016-05-22.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    final static String LogTag = "Awe_DBHelper";

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlToCreateUserTable = "CREATE TABLE users (" +
                "idx integer not null primary key, " +
                "email text not null, " +
                "password text not null, " +
                "nickname text not null, " +
                "age integer not null, " +
                "gender text not null);";

        String sqlToCreateFriendsTable = "CREATE TABLE friends (" +
                "idx integer not null primary key, " +
                "user integer not null, " +
                "friend integer not null);";

        String sqlToCreateMessagesTable = "CREATE TABLE messages (" +
                "idx integer not null primary key, " +
                "user integer not null, " +
                "to_user integer not null, " +
                "message text not null);";

        db.execSQL(sqlToCreateUserTable);
        db.execSQL(sqlToCreateFriendsTable);
        db.execSQL(sqlToCreateMessagesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean emailValidate(String email) {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) cnt FROM users WHERE email=?", new String[] {email.trim()});
        cursor.moveToFirst();
        result = cursor.getInt(cursor.getColumnIndex("cnt"));

        db.close();
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean passwordValidate(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) cnt FROM users WHERE email=? AND password=?",
                new String[] {email.trim(), password.trim()});
        cursor.moveToFirst();
        result = cursor.getInt(cursor.getColumnIndex("cnt"));

        db.close();
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void insertUser(String email, String password, String nickname, int age, String gender) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO users (email, password, nickname, age, gender) VALUES (?, ?, ?, ?, ?)",
                new String[] {email.trim(), password.trim(), nickname.trim(), String.valueOf(age), gender.trim()});

        db.close();
    }

    public void insertFriend(int userIdx, int friendIdx) {

    }

    public void deleteFriend(int userIdx, int friendIdx) {

    }

    public String[] getFriends(int userIdx) {

        return null;
    }

    public void insertMessage(int senderIdx, int receiverIdx, String message) {

    }

    public void deleteMessage(int userIdx, int friendIdx) {

    }

    public String[] getMessages(int userIdx, int friendIdx) {

        return null;
    }

    public int getUserIdx(String email) {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        Cursor cursor = db.rawQuery("SELECT idx FROM users WHERE email=?",
                new String[] {email.trim()});
        cursor.moveToFirst();
        result = cursor.getInt(cursor.getColumnIndex("idx"));

        db.close();
        return result;
    }

    public String getUserNickname(int userIdx) {

        return null;
    }
}
