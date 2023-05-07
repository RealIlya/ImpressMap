package com.example.impressmap.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.ActivityMainBinding;
import com.example.impressmap.ui.fragment.AuthFragment;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.AuthViewModel;


public class MainActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.Theme_ImpressMap);
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        authViewModel.tryToSignIn(
                () -> transaction.replace(R.id.container, MainFragment.newInstance()).commit(),
                () -> transaction.replace(R.id.container, AuthFragment.newInstance()).commit());
    }
}