package com.example.impressmap.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.impressmap.databinding.ActivityMainBinding;
import com.example.impressmap.ui.fragment.AuthFragment;
import com.example.impressmap.ui.fragment.MainFragment;


public class MainActivity extends FragmentActivity
{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                                   .replace(binding.container.getId(), new AuthFragment())
                                   .commit();
    }
}