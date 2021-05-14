package com.example.watchit;

import android.graphics.Bitmap;

import java.util.Arrays;

public class User {
    private String nickname;
    private Bitmap avatar;
    private User Friends[];
    private Title Unwatched[], Watched[];

    public User(String nickname, Bitmap avatar, User[] Friends, Title[] Unwatched, Title[] Watched) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.Friends = Friends;
        this.Watched = Watched;
        this.Unwatched = Unwatched;
    }

    public void addUnwatched(Title title) {
        this.Unwatched = Arrays.copyOf(this.Unwatched, this.Unwatched.length + 1);
        this.Unwatched[this.Unwatched.length - 1] = title;
    }

    public void addWatched(Title title) {
        this.Watched = Arrays.copyOf(this.Watched, this.Watched.length + 1);
        this.Watched[this.Watched.length - 1] = title;
    }

    public void addFriend(User user) {
        this.Friends = Arrays.copyOf(this.Friends, this.Friends.length + 1);
        this.Friends[this.Friends.length - 1] = user;
    }

    public Title[] getUnwatched()
    {
        return this.Unwatched;
    }

    public Title[] getWatched()
    {
        return this.Watched;
    }

    public User[] getFriends()
    {
        return this.Friends;
    }

    public String getNickname()
    {
        return this.nickname;
    }

    public Bitmap getAvatar()
    {
        return this.avatar;
    }

    public void setAvatar(Bitmap avatar)
    {
        this.avatar = avatar;
    }
}
