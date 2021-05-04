package com.example.watchit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.zip.Inflater;

public class FriendActivity extends AppCompatActivity
{
    private ListView friend_list_view_unwatched, friend_list_view_watched;
    private MyAdapter adapter;

    private class MyAdapter extends ArrayAdapter<String>
    {
        public String[] text_list;
        public Bitmap[] bitmap_list;

        public MyAdapter(Context context, int textViewResourceId, String[] objects, Bitmap[] images)
        {
            super(context, textViewResourceId, objects);
            text_list = objects;
            bitmap_list = images;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_item, parent, false);
            TextView label = row.findViewById(R.id.item_text);
            label.setText(text_list[position]);
            ImageView iconImageView = row.findViewById(R.id.item_image);
            iconImageView.setImageBitmap(bitmap_list[position]);
            return row;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_friend);
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("friend");
        friend_list_view_unwatched = findViewById(R.id.friend_list_view_unwatched);
        friend_list_view_watched = findViewById(R.id.friend_list_view_watched);
        fillInformation(user);
    }

    public void fillInformation(User user)
    {
        ImageView friend_avatar = findViewById(R.id.friend_avatar);
        try
        {
            friend_avatar.setImageBitmap(user.getAvatar());
        }
        catch (Exception ignored)
        {

        }

        TextView friend_nickname = findViewById(R.id.friend_nickname);
        friend_nickname.setText(user.getNickname());
        formUnwatched(user);
        formWatched(user);
    }

    private void formUnwatched(User user)
    {
        String[] list_unwatched = new String[0];
        Bitmap[] list_unwatched_images = new Bitmap[0];
        int i = 0;
        for (Title title:user.getUnwatched())
        {
            list_unwatched = Arrays.copyOf(list_unwatched, list_unwatched.length + 1);
            list_unwatched[i] = title.getCaption();
            list_unwatched_images = Arrays.copyOf(list_unwatched_images, list_unwatched_images.length + 1);
            list_unwatched_images[i] = title.getImage();
            i++;
        }
        adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, list_unwatched, list_unwatched_images);
        friend_list_view_unwatched.setAdapter(adapter);
        friend_list_view_unwatched.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent intent = new Intent(FriendActivity.this, TitleActivity.class);
            intent.putExtra("title", user.getUnwatched()[position]);
            startActivity(intent);
        });
    }

    private void formWatched(User user)
    {
        String[] list_watched = new String[0];
        Bitmap[] list_watched_images = new Bitmap[0];
        int i = 0;
        for (Title title:user.getWatched())
        {
            list_watched = Arrays.copyOf(list_watched, list_watched.length + 1);
            list_watched[i] = title.getCaption();
            list_watched_images = Arrays.copyOf(list_watched_images, list_watched_images.length + 1);
            list_watched_images[i] = title.getImage();
            i++;
        }
        adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, list_watched, list_watched_images);
        friend_list_view_watched.setAdapter(adapter);
        friend_list_view_watched.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent intent = new Intent(FriendActivity.this, TitleActivity.class);
            intent.putExtra("title", user.getWatched()[position]);
            startActivity(intent);
        });
    }
}
