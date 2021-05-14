package com.example.watchit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchit.R;
import com.example.watchit.Title;
import com.example.watchit.network.DataBase;

import java.sql.SQLException;

import static com.example.watchit.activities.MainActivity.user;

public class TitleActivity extends AppCompatActivity
{
    boolean isWatched, isFriend;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_title);
        Intent intent = getIntent();
        Title title = intent.getParcelableExtra("title");
        isWatched = intent.getBooleanExtra("isWatched", false);
        isFriend = intent.getBooleanExtra("isFriend", true);
        fillInformation(title);
    }

    private void fillInformation(Title title)
    {
        Button button_move_to_watched = findViewById(R.id.button_move_to_watched);
        if (isFriend)
        {
            button_move_to_watched.setVisibility(View.INVISIBLE);
        }
        if (isWatched)
        {
            button_move_to_watched.setEnabled(false);
        }
        button_move_to_watched.setOnClickListener(v -> {
            try {
                DataBase db = new DataBase();
                db.move_to_watched(user.getNickname(), title.getUrl());
            }
            catch (InterruptedException | SQLException e) {
                e.printStackTrace();
            }
        });
        ImageView title_image = findViewById(R.id.title_image);
        title_image.setImageBitmap(title.getImage());
        TextView title_caption, title_description, title_year, title_genre, title_producer, title_url;
        title_caption = findViewById(R.id.title_caption);
        title_year = findViewById(R.id.title_year);
        title_description = findViewById(R.id.title_description);
        title_genre = findViewById(R.id.title_genre);
        title_producer = findViewById(R.id.title_producer);
        title_url = findViewById(R.id.title_url);
        title_caption.setText(title.getCaption());
        title_description.setText(title.getDescription());
        title_year.setText(title.getYear());
        title_genre.setText(title.getGenre());
        title_producer.setText(title.getProducer());
        title_url.setText(title.getUrl());
    }
}
