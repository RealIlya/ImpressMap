package com.example.impressmap.ui.fragment;

import static com.example.impressmap.ui.fragment.MainFragment.COMMON_MODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.database.firebase.cases.AuthorizationCase;
import com.example.impressmap.databinding.FragmentAuthBinding;
import com.example.impressmap.ui.viewModels.MainViewModel;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.material.snackbar.Snackbar;

public class AuthFragment extends Fragment
{
    private static final int SIGN_IN_TYPE = 0, SIGN_UP_TYPE = 1;

    private FragmentAuthBinding binding;
    private final SuccessCallback successCallback = () ->
    {
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                MainViewModel.class);
        mainViewModel.setMode(COMMON_MODE);

        MainFragment fragment = MainFragment.newInstance();
        requireActivity().getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.container, fragment)
                         .commit();
    };

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
        binding.signUpButton.setOnClickListener(v ->
        {
            String nameText = binding.nameView.getText().toString().trim();
            String surnameText = binding.surnameView.getText().toString().trim();
            String emailText = binding.emailView.getText().toString().trim();
            String passwordText = binding.passwordView.getText().toString().trim();
            if (!nameText.isEmpty() && !surnameText.isEmpty() && !emailText.isEmpty() && !passwordText.isEmpty())
            {
                authorizationCase.signUp(nameText, surnameText, emailText, passwordText, successCallback);
            }
            else
            {
                Snackbar.make(requireView(), R.string.field_is_necessary, Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        binding.nextButton.setOnClickListener(v -> {
            String emailText = binding.emailView.getText().toString().trim();
            String passwordText = binding.passwordView.getText().toString().trim();
            if (!emailText.isEmpty() && !passwordText.isEmpty())
            {
                authorizationCase.signIn(emailText, passwordText, successCallback);
            }
            else
            {
                Snackbar.make(requireView(), R.string.field_is_necessary, Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        binding.signUpCheckBox.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            if (checked)
            {
                binding.registerView.setVisibility(View.VISIBLE);
                binding.nextButton.setVisibility(View.GONE);
                binding.signUpButton.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.registerView.setVisibility(View.GONE);
                binding.nextButton.setVisibility(View.VISIBLE);
                binding.signUpButton.setVisibility(View.GONE);
            }
        });
    }
}
