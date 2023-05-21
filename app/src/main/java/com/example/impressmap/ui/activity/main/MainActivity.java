package com.example.impressmap.ui.activity.main;

import android.os.Bundle;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.ActivityMainBinding;
import com.example.impressmap.ui.activity.AuthViewModel;
import com.example.impressmap.ui.fragment.auth.AuthFragment;
import com.example.impressmap.ui.fragment.main.MainFragment;


public class MainActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.Theme_ImpressMap);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

/*        binding.container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/

        setContentView(binding.getRoot());

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        authViewModel.tryToSignIn(
                () -> transaction.replace(R.id.container, MainFragment.newInstance()).commit(),
                () -> transaction.replace(R.id.container, AuthFragment.newInstance()).commit());
    }
}