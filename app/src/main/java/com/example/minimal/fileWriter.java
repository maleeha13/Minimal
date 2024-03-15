package com.example.minimal;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File writer class is used to log details into a text file for testing and eval purposes
 */
public class fileWriter {
    public static void appendToFile(Context context, String fileName, String data) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_APPEND | Context.MODE_PRIVATE);

            // Append the data to the file
            fos.write((data + "\n").getBytes());


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
