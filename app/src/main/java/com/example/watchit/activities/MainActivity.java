package com.example.watchit.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import com.example.watchit.network.DataBase;
import com.example.watchit.R;
import com.example.watchit.User;
import com.example.watchit.utils.CustomArrayAdapter;
import com.example.watchit.utils.ListItemClass;
import com.google.android.material.navigation.NavigationView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    public static User user;

    private CustomArrayAdapter adapter;

    private Button button_add_unwatched, button_add_watched, button_add_friend;

    private void notify_user_changed() {
        new AccountContent().formLists();
    }

    private void disableAll() {
        findViewById(R.id.content_account).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_friends).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_unwatched).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_watched).setVisibility(View.INVISIBLE);
        findViewById(R.id.content_registration).setVisibility(View.INVISIBLE);
        findViewById(R.id.choose_avatar).setVisibility(View.INVISIBLE);
    }

    private void enable(int id) {
        findViewById(id).setVisibility(View.VISIBLE);
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

    private class AccountContent {

        private EditText edit_text_login = findViewById(R.id.edit_text_login);
        private EditText edit_text_password = findViewById(R.id.edit_text_password);
        private ListView list_view_unwatched = findViewById(R.id.list_view_unwatched);
        private ListView list_view_watched = findViewById(R.id.list_view_watched);
        private ListView list_view_friends = findViewById(R.id.list_view_friends);

        @SuppressLint("ShowToast")
        public void execute() {

            Button button_login = findViewById(R.id.button_login);
            Button button_start_registration = findViewById(R.id.button_start_registration);

            button_login.setOnClickListener(v -> {
                DataBase db;
                try {
                    db = new DataBase();
                    user = db.login_user(edit_text_login.getText().toString(), edit_text_password.getText().toString(), MainActivity.this);
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
                if (user != null) {
                    formLists();
                    Toast.makeText(getApplicationContext(), "Успешный вход", Toast.LENGTH_LONG);
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
                else {
                    Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT);
                }
            });

            button_start_registration.setOnClickListener(v -> {
                RegistrationContent();
            });
        }

        public void formLists() {
            formUnwatched();
            formWatched();
            formFriends();
        }

        private void formUnwatched() {
            adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.list_item, ListItemClass.createListItemClass(user.getUnwatched()), getLayoutInflater());
            list_view_unwatched.setAdapter(adapter);
            try {
                list_view_unwatched.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                    intent.putExtra("title", user.getUnwatched()[position]);
                    intent.putExtra("isWatched", false);
                    intent.putExtra("isFriend", false);
                    startActivity(intent);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void formWatched() {
            adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.list_item, ListItemClass.createListItemClass(user.getWatched()), getLayoutInflater());
            list_view_watched.setAdapter(adapter);
            try {
                list_view_watched.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(MainActivity.this, TitleActivity.class);
                    intent.putExtra("title", user.getWatched()[position]);
                    intent.putExtra("isWatched", true);
                    intent.putExtra("isFriend", false);
                    startActivity(intent);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void formFriends() {
            adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.list_item, ListItemClass.createListItemClass(user.getFriends()), getLayoutInflater());
            list_view_friends.setAdapter(adapter);
            try {
                list_view_friends.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(MainActivity.this, FriendActivity.class);
                    intent.putExtra("position", position);
                    startActivity(intent);
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void RegistrationContent() {
            Button button_registration_confirm;
            EditText edit_text_registration_nickname, edit_text_registration_password;

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
                        Toast.makeText(getApplicationContext(), "Этот никнейм занят", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        new AccountContent().execute();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        disableAll();
        switch (id) {
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
        if (user != null)
            notify_user_changed();
        return true;
    }
}