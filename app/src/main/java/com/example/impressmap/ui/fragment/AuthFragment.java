package com.example.impressmap.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.impressmap.R;
import com.example.impressmap.database.firebase.cases.AuthorizationCase;
import com.example.impressmap.databinding.FragmentAuthBinding;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.material.snackbar.Snackbar;

public class AuthFragment extends Fragment
{
    private static final int SIGN_IN_TYPE = 0, SIGN_UP_TYPE = 1;

    private FragmentAuthBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        AuthorizationCase authorizationCase = new AuthorizationCase();
        binding.nextButton.setOnClickListener(v -> userWay(authorizationCase, SIGN_IN_TYPE));

        binding.signUpButton.setOnClickListener(v -> userWay(authorizationCase, SIGN_UP_TYPE));

        binding.signUpCheckBox.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            if (checked)
            {
                binding.nextButton.setVisibility(View.GONE);
                binding.signUpButton.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.nextButton.setVisibility(View.VISIBLE);
                binding.signUpButton.setVisibility(View.GONE);
            }
        });
    }

    private void userWay(AuthorizationCase authorizationCase,
                         int type)
    {
        String loginText = String.valueOf(binding.loginView.getText());
        String passwordText = String.valueOf(binding.passwordView.getText());
        if (!loginText.isEmpty() && !passwordText.isEmpty())
        {
            SuccessCallback successCallback = () -> requireActivity().getSupportFragmentManager()
                                                                     .beginTransaction()
                                                                     .replace(R.id.container,
                                                                             new MainFragment())
                                                                     .commit();
            switch (type)
            {
                case SIGN_IN_TYPE:
                    authorizationCase.signIn(loginText, passwordText, successCallback);
                    break;
                case SIGN_UP_TYPE:
                    authorizationCase.signUp(loginText, passwordText, successCallback);
                    break;
            }
        }
        else
        {
            Snackbar.make(requireView(), R.string.field_is_necessary, Snackbar.LENGTH_LONG).show();
        }
    }
}
