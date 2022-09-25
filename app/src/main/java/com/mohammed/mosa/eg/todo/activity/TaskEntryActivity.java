package com.mohammed.mosa.eg.todo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.ActivityTaskEntryBinding;
import com.mohammed.mosa.eg.todo.adapter.PrioritySpinnerAdapter;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Priority;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskEntryActivity extends AppCompatActivity {
    ActivityTaskEntryBinding binding;
    int priority;
    TaskDB db;
    Calendar dateAndTime;
    Calendar copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = TaskDB.getInstance(this);
        dateAndTime = Calendar.getInstance();
        copy = (Calendar) dateAndTime.clone();

        Intent intent = getIntent();
        Task task = (Task) intent.getSerializableExtra(Utility.TASK);
        initDateAndTime(task);

        PrioritySpinnerAdapter spinnerAdapter = new PrioritySpinnerAdapter(this, prioritiesData());
        binding.spPriortiy.setAdapter(spinnerAdapter);
        binding.spPriortiy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                    priority = 4;
                else
                    priority = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.fapSave.setOnClickListener(view -> {
            if(task.getId() != -1){
                task.setTask(binding.etTextEntry.getText().toString().trim());
                task.setPriority(priority);
                if(copy.equals(dateAndTime))
                    task.setFinishDate(task.getFinishDate());
                else
                    task.setFinishDate(dateAndTime.getTime());
                db.updateTask(task);
                finish();
            } else {
                String text = binding.etTextEntry.getText().toString().trim();
                Task todo = new Task( text, false, priority, dateAndTime.getTime());
                if(!text.isEmpty()) {
                    db.insertTask(todo);
                    finish();
                }
                else
                    Toast.makeText(TaskEntryActivity.this, getBaseContext().getString(R.string.worring), Toast.LENGTH_SHORT).show();
            }
        });
        DatePickerDialog.OnDateSetListener dateListener = (datePicker, year, month, day_of_month) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, day_of_month);
            if(Utility.isDateInFuture(dateAndTime.getTime())){
                Toast.makeText(TaskEntryActivity.this, getString(R.string.time_travsl), Toast.LENGTH_LONG).show();
            }
            else
                binding.btnDate.setText(Utility.formatDate(dateAndTime.getTime()));
        };
        binding.btnDate.setOnClickListener(view -> {
            int year = dateAndTime.get(Calendar.YEAR);
            int month = dateAndTime.get(Calendar.MONTH);
            int day = dateAndTime.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                    dateListener,
                    year, month, day);
            datePickerDialog.show();
        });
        TimePickerDialog.OnTimeSetListener timeListener = (timePicker, hour, minute) -> {
            dateAndTime.set(Calendar.HOUR, hour);
            dateAndTime.set(Calendar.MINUTE, minute);
            binding.btnTime.setText(Utility.formatTime(dateAndTime.getTime()));
        };
        binding.btnTime.setOnClickListener(view -> {
            int hour = dateAndTime.get(Calendar.HOUR);
            int mint = dateAndTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                    timeListener, hour, mint, true);
            timePickerDialog.show();
        });
    }

    private void initDateAndTime(Task task){
        if(task.getId() != -1){
            binding.etTextEntry.setText(task.getTask());
            binding.btnTime.setText(Utility.formatTime(task.getFinishDate()));
            binding.btnDate.setText(Utility.formatDate(task.getFinishDate()));
        }else{
            binding.btnTime.setText(Utility.formatTime(dateAndTime.getTime()));
            binding.btnDate.setText(Utility.formatDate(dateAndTime.getTime()));
        }
    }

    private ArrayList<Priority> prioritiesData(){
        ArrayList<Priority> priorities = new ArrayList<>();
        priorities.add(new Priority(new ColorDrawable(Color.parseColor("#2196F3")), getString(R.string.priority)));
        priorities.add(new Priority(new ColorDrawable(Color.parseColor("#ff0000")), getString(R.string.high)));
        priorities.add(new Priority(new ColorDrawable(Color.parseColor("#ffcc00")), getString(R.string.medium)));
        priorities.add(new Priority(new ColorDrawable(Color.parseColor("#37c837")), getString(R.string.low)));
        return priorities;
    }
}