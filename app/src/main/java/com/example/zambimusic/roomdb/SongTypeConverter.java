package com.example.zambimusic.roomdb;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SongTypeConverter {

        @TypeConverter
        public static ArrayList<Song> fromString(String value) {
            Type listType = new TypeToken<ArrayList<Song>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String fromArrayList(ArrayList<Long> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }

}
