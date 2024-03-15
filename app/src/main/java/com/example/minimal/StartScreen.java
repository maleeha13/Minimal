package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * The StartScreen class represents the starting screen of the game.
 */
public class StartScreen extends AppCompatActivity {

    /** Total number of rounds set by the user */
    static int numberOfRounds=0;

    /** Current round of the game */
    static int currentRound=0;

    /** Default name for player*/
    static String name= "Player 1";

    /** Default difficulty */
    static String  difficulty = "easy";

    /** Default avatar*/
    static String avatar = "avatar1";

    /** Image of chosen avatar */
    static ImageView chosen;


    /**
     * Sets spinner options for difficulty and number of rounds and sets avatar
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Spinner spinner = findViewById(R.id.spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_options, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.dropdown);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = parentView.getItemAtPosition(position).toString();
                setRounds(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.diff_options, R.layout.custom_spinner_item);

        adapter2.setDropDownViewResource(R.layout.dropdown);

        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                difficulty = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        ImageView avatar1 = findViewById(R.id.avatar1);
        ImageView avatar2 = findViewById(R.id.avatar2);
        ImageView avatar3 = findViewById(R.id.avatar3);
        ImageView avatar4 = findViewById(R.id.avatar4);
        ImageView avatar5 = findViewById(R.id.avatar5);
        ImageView avatar6 = findViewById(R.id.avatar6);
        chosen = findViewById(R.id.chosen);

        avatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar(avatar1);
                avatar= "avatar1";
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av1));
            }
        });

        avatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar(avatar2);
                avatar= "avatar2";
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av2));
            }
        });

        avatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar(avatar3);
                avatar= "avatar3";
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av3));
            }
        });

        avatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar(avatar4);
                avatar= "avatar4";
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av4));
            }
        });

        avatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar(avatar5);
                avatar= "avatar5";
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av5));
            }
        });

        avatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAvatar(avatar6);
                avatar= "avatar6";
                chosen.setImageDrawable(ActivityCompat.getDrawable(getApplicationContext(), R.drawable.av6));
            }
        });
        avatar1.performClick();
    }

    /**
     * Sets the number of rounds based on the selected option.
     * @param rounds The selected number of rounds.
     */
    public static void setRounds(String rounds){
        numberOfRounds= Integer.parseInt(rounds);
    }

    /**
     * Highlights the selected avatar and updates the chosen avatar.
     * @param image The ImageView of the selected avatar.
     */
    public void selectAvatar(ImageView image) {
        if(avatar!=null) {
            int resId = getResources().getIdentifier(avatar, "id", getPackageName());
            ImageView img = findViewById(resId);
            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setShape(GradientDrawable.OVAL);
            borderDrawable.setVisible(false, false);
            img.setImageDrawable(borderDrawable);

        }
        Drawable current = image.getDrawable();

        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.OVAL);
        border.setStroke(12, Color.parseColor("#E17B26"));

        Drawable[] layer = {current, border};
        LayerDrawable layDrawable = new LayerDrawable(layer);

        image.setImageDrawable(layDrawable);
    }

    /**
     * Starts the game activity.
     * @param v The View.
     */
    public void startButton (View v){
        EditText getName = findViewById(R.id.nameEditText);
        name = getName.getText().toString();
        if(name.isEmpty()){
            name= "Player 1";
        }
        Intent intent=new Intent(StartScreen.this, MainActivity.class);
        startActivity(intent);


    }
}