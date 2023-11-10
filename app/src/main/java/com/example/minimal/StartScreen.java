package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {

    MainActivity m = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }


    public void startButton (View v){
        Intent intent=new Intent(StartScreen.this, MainActivity.class);


        startActivityForResult(intent, 1);
//        m.startGame(v);


    }
}