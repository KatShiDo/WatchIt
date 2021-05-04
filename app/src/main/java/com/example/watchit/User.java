package com.example.watchit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

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

    /*protected User(Parcel in) {
        nickname = in.readString();
        byte[] byte_array = new byte[0];
        in.readByteArray(byte_array);
        avatar = BitmapFactory.decodeByteArray(byte_array, 0, byte_array.length);
        //Friends = in.createTypedArray(User.CREATOR);
        Friends = (User[]) in.readParcelableArray(User.class.getClassLoader());
        Unwatched = (Title[]) in.readParcelableArray(Title.class.getClassLoader());
        Watched = (Title[]) in.readParcelableArray(Title.class.getClassLoader());
    }*/

    /*public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };*/

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
        dest.writeParcelable(avatar, flags);
        dest.writeTypedArray(Friends, flags);
        dest.writeTypedArray(Unwatched, flags);
        dest.writeTypedArray(Watched, flags);
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

    /*@Override
    public int describeContents() {
        return 0;
    }*/

    /*@Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, stream);
        dest.writeByteArray(stream.toByteArray());
        dest.writeSerializable(Friends);
        dest.writeSerializable(Unwatched);
        dest.writeSerializable(Watched);
    }*/
}
