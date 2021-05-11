package com.example.watchit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchit.R;
import com.example.watchit.User;
import com.example.watchit.utils.CustomArrayAdapter;
import com.example.watchit.utils.ListItemClass;

import static com.example.watchit.activities.MainActivity.user;

public class FriendActivity extends AppCompatActivity
{
    private ListView friend_list_view_unwatched, friend_list_view_watched;
    private CustomArrayAdapter adapter;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_friend);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        friend_list_view_unwatched = findViewById(R.id.friend_list_view_unwatched);
        friend_list_view_watched = findViewById(R.id.friend_list_view_watched);
        fillInformation();
    }

    public void fillInformation()
    {
        ImageView friend_avatar = findViewById(R.id.friend_avatar);
        friend_avatar.setImageBitmap(user.getFriends()[position].getAvatar());

        TextView friend_nickname = findViewById(R.id.friend_nickname);
        friend_nickname.setText(user.getFriends()[position].getNickname());
        formUnwatched(user.getFriends()[position]);
        formWatched(user.getFriends()[position]);
    }

    private void formUnwatched(User user)
    {
        adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.list_item, ListItemClass.createListItemClass(user.getUnwatched()), getLayoutInflater());
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
        adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.list_item, ListItemClass.createListItemClass(user.getWatched()), getLayoutInflater());
        friend_list_view_watched.setAdapter(adapter);
        friend_list_view_watched.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent intent = new Intent(FriendActivity.this, TitleActivity.class);
            intent.putExtra("title", user.getWatched()[position]);
            startActivity(intent);
        });
    }
}
