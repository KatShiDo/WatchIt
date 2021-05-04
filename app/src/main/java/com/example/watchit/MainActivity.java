package com.example.watchit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private User user;

    private MyAdapter adapter;

    private void disableAll()
    {
        findViewById(R.id.content_account).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_friends).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_unwatched).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_watched).setVisibility(View.INVISIBLE);
    }

    private void enable(int id)
    {
        findViewById(id).setVisibility(View.VISIBLE);
    }

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

    private class AccountContent
    {
        private EditText edit_text_login, edit_text_password;
        private ListView list_view_unwatched, list_view_watched, list_view_friends;
        @SuppressLint("ShowToast")
        public AccountContent()
        {
            Button button_login = findViewById(R.id.button_login);
            edit_text_login = findViewById(R.id.edit_text_login);
            edit_text_password = findViewById(R.id.edit_text_password);
            button_login.setOnClickListener(v ->
            {
                DataBase db = null;
                try {
                    db = new DataBase();
                    user = db.login_user(edit_text_login.getText().toString(), edit_text_password.getText().toString(), MainActivity.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (user != null)
                {
                    formLists();
                    Toast.makeText(getApplicationContext(), "Успешный вход", Toast.LENGTH_SHORT);
                    button_login.setEnabled(false);
                    edit_text_login.setEnabled(false);
                    edit_text_password.setEnabled(false);
                }
            });

            list_view_unwatched = findViewById(R.id.list_view_unwatched);
            list_view_watched = findViewById(R.id.list_view_watched);
            list_view_friends = findViewById(R.id.list_view_friends);
        }

        private void formLists()
        {
            formUnwatched();
            formWatched();
            formFriends();
        }

        private void formUnwatched()
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
            list_view_unwatched.setAdapter(adapter);
            try {
                list_view_unwatched.setOnItemClickListener((parent, view, position, id) ->
                {
                    Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                    intent.putExtra("title", user.getUnwatched()[position]);
                    startActivity(intent);
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        private void formWatched()
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
            list_view_watched.setAdapter(adapter);
            list_view_watched.setOnItemClickListener((parent, view, position, id) ->
            {
                Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                intent.putExtra("title", user.getWatched()[position]);
                startActivity(intent);
            });
        }

        private void formFriends()
        {
            String[] list_friends = new String[0];
            Bitmap[] list_friends_images = new Bitmap[0];
            int i = 0;
            for (User user:user.getFriends())
            {
                list_friends = Arrays.copyOf(list_friends, list_friends.length + 1);
                list_friends[i] = user.getNickname();
                list_friends_images = Arrays.copyOf(list_friends_images, list_friends_images.length + 1);
                list_friends_images[i] = user.getAvatar();
                i++;
            }
            adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, list_friends, list_friends_images);
            list_view_friends.setAdapter(adapter);
            list_view_friends.setOnItemClickListener((parent, view, position, id) ->
            {
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                intent.putExtra("friend", (Parcelable) user.getFriends()[position]);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_account);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        new AccountContent();

        enable(R.id.content_account);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        disableAll();
        switch (id)
        {
            case R.id.nav_account:
                enable(R.id.content_account);
                toolbar.setTitle(R.string.menu_account);
                break;
            case R.id.nav_friends:
                enable(R.id.content_friends);
                toolbar.setTitle(R.string.menu_friends);
                break;
            case R.id.nav_unwatched:
                enable(R.id.content_unwatched);
                toolbar.setTitle(R.string.menu_unwatched);
                break;
            case R.id.nav_watched:
                enable(R.id.content_watched);
                toolbar.setTitle(R.string.menu_watched);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}