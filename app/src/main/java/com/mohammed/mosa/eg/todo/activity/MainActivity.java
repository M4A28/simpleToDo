package com.mohammed.mosa.eg.todo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.ActivityMainBinding;
import com.mohammed.mosa.eg.todo.adapter.TasksRVAdapter;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.fragment.ExportFragment;
import com.mohammed.mosa.eg.todo.service.RemianderService;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    TasksRVAdapter adapter;
    ArrayList<Task> tasks;
    TaskDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utility.verifyPermissions(this, getBaseContext());
        startService(new Intent(this, RemianderService.class));
        db = TaskDB.getInstance(this);
        tasks = db.getAllUnfinishedTasks();
        showTasks();
        adapter = new TasksRVAdapter(tasks, task -> {
            Intent intent = new Intent(getBaseContext(), TaskEntryActivity.class);
            intent.putExtra(Utility.TASK, task);
            startActivity(intent);
        }, this);

        binding.fapAdd.setOnClickListener(K -> {
            Intent intent = new Intent(MainActivity.this, TaskEntryActivity.class);
            Task task = new Task();
            task.setId(-1);
            intent.putExtra(Utility.TASK, task);
            startActivity(intent);
        });
        binding.rvMainTasks.setAdapter(adapter);
        binding.rvMainTasks.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMainTasks.setHasFixedSize(true);
        
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
                Snackbar.make(binding.rvMainTasks, getString(R.string.deleting_task), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.undo), view -> {
                            tasks.add(position, data);
                            db.insertTask(data);
                            adapter.notifyDataSetChanged();
                            showTasks();
                        }).show();
            }
        }).attachToRecyclerView(binding.rvMainTasks);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.delete_all_task:
                ClearTasksAlert(this.getString(R.string.delete_all_tasks));
                break;
            case R.id.finished_task:
                 intent = new Intent(getBaseContext(), FinishedTaskActivity.class);
                startActivity(intent);
                break;
            case R.id.export:
                ExportFragment exportFragment = new ExportFragment();
                exportFragment.show(getSupportFragmentManager(), null);
                break;
            case R.id.about:
                intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tasks = db.getAllUnfinishedTasks();
        adapter.setTasks(tasks);
        adapter.notifyDataSetChanged();
        showTasks();
    }

    private void ClearTasksAlert(String title){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(this.getString(R.string.conform));
        dialog.setPositiveButton(this.getString(R.string.delete), (dialogInterface, i) -> {
            Utility.playSound("trash_empty.ogg", getBaseContext());
            tasks.clear();
            db.deleteAllTasks();
            adapter.notifyDataSetChanged();
        });
        dialog.setNegativeButton(this.getString(R.string.cancel), null);
        if(tasks.size() > 0)
            dialog.show();
        else
            Toast.makeText(this, getString(R.string.embty_list), Toast.LENGTH_SHORT).show();
    }


    private void showTasks(){
        if(tasks == null || tasks.size() == 0){
            binding.rvMainTasks.setVisibility(View.GONE);
            binding.emptyList.lyMain.setVisibility(View.VISIBLE);

        } else {
            binding.rvMainTasks.setVisibility(View.VISIBLE);
            binding.emptyList.lyMain.setVisibility(View.GONE);
        }
    }


}