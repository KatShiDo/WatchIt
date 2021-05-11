package com.example.watchit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchit.R;
import com.example.watchit.Title;

public class TitleActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_title);
        Intent intent = getIntent();
        Title title = intent.getParcelableExtra("title");
        fillInformation(title);
    }

    private void fillInformation(Title title)
    {
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
        if (title.getDescription().length() > 500)
        {
            String desc = title.getDescription().substring(0, 500);
            desc = desc + "...";
            title_description.setText(desc);
        }
        else
        {
            title_description.setText(title.getDescription());
        }
        title_year.setText(title.getYear());
        title_genre.setText(title.getGenre());
        title_producer.setText(title.getProducer());
        title_url.setText(title.getUrl());
    }
}
