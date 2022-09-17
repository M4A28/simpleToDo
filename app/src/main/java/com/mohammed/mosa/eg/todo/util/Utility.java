package com.mohammed.mosa.eg.todo.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static Date toDate(Long time){

        return new Date(time);
    }

    public static long toLong(Date date){
        return date.getTime();
    }


    public static ColorDrawable colorPriority(int priority){
        switch (priority){
            case 1:
                return new ColorDrawable(Color.parseColor("#ff0000"));
            case 2:
                return new ColorDrawable(Color.parseColor("#ffcc00"));
            case 3:
                return new ColorDrawable(Color.parseColor("#37c837"));
        }
        return new ColorDrawable(Color.parseColor("#2196F3"));
    }

    public static int toInt(boolean state){
        return state? 1: 0;
    }

    public static boolean toBoolean(int state){
        return state == 1;
    }

    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        return dateFormat.format(date);
    }

    public static String fitString(String text, int length){
        return text.length() <= length ? text : text.substring(0, length);
    }

}
