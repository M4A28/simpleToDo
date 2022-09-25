package com.mohammed.mosa.eg.todo.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.widget.Toast;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.FragmentExportBinding;
import com.mohammed.mosa.eg.todo.database.TaskDB;
import com.mohammed.mosa.eg.todo.util.Task;
import com.mohammed.mosa.eg.todo.util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class ExportFragment extends DialogFragment {
    FragmentExportBinding binding;
    TaskDB db;

    public ExportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentExportBinding.inflate(getActivity().getLayoutInflater());
        db = TaskDB.getInstance(getContext());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.export_to)).setView(binding.getRoot());
        alert.setPositiveButton(getString(R.string.export), (dialogInterface, i) -> {
            switch (binding.rgExport.getCheckedRadioButtonId()){
                case R.id.rb_csv_export:
                    exportToCSV();
                    break;
                case R.id.rb_txt_export:
                    exportToTxT();
                    break;
                default:
                    Toast.makeText(getContext(), getString(R.string.choice_option), Toast.LENGTH_LONG).show();
                    break;
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), null);
        return alert.create();
    }

    public void exportToTxT() {
        Utility.verifyPermissions(getActivity(), getContext());
        ArrayList<Task> tasks = db.getAllTasks();
        File dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + "/" + "Exported" + "/");
        String exportedTasksFile = "exported_tasks_" + Utility.formatDateAndTime(new Date()) + ".txt";
        File file = new File(dir, exportedTasksFile);

        if (!dir.exists())
            dir.mkdirs();

        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            int counter = 1;
            String state;
            for (Task t : tasks) {
                state = t.isDone()? getString(R.string.finish):getString(R.string.not_finish);
                printWriter.println(counter + "- " + t.getTask()+ " # " + state);
                counter++;
            }
            printWriter.close();
            fileOutputStream.close();
            Toast.makeText(getContext(), getString(R.string.task_backup) + file.getParent(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportToCSV(){
        Utility.verifyPermissions(getActivity(), getContext());
        ArrayList<Task> tasks = db.getAllTasks();
        File dir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + "/" + "BackUp" + "/");
        String backupFile = "backup_tasks_" + Utility.formatDateAndTime(new Date())+".csv";
        File file = new File(dir, backupFile);

        if(!dir.exists())
            dir.mkdirs();

        try {
            file.createNewFile();
            FileOutputStream fileOutputStream =  new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println("id,task,isDone,priority,createdDate");
            for(Task t: tasks){
                printWriter.println(t.toCSV());
            }
            printWriter.close();
            fileOutputStream.close();
            Toast.makeText(getContext(), getString(R.string.task_backup) + file.getParent(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}