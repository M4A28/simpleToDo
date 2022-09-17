package com.mohammed.mosa.eg.todo.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.TaskItemBinding;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.util.ArrayList;
import java.util.Collections;

public class TasksRVAdapter extends RecyclerView.Adapter<TasksRVAdapter.TaskHolder> {

    ArrayList<Task> tasks;
    TaskOnclickListener listener;
    Context context;
    TaskDB db;


    public interface TaskOnclickListener{
        void onClick(Task task);
    }

    public TasksRVAdapter(ArrayList<Task> tasks, TaskOnclickListener listener, Context context) {
        Collections.sort(tasks);
        this.tasks = tasks;
        this.listener = listener;
        this.context = context;
    }

    public void setTasks(ArrayList<Task> tasks) {
        Collections.sort(tasks);
        this.tasks = tasks;
    }

    public void setListener(TaskOnclickListener listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder{
        TaskItemBinding binding;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            binding = TaskItemBinding.bind(itemView);
            db = new TaskDB(context);
        }

        public void bind(Task task){
            binding.tvDate.setText(Utility.formatDate(task.getFinishDate()));
            binding.viPriority.setBackground(Utility.colorPriority(task.getPriority()));
            binding.tvTask.setText(Utility.fitString(task.getTask(), 30));
            binding.lyMain.setOnClickListener(k -> listener.onClick(task));

            if(!task.isDone()){
                binding.cbTaskComplet.setChecked(false);
                binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
            }else {
                binding.cbTaskComplet.setChecked(true);
                binding.cbTaskComplet.setChecked(true);
                binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#DDDDDD")));
            }


         binding.cbTaskComplet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#DDDDDD")));
                        Log.d("result", db.makeTaskComplete(task)+"");

                    }
                    else if(b == false) {
                        binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                        db.makeTaskUnComplete(task);
                        Log.d("result", db.makeTaskComplete(task)+"");

                    }


                }
            });
        }
    }
}
