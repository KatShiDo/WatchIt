package com.example.watchit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class User implements Parcelable, Serializable
{
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

    protected User(Parcel in) {
        nickname = in.readString();
        avatar = in.readParcelable(Bitmap.class.getClassLoader());
        Friends = in.createTypedArray(User.CREATOR);
        Unwatched = in.createTypedArray(Title.CREATOR);
        Watched = in.createTypedArray(Title.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        Bitmap avatar_compressed = BitmapCompressor.compressBitmap(avatar, 150);
        dest.writeParcelable(avatar_compressed, flags);
        dest.writeTypedArray(Friends, flags);
        dest.writeTypedArray(Unwatched, flags);
        dest.writeTypedArray(Watched, flags);
    }

    public void addUnwatched(Title title)
    {
        this.Unwatched = Arrays.copyOf(this.Unwatched, this.Unwatched.length + 1);
        this.Unwatched[this.Unwatched.length - 1] = title;
    }

    public void addWatched(Title title)
    {
        this.Watched = Arrays.copyOf(this.Watched, this.Watched.length + 1);
        this.Watched[this.Watched.length - 1] = title;
    }

    public void addFriend(User user)
    {
        this.Friends = Arrays.copyOf(this.Friends, this.Friends.length + 1);
        this.Friends[this.Friends.length - 1] = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
