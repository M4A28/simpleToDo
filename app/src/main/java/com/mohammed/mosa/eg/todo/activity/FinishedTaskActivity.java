package com.mohammed.mosa.eg.todo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.ActivityFinishedTaskBinding;
import com.mohammed.mosa.eg.todo.adapter.TasksRVAdapter;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.util.ArrayList;
import java.util.List;

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
        db = TaskDB.getInstance(this);
        tasks = db.getAllFinishedTask();
        adapter = new TasksRVAdapter(tasks, new TasksRVAdapter.TaskOnclickListener() {
            @Override
            public void onClick(Task task) {

            }
        }, this);
        binding.rvMainFinished.setAdapter(adapter);
        binding.rvMainFinished.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMainFinished.setHasFixedSize(true);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task data = tasks.remove(position);
                showTasks();
                db.deleteTask(data);
                adapter.notifyDataSetChanged();
                Snackbar.make(binding.rvMainFinished, getString(R.string.deleting_task), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), view -> {
                            tasks.add(position, data);
                            db.insertTask(data);
                            adapter.notifyDataSetChanged();
                            showTasks();
                        }).show();
            }
        }).attachToRecyclerView(binding.rvMainFinished);
        showTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks = db.getAllFinishedTask();
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
        showTasks();
    }

    private void clearFinishedTaskAlert(String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage( this.getString(R.string.conform_finished));
        dialog.setPositiveButton(this.getString(R.string.delete), (dialogInterface, i) -> {
            Utility.playSound("trash_empty.ogg", getBaseContext());
            db.deleteAllFinishedTasks();
            tasks = db.getAllUnfinishedTasks();
            adapter.setTasks(tasks);
            adapter.notifyDataSetChanged();
            finish();
        });
        dialog.setNegativeButton(this.getString(R.string.cancel), null);

        if(tasks.size() > 0)
            dialog.show();
        else
            Toast.makeText(this, getString(R.string.embty_list), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finished_tasks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_delete:
                clearFinishedTaskAlert(getBaseContext().getString(R.string.delete_finished_tasks));
                break;
            case 5:
                int a = 4;
                break;
        }
        return false;
    }


    private void showTasks(){
        binding.emptyList.ivListType.setImageDrawable(getDrawable(R.drawable.ic_cheked_list));
        if(tasks == null || tasks.size() == 0){
            binding.rvMainFinished.setVisibility(View.GONE);
            binding.emptyList.lyMain.setVisibility(View.VISIBLE);
        } else {
            binding.rvMainFinished.setVisibility(View.VISIBLE);
            binding.emptyList.lyMain.setVisibility(View.GONE);
        }

    }
}