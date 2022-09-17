package com.mohammed.mosa.eg.todo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.ActivityFinishedTaskBinding;
import com.mohammed.mosa.eg.todo.adapter.TasksRVAdapter;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;

import java.util.ArrayList;
import java.util.Collections;

public class FinishedTaskActivity extends AppCompatActivity {

    ActivityFinishedTaskBinding binding;
    TasksRVAdapter adapter;
    ArrayList<Task> tasks;
    TaskDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinishedTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new TaskDB(this);
        tasks = db.getAllFinishedTask();
        binding.fapClear.setOnClickListener(k -> alert(getBaseContext().getString(R.string.delete_finished_tasks)));
        adapter = new TasksRVAdapter(tasks, new TasksRVAdapter.TaskOnclickListener() {
            @Override
            public void onClick(Task task) {
                db.deleteTask(task);
                adapter.setTasks(db.getAllFinishedTask());
                adapter.notifyDataSetChanged();
            }
        }, this);
        binding.rvMainFinished.setAdapter(adapter);
        binding.rvMainFinished.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMainFinished.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks = db.getAllFinishedTask();
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

    private void alert(String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage( this.getString(R.string.conform_finished));
        dialog.setPositiveButton(this.getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteAllFinishedTasks();
                tasks = db.getAllUnfinishedTasks();
                adapter.setTasks(tasks);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton(this.getString(R.string.cancel), null);
        dialog.show();

    }


}