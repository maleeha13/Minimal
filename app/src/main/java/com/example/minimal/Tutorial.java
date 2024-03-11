package com.example.minimal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class Tutorial extends AppCompatActivity {

    private VideoView videoView;
    private ImageView imageView;
    private Button btnPrevious, btnNext;
    private int currentContentIndex = 0;
    private int currentPic = 0;
    private int currentVid = 0;

    private boolean isVideoMode = false; // Start with video mode

    // Define your video and image URLs
    private int[] videoUrls = {
            R.raw.t3,
            R.raw.t5,
            R.raw.t7,
            R.raw.t9,
            R.raw.t11,
            R.raw.t13,
            R.raw.t16,

            // Add more video URLs as needed
    };

    // Define your image URLs or resource IDs
    private int[] imageResourceIds = {
            R.drawable.t1,
            R.drawable.t2,
            R.drawable.t4,
            R.drawable.t6,
            R.drawable.t8,
            R.drawable.t10,
            R.drawable.t12,
            R.drawable.t14,
            R.drawable.t15

            // Add more image resource IDs as needed
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        videoView = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        // Initially show the first content
        showContent(currentContentIndex);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentContentIndex > 0) {
                    currentContentIndex--;
                    showContent(currentContentIndex);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentContentIndex < 16) {
                    currentContentIndex++;
                    System.out.println("next is " + currentContentIndex);
                    showContent(currentContentIndex);
                }
                else{
                    Intent intent = new Intent(Tutorial.this, StartScreen.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showContent(int index) {
        if (index < videoUrls.length + imageResourceIds.length) {
            switch (index) {
                case 0:
                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 14:
                    // Show images
                    isVideoMode = false;
                    videoView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(imageResourceIds[currentPic]);
                    currentPic++;
                    break;
                default:
                    // Show videos
                    isVideoMode = true;
                    imageView.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videoUrls[currentVid ]));
                    videoView.start();
                    currentVid++;
                    break;
            }
        }
    }



}
