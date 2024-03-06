package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class StartScreen extends AppCompatActivity {

    MainActivity m = new MainActivity();
    static int numberOfRounds=0;
    static int currentRound=0;
    static String name= "Player 1";
    static ImageView avatar;
    static ImageView chosen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.dropdown_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set up a listener for item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedOption = parentView.getItemAtPosition(position).toString();
                setRounds(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        ImageView avatar1 = findViewById(R.id.avatarImageView1);
        ImageView avatar2 = findViewById(R.id.avatarImageView2);
        ImageView avatar3 = findViewById(R.id.avatarImageView3);
        ImageView avatar4 = findViewById(R.id.avatarImageView4);
        ImageView avatar5 = findViewById(R.id.avatarImageView5);
        ImageView avatar6 = findViewById(R.id.avatarImageView6);
        chosen = findViewById(R.id.chosen);


        // Add more image view references for avatars if needed

        // Set click listeners for avatar selection
        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av1));
            }
        });

        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av2));
            }
        });

        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av3));
            }
        });

        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av4));
            }
        });

        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av5));
            }
        });

        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("check");
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av6));
            }
        });
    }

//    public  void setAvatar(ImageView av) {
//
//        Drawable avatarDrawable = av.getDrawable();
//        if (chosen != null && avatarDrawable != null) {
//
//            chosen.setImageResource(R.drawable.av3);
//        }
//        // Update other avatar related logic here if needed
//    }

    public static void setRounds(String rounds){
        numberOfRounds= Integer.parseInt(rounds);
    }



    public void startButton (View v){
        Intent intent=new Intent(StartScreen.this, MainActivity.class);


        startActivityForResult(intent, 1);
//        m.startGame(v);


    }
}