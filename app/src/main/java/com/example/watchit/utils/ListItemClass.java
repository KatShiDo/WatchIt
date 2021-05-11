package com.example.watchit.utils;

import android.graphics.Bitmap;

import com.example.watchit.Title;
import com.example.watchit.User;

import java.util.ArrayList;
import java.util.List;

public class ListItemClass {
    private String text;
    private Bitmap image;

    public ListItemClass(String text, Bitmap image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return this.text;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public static List<ListItemClass> createListItemClass(Title[] titles) {
        List<ListItemClass> listItemClass = new ArrayList<>();
        for (Title title : titles) {
            listItemClass.add(new ListItemClass(title.getCaption(), title.getImage()));
        }
        return listItemClass;
    }

    public static List<ListItemClass> createListItemClass(User[] users){
        List<ListItemClass> listItemClass = new ArrayList<>();
        for (User user : users) {
            listItemClass.add(new ListItemClass(user.getNickname(), user.getAvatar()));
        }
        return listItemClass;
    }
}
