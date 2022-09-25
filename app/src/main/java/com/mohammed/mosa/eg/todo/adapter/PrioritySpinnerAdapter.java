package com.mohammed.mosa.eg.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohammed.mosa.eg.task.R;
import com.mohammed.mosa.eg.task.databinding.SpinnerRowBinding;
import com.mohammed.mosa.eg.todo.util.Priority;

import java.util.ArrayList;

public class PrioritySpinnerAdapter extends ArrayAdapter<Priority> {

    Context context;
    ArrayList<Priority> priorities;
    int resource;

    public PrioritySpinnerAdapter(@NonNull Context context, @NonNull ArrayList<Priority> objects) {
        super(context, R.layout.spinner_row, objects);
        this.context = context;
        this.priorities = objects;
        this.resource = R.layout.spinner_row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(resource, parent, false);
        SpinnerRowBinding binding = SpinnerRowBinding.bind(v);
        binding.viPriorityColor.setBackground(priorities.get(position).getColorDrawable());
        binding.tvPriority.setText(priorities.get(position).getText());
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
