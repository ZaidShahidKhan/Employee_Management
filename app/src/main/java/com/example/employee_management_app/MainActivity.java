package com.example.employee_management_app;

import android.animation.AnimatorInflater;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private CardView cardAddEmployee, cardViewTeam;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        playStartupSound();
        TextView companyName = findViewById(R.id.tvCompanyName);
        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_three_times);
        companyName.startAnimation(blinkAnimation);

        TextView subtitle = findViewById(R.id.tvSubtitle);
        Animation fadeSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        fadeSlideUp.setStartOffset(300);
        subtitle.startAnimation(fadeSlideUp);

        // Animate management text and divider
        TextView management = findViewById(R.id.tvManagement);
        View divider = findViewById(R.id.divider);

        Animation fadeSlideUpManagement = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        fadeSlideUpManagement.setStartOffset(600);
        management.startAnimation(fadeSlideUpManagement);

        Animation fadeSlideUpDivider = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);
        fadeSlideUpDivider.setStartOffset(800);
        divider.startAnimation(fadeSlideUpDivider);

        CardView cardAddEmployee = findViewById(R.id.cardAddEmployee);
        CardView cardViewTeam = findViewById(R.id.cardViewTeam);

        Animation scaleFadeInAdd = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        scaleFadeInAdd.setStartOffset(1000);
        cardAddEmployee.startAnimation(scaleFadeInAdd);

        Animation scaleFadeInView = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in);
        scaleFadeInView.setStartOffset(1200);
        cardViewTeam.startAnimation(scaleFadeInView);

        View.OnTouchListener cardTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, android.view.MotionEvent event) {
                switch (event.getAction()) {
                    case android.view.MotionEvent.ACTION_DOWN:
                        // Shrink the card
                        view.animate()
                                .scaleX(0.8f)
                                .scaleY(0.8f)
                                .setDuration(100)
                                .start();
                        break;

                    case android.view.MotionEvent.ACTION_UP:
                        // Return to original size and perform click
                        view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    if (view.getId() == R.id.cardAddEmployee) {
                                        Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                                        startActivity(intent);
                                    } else if (view.getId() == R.id.cardViewTeam) {
                                        Intent intent = new Intent(MainActivity.this, EmployeeListActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .start();
                        break;

                    case android.view.MotionEvent.ACTION_CANCEL:
                        // Return to original size if action canceled
                        view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100)
                                .start();
                        break;
                }
                return true;
            }
        };

        cardAddEmployee.setOnTouchListener(cardTouchListener);
        cardViewTeam.setOnTouchListener(cardTouchListener);


        // Make status bar transparent
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

    }
    private void playStartupSound() {
        try {
            // Create and setup MediaPlayer
            mediaPlayer = MediaPlayer.create(this, R.raw.sound);

            // Optional: Set volume
         //   mediaPlayer.setVolume(0.5f, 0.5f);  // Left and right volume (0.0 to 1.0)

            // Start playing
            mediaPlayer.start();

            // Optional: Release when done playing
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}