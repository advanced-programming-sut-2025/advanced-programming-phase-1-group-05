package org.example.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileController {
    public static void writeTextToFile(String text , String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(text);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean deleteGameOwnedBy(String ownerUsername) {
        try {
            // فرض بر این است که فایل game_state.json حاوی ownerUsername است
            String json = getTextOfFile("game_state.json");
            if (json == null || json.trim().isEmpty()) return false;

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            if (obj.get("ownerUsername").getAsString().equals(ownerUsername)) {
                return new File("game_state.json").delete();
            }
        } catch (Exception e) {
            System.err.println("Error deleting game: " + e.getMessage());
        }
        return false;
    }


    public static void writeTextToFile2(String text) {
        try {
            FileWriter fileWriter = new FileWriter("game_state.json");
            fileWriter.write(text);
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    public static String getTextOfFile(String path) {
//        String text = "";
//        try {
//            File file = new File(path);
//            Scanner fileScanner = new Scanner(file);
//            while (fileScanner.hasNextLine()) {
//                text += fileScanner.nextLine() + "\n";
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return text;
//    }
    public static String getTextOfFile(String path) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(path); // ✅ استفاده صحیح از ورودی
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                text.append(fileScanner.nextLine()).append("\n");
            }
            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file " + path + ": " + e.getMessage());
        }
        return text.toString();
    }


    public static void copyFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static File creatingFile(String path) {
        System.out.println(path);
        try {
            File myObj = new File(path);
            myObj.createNewFile();
            return myObj;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileExtension(String fileName) {
        String extension = "";

        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return extension;
    }

    public static void saveFile(File file) {

    }
}
