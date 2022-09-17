package com.mohammed.mosa.eg.todo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.ActivityMainBinding;
import com.mohammed.mosa.eg.todo.adapter.TasksRVAdapter;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TasksRVAdapter adapter;
    ArrayList<Task> tasks;
    TaskDB db;

    // made wtih love from mohammed mosa
    // mohammed.mosa.eg@gmail.com
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new TaskDB(this);
        Toast.makeText(this, "Made with love from Mohammed Mosa", Toast.LENGTH_SHORT).show();
        binding.fapAdd.setOnClickListener(K -> {
            Intent intent = new Intent(MainActivity.this, TaskEntryActivity.class);
            Task task = new Task();
            task.setId(-1);
            intent.putExtra("task", task);
            startActivity(intent);
        });
        tasks = db.getAllUnfinishedTasks();
        adapter = new TasksRVAdapter(tasks, new TasksRVAdapter.TaskOnclickListener() {
            @Override
            public void onClick(Task task) {
                Intent intent = new Intent(getBaseContext(), TaskEntryActivity.class);
                intent.putExtra("task", task);
                startActivity(intent);
            }
        }, this);
        binding.rvMainTasks.setAdapter(adapter);
        binding.rvMainTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMainTasks.setHasFixedSize(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks = db.getAllUnfinishedTasks();
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_task:
               alert(this.getString(R.string.delete_all_tasks));
                break;
            case R.id.finished_task:
                Intent intent = new Intent(getBaseContext(), FinishedTaskActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void alert(String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(this.getString(R.string.conform));
        dialog.setPositiveButton(this.getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteAllTasks();
                tasks = db.getAllUnfinishedTasks();
                adapter.setTasks(tasks);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton(this.getString(R.string.cancel), null);
        dialog.show();

    }
}