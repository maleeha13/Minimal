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
            // Open the file for appending, creating it if it doesn't exist
            fos = context.openFileOutput(fileName, Context.MODE_APPEND | Context.MODE_PRIVATE);

            // Append the data to the file with a newline character
            fos.write((data + "\n").getBytes());

            // Notify that the data was written successfully
            // You can handle this as needed, such as showing a Toast message
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        } finally {
            // Close the FileOutputStream
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately
                }
            }
        }
    }

}
