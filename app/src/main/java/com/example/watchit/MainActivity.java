package com.example.watchit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import androidx.annotation.Nullable;
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
import java.sql.SQLException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    public static User user;

    private MyAdapter adapter;

    private Button button_add_unwatched, button_add_watched, button_add_friend;

    private void disableAll()
    {
        findViewById(R.id.content_account).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_friends).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_unwatched).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_watched).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_registration).setVisibility(View.INVISIBLE);
        findViewById(R.id.choose_avatar).setVisibility(View.INVISIBLE);
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

    private void update_avatar(int avatar_index){
        DataBase db = null;
        try {
            db = new DataBase();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert db != null;
        try {
            db.set_user_avatar(user.getNickname(), avatar_index);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ImageView image_view_avatar;
        image_view_avatar = findViewById(R.id.image_view_avatar);
        image_view_avatar.setImageBitmap(user.getAvatar());
        disableAll();
        enable(R.id.content_account);
    }

    private void set_avatar() {
        disableAll();
        enable(R.id.choose_avatar);
        drawer.closeDrawer(GravityCompat.START);
        ImageView av1, av2, av3, av4, av5, av6, av7, av8, av9, av10, av11, av12, av13, av14, av15, av16;
        av1 = findViewById(R.id.avatar_1);
        av2 = findViewById(R.id.avatar_2);
        av3 = findViewById(R.id.avatar_3);
        av4 = findViewById(R.id.avatar_4);
        av5 = findViewById(R.id.avatar_5);
        av6 = findViewById(R.id.avatar_6);
        av7 = findViewById(R.id.avatar_7);
        av8 = findViewById(R.id.avatar_8);
        av9 = findViewById(R.id.avatar_9);
        av10 = findViewById(R.id.avatar_10);
        av11 = findViewById(R.id.avatar_11);
        av12 = findViewById(R.id.avatar_12);
        av13 = findViewById(R.id.avatar_13);
        av14 = findViewById(R.id.avatar_14);
        av15 = findViewById(R.id.avatar_15);
        av16 = findViewById(R.id.avatar_16);

        av1.setOnClickListener(v ->
        {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av1));
            update_avatar(1);
        });
        av2.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av2));
            update_avatar(2);
        });
        av3.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av3));
            update_avatar(3);
        });
        av4.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av4));
            update_avatar(4);
        });
        av5.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av5));
            update_avatar(5);
        });
        av6.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av6));
            update_avatar(6);
        });
        av7.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av7));
            update_avatar(7);
        });
        av8.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av8));
            update_avatar(8);
        });
        av9.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av9));
            update_avatar(9);
        });
        av10.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av10));
            update_avatar(10);
        });
        av11.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av11));
            update_avatar(11);
        });
        av12.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av12));
            update_avatar(12);
        });
        av13.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av13));
            update_avatar(13);
        });
        av14.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av14));
            update_avatar(14);
        });
        av15.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av15));
            update_avatar(15);
        });
        av16.setOnClickListener(v -> {
            user.setAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.av16));
            update_avatar(16);
        });


    }

    private class RegistrationContent
    {
        private Button button_registration_confirm;
        private EditText edit_text_registration_nickname, edit_text_registration_password;
        @SuppressLint("ShowToast")
        public RegistrationContent()
        {
            disableAll();
            enable(R.id.content_registration);
            button_registration_confirm = findViewById(R.id.button_registration_confirm);
            edit_text_registration_nickname = findViewById(R.id.edit_text_registration_nickname);
            edit_text_registration_password = findViewById(R.id.edit_text_registration_password);

            button_registration_confirm.setOnClickListener(v -> {
                try {
                    DataBase db = new DataBase();
                    if (!db.add_user(edit_text_registration_nickname.getText().toString(), edit_text_registration_password.getText().toString()))
                    {
                        Toast.makeText(getApplicationContext(), "Этот никнейм занят", Toast.LENGTH_LONG);
                    }
                    else
                    {
                        disableAll();
                        enable(R.id.content_account);
                    }
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            });
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
            Button button_start_registration = findViewById(R.id.button_start_registration);
            edit_text_login = findViewById(R.id.edit_text_login);
            edit_text_password = findViewById(R.id.edit_text_password);
            button_login.setOnClickListener(v ->
            {
                DataBase db;
                try {
                    db = new DataBase();
                    user = db.login_user(edit_text_login.getText().toString(), edit_text_password.getText().toString(), MainActivity.this);
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
                if (user != null)
                {
                    formLists();
                    Toast.makeText(getApplicationContext(), "Успешный вход", Toast.LENGTH_SHORT);
                    button_login.setEnabled(false);
                    button_start_registration.setEnabled(false);
                    edit_text_login.setEnabled(false);
                    edit_text_password.setEnabled(false);
                    TextView text_view_nickname;
                    text_view_nickname = findViewById(R.id.text_view_nickname);
                    text_view_nickname.setText(user.getNickname());
                    ImageView image_view_avatar;
                    image_view_avatar = findViewById(R.id.image_view_avatar);
                    image_view_avatar.setImageBitmap(user.getAvatar());

                    image_view_avatar.setOnClickListener(v1 -> set_avatar());

                    navigationView.getMenu().findItem(R.id.nav_friends).setEnabled(true);
                    navigationView.getMenu().findItem(R.id.nav_unwatched).setEnabled(true);
                    navigationView.getMenu().findItem(R.id.nav_watched).setEnabled(true);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT);
                }
            });

            button_start_registration.setOnClickListener(v -> {
                new RegistrationContent();
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
            try {
                list_view_watched.setOnItemClickListener((parent, view, position, id) ->
                {
                    Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                    intent.putExtra("title", user.getWatched()[position]);
                    startActivity(intent);
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
            try {
                list_view_friends.setOnItemClickListener((parent, view, position, id) ->
                {
                    Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                    intent.putExtra("friend", (Parcelable) user.getFriends()[position]);
                    startActivity(intent);
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            assert data != null;
            user = data.getParcelableExtra("user");
        }
    }*/

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
        disableAll();
        enable(R.id.content_account);

        button_add_unwatched = findViewById(R.id.button_add_unwatched);
        button_add_watched = findViewById(R.id.button_add_watched);
        button_add_friend = findViewById(R.id.button_add_friend);

        button_add_unwatched.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTitleActivity.class);
            intent.putExtra("category", 0);
            startActivity(intent);
        });

        button_add_watched.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTitleActivity.class);
            intent.putExtra("category", 1);
            startActivity(intent);
        });

        button_add_friend.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });
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