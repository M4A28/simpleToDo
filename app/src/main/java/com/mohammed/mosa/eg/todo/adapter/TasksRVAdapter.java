package com.mohammed.mosa.eg.todo.adapter;


import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.TaskItemBinding;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class TasksRVAdapter extends RecyclerView.Adapter<TasksRVAdapter.TaskHolder> implements Serializable {

    public interface TaskOnclickListener{
        void onClick(Task task);
    }

    ArrayList<Task> tasks;
    TaskOnclickListener listener;
    Context context;
    TaskDB db;

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

            binding.tvDate.setText(Utility.formatDateAndTime(task.getFinishDate()));
            binding.viPriority.setBackground(Utility.colorPriority(task.getPriority()));
            binding.tvTask.setText(Utility.fitString(task.getTask(), 25));
            binding.lyMain.setOnClickListener(k -> listener.onClick(task));
            binding.lyMain.setOnLongClickListener(view -> {
                // copy text
                copyTextToClipboard(task.getTask());
                Toast.makeText(context, context.getString(R.string.copy_to_clipvoard), Toast.LENGTH_SHORT).show();
                return true;
            });

            if(!task.isDone()){
                binding.cbTaskComplet.setChecked(false);
                binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
            }else {
                binding.cbTaskComplet.setChecked(true);
                binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#DDDDDD")));
            }

         binding.cbTaskComplet.setOnCheckedChangeListener((compoundButton, b) -> {
             if(b) {
                 binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#DDDDDD")));
                 db.makeTaskComplete(task);

             }
             else if(!b) {
                 binding.cvMainCard.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
                 db.makeTaskUnComplete(task);

             }
         });
        }
    }

    private void copyTextToClipboard(String data){
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("data coped", data);
        clipboardManager.setPrimaryClip(clipData);
    }
}
