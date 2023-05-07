package com.example.impressmap.ui.fragment;

import static com.example.impressmap.ui.fragment.main.MainFragment.COMMON_MODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentAuthBinding;
import com.example.impressmap.ui.fragment.main.MainFragment;
import com.example.impressmap.ui.viewmodel.AuthViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;
import com.example.impressmap.util.FailCallback;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.material.snackbar.Snackbar;

public class AuthFragment extends Fragment
{
    private final SuccessCallback successCallback;
    private FragmentAuthBinding binding;
    private final FailCallback failCallback;
    private AuthViewModel viewModel;

    protected AuthFragment()
    {
        successCallback = () ->
        {
            MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                    MainViewModel.class);
            mainViewModel.setMode(COMMON_MODE);

            MainFragment fragment = MainFragment.newInstance();
            requireActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .setPrimaryNavigationFragment(fragment)
                             .replace(R.id.container, fragment)
                             .commit();
        };

        failCallback = () -> Snackbar.make(requireView(), R.string.field_is_necessary,
                Snackbar.LENGTH_LONG).show();
    }

    @NonNull
    public static AuthFragment newInstance()
    {
        return new AuthFragment();
    }

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
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.signUpButton.setOnClickListener(v ->
        {
            String nameText = binding.nameView.getText().toString().trim();
            String surnameText = binding.surnameView.getText().toString().trim();
            String emailText = binding.emailView.getText().toString().trim();
            String passwordText = binding.passwordView.getText().toString().trim();

            viewModel.signUp(nameText, surnameText, emailText, passwordText,
                    binding.dontLogoutCheckBox.isChecked(), successCallback, failCallback);
        });

        binding.nextButton.setOnClickListener(v ->
        {
            String emailText = binding.emailView.getText().toString().trim();
            String passwordText = binding.passwordView.getText().toString().trim();

            viewModel.signIn(emailText, passwordText, binding.dontLogoutCheckBox.isChecked(),
                    successCallback, failCallback);
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
