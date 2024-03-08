package com.example.minimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.TextView;

public class StartScreen extends AppCompatActivity {

    MainActivity m = new MainActivity();
    static int numberOfRounds=0;
    static int currentRound=0;
    static String name= "Player 1";
    static String  difficulty = "easy";
    static String avatar;
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

        Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.diff_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        // Set up a listener for item selection
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                difficulty = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        ImageView avatar1 = findViewById(R.id.avatar1);
        ImageView avatar2 = findViewById(R.id.avatar2);
        ImageView avatar3 = findViewById(R.id.avatar3);
        ImageView avatar4 = findViewById(R.id.avatar4);
        ImageView avatar5 = findViewById(R.id.avatar5);
        ImageView avatar6 = findViewById(R.id.avatar6);
        chosen = findViewById(R.id.chosen);


        // Add more image view references for avatars if needed

        // Set click listeners for avatar selection
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


    public void selectAvatar(ImageView image) {
        // Get the current drawable of the ImageView
        if(avatar!=null) {
            int resId = getResources().getIdentifier(avatar, "id", getPackageName());
            ImageView img = findViewById(resId);


            Drawable currentDrawable = img.getDrawable();

// Create a GradientDrawable for the border
            GradientDrawable borderDrawable = new GradientDrawable();
            borderDrawable.setShape(GradientDrawable.OVAL); // Set the shape to Oval for circular shape
            borderDrawable.setVisible(false, false);
//            borderDrawable.setStroke(0, Color.GRAY); // Set the border width to 0 to remove the border

// Create a LayerDrawable to combine the image and the border
//            Drawable[] layers = {currentDrawable, borderDrawable};
//            LayerDrawable layerDrawable = new LayerDrawable(layers);
            img.setImageDrawable(borderDrawable);

        }
        // Set the LayerDrawable as the image drawable of the ImageView


        Drawable current = image.getDrawable();

        // Create a GradientDrawable for the border
        GradientDrawable border = new GradientDrawable();
        border.setShape(GradientDrawable.OVAL); // Set the shape to Oval for circular shape
        border.setStroke(12, Color.parseColor("#E17B26")); // Set the border width and color

        // Create a LayerDrawable to combine the image and the border
        Drawable[] layer = {current, border};
        LayerDrawable layDrawable = new LayerDrawable(layer);

        // Set the LayerDrawable as the image drawable of the ImageView
        image.setImageDrawable(layDrawable);
    }


    public void startButton (View v){
        EditText getName = findViewById(R.id.nameEditText);
        name = getName.getText().toString();
        if(name.isEmpty()){
            name= "Player 1";
        }

        Intent intent=new Intent(StartScreen.this, MainActivity.class);


        startActivityForResult(intent, 1);
//        m.startGame(v);


    }
}