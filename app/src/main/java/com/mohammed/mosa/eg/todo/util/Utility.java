package com.mohammed.mosa.eg.todo.util;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.todo.activity.MainActivity;
import com.mohammed.mosa.eg.todo.database.TaskDB;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Utility {

    public static final String CHANNEL_ID = "com.mohammed.mosa.eg.ToDo";
    public static final String NOTIFICATION_NAME = "TaskReminder";
    public static final int NOTIFICATION_ID = 999;
    public static final String TASK = "task";
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

    public static String formatDateAndTime(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        return dateFormat.format(date);
    }

    public static String formatTime(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        return dateFormat.format(date);
    }
    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public static String fitString(String text, int length){
        return text.length() <= length ? text : text.substring(0, length)+"...";
    }

    public static String purString(String text){
        return text.replaceAll("[\\n\\t]", " ").trim();
    }

    public static void playSound(String audio, Context context){
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(audio);
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength());
            mediaPlayer.prepare();
            if(!mediaPlayer.isPlaying())
                mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyPermissions(Activity activity, Context context) {
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(activity, STORAGE_PERMISSIONS, 1);
            return false;
        }
        return true;
    }

    public static boolean isDateInFuture(Date date){
        return date.getTime() <= new Date().getTime();
    }

    public static void testingData(int range, Context context){
        Random random = new Random();
        TaskDB db = TaskDB.getInstance(context);
        String[] randomWord = "man is man and mohammed is man".split(" ");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < range; i++){
            int p = random.nextInt(3)+1;
            for(int j = 0; i < 5; i++){
                String word = randomWord[random.nextInt(randomWord.length)];
                builder.append(word).append(" ");
            }
            db.insertTask(new Task(builder.toString(), false, p, new Date()));
        }
    }

    public static ArrayList<Task> readCSV(String csv){
        String[] lines = csv.split("\n");
        ArrayList<Task> tasks = new ArrayList<>();
        for(String line: lines){
            try {
                tasks.add(toTask(line));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return tasks;
    }

    public static Task toTask(String text){
        String[] data = text.split(",");
//        int id = Integer.parseInt(data[0]);
        String task = data[1];
        boolean isDone = toBoolean(Integer.parseInt(data[2]));
        int priority = Integer.parseInt(data[3]);
        Date finishedDate = toDate(Long.parseLong(data[4]));
        return new Task(task, isDone, priority, finishedDate);
    }


    public static void ShowNotification(Context context, Task task){
        NotificationChannel notificationChannel;
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent contentIntent;
        contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE);
        NotificationManager mNotificationManager = (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, NOTIFICATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        Notification.Builder NotificationBuilder;

        // check Android API and do as needed
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationBuilder = new Notification.Builder(context, CHANNEL_ID);
        } else {
            NotificationBuilder = new Notification.Builder(context);
        }

        Notification.Builder mBuilder = NotificationBuilder;
        mBuilder.setSmallIcon(R.drawable.ic_alert);
        mBuilder.setContent(getCoustomDesighin(context.getApplicationContext(), task));
        mBuilder.setContentIntent(contentIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID);
        }
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private static RemoteViews getCoustomDesighin(Context context, Task task) {
        RemoteViews remoteViews;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.tv_task, task.getTask());
        remoteViews.setTextViewText(R.id.tv_date, formatDateAndTime(task.getFinishDate()));
        return remoteViews;
    }








}
