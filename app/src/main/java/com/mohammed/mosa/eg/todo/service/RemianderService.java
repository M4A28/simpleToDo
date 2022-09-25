package com.mohammed.mosa.eg.todo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;

public class RemianderService extends Service {
    public RemianderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Calendar calendar = Calendar.getInstance();
        TaskDB db = TaskDB.getInstance(getApplicationContext());
        ArrayList<Task> tasks = db.getAllUnfinishedTasks();
        for(Task t: tasks){
            if(calendar.getTimeInMillis() == t.getFinishDate().getTime()){
                // send notification
                Utility.ShowNotification(getApplicationContext(), t);
            }
        }




    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}