package com.example.watchit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchit.network.DataBase;
import com.example.watchit.network.Parser;
import com.example.watchit.R;
import com.example.watchit.Title;

import java.sql.SQLException;

public class AddTitleActivity extends AppCompatActivity
{
    Button button_add_title_confirm;
    EditText edit_text_title_url;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_title);
        Intent intent = getIntent();
        button_add_title_confirm = findViewById(R.id.button_add_title_confirm);
        edit_text_title_url = findViewById(R.id.edit_text_title_url);
        button_add_title_confirm.setOnClickListener(v -> {
            try {
                DataBase db;
                switch (intent.getIntExtra("category", -1))
                {
                    case 0:
                        MainActivity.user.addUnwatched(new Title(Parser.getInformation(edit_text_title_url.getText().toString()),
                                Parser.getBitmap(edit_text_title_url.getText().toString(), this)));
                        db = new DataBase();
                        db.add_title(edit_text_title_url.getText().toString(), false, MainActivity.user);
                        break;
                    case 1:
                        MainActivity.user.addWatched(new Title(Parser.getInformation(edit_text_title_url.getText().toString()),
                                Parser.getBitmap(edit_text_title_url.getText().toString(), this)));
                        db = new DataBase();
                        db.add_title(edit_text_title_url.getText().toString(), true, MainActivity.user);
                        break;
                }
            }
            catch (InterruptedException | SQLException e)
            {
                e.printStackTrace();
            }
            finish();
        });
    }
}
