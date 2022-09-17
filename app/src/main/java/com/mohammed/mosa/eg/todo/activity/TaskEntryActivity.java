package com.mohammed.mosa.eg.todo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.ActivityTaskEntryBinding;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;

import java.util.Date;

public class TaskEntryActivity extends AppCompatActivity {
    ActivityTaskEntryBinding binding;
    int priorty = 0;
    TaskDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new TaskDB(this);

        Intent intent = getIntent();
        Task task = (Task) intent.getSerializableExtra("task");
        if(task.getId() != -1){
            binding.etTextEntry.setText(task.getTask());
        }
        binding.spPriortiy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                    priorty = 4;
                else
                    priorty = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.fapSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task.getId() != -1){
                    task.setTask(binding.etTextEntry.getText().toString());
                    task.setFinishDate(new Date());
                    task.setPriority(priorty);
                    db.updateTask(task);
                    finish();
                } else {
                    String text = binding.etTextEntry.getText().toString();
                    Task todo = new Task( text, false, priorty, new Date());
                    if(!text.isEmpty()) {
                        db.insertTask(todo);
                        finish();
                    }
                    else
                        Toast.makeText(TaskEntryActivity.this, getBaseContext().getString(R.string.worring), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_entry_menu, menu);
        return true;
    }
}