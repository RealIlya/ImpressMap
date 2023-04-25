package com.example.impressmap.ui.activity;

import static com.example.impressmap.ui.fragment.AuthFragment.EMAIL_KEY;
import static com.example.impressmap.ui.fragment.AuthFragment.PASSWORD_KEY;
import static com.example.impressmap.ui.fragment.MainFragment.COMMON_MODE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.database.firebase.cases.AuthorizationCase;
import com.example.impressmap.databinding.ActivityMainBinding;
import com.example.impressmap.ui.fragment.AuthFragment;
import com.example.impressmap.ui.fragment.MainFragment;
import com.example.impressmap.ui.viewmodel.MainViewModel;


public class MainActivity extends FragmentActivity
{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String emailText = preferences.getString(EMAIL_KEY, "");
        String passwordText = preferences.getString(PASSWORD_KEY, "");

        boolean emailExists = !emailText.isEmpty() && preferences.contains(EMAIL_KEY);
        boolean passwordExists = !passwordText.isEmpty() && preferences.contains(PASSWORD_KEY);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (emailExists && passwordExists)
        {
            AuthorizationCase authorizationCase = new AuthorizationCase();
            authorizationCase.signIn(emailText, passwordText, () ->
            {
                MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
                mainViewModel.setMode(COMMON_MODE);
                transaction.replace(binding.container.getId(), MainFragment.newInstance()).commit();
            });
        }
        else
        {
            transaction.replace(binding.container.getId(), new AuthFragment()).commit();
        }


    }
}