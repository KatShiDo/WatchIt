package com.example.watchit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchit.network.DataBase;
import com.example.watchit.R;

import java.sql.SQLException;

public class AddFriendActivity extends AppCompatActivity
{
    Button button_add_friend_confirm;
    EditText edit_text_friend_nickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        button_add_friend_confirm = findViewById(R.id.button_add_friend_confirm);
        edit_text_friend_nickname = findViewById(R.id.edit_text_friend_nickname);
        button_add_friend_confirm.setOnClickListener(v -> {
            try {
                DataBase db = new DataBase();
                MainActivity.user.addFriend(db.form_user(edit_text_friend_nickname.getText().toString(), true, this));
                db.add_friend(edit_text_friend_nickname.getText().toString(), MainActivity.user);
            } catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
            finish();
        });
    }
}
