package com.mohammed.mosa.eg.todo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mohammed.mosa.eg.task.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}