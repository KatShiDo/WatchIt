package com.example.watchit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.watchit.R;

public class LogoActivity extends AppCompatActivity
{
    private Animation logo_anim, button_start_anim;
    private Button button_start;
    private ImageView logo_image;
    private boolean isFinished = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);
        init();
        button_start.setOnClickListener(v -> {
            onClickStart();
            isFinished = true;
        });
        startMainActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }

    private void onClickStart()
    {
        Intent intent = new Intent(LogoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void init()
    {
        button_start = findViewById(R.id.button_start);
        logo_image = findViewById(R.id.logo_image);
        logo_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
        button_start_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);
        logo_image.startAnimation(logo_anim);
        button_start.startAnimation(button_start_anim);
    }

    private void startMainActivity()
    {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!isFinished)
            {
                onClickStart();
            }
        }).start();
    }
}
