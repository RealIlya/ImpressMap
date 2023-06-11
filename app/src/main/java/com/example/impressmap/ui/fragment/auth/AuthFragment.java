package com.example.impressmap.ui.fragment.auth;

import static com.example.impressmap.ui.fragment.map.MapFragment.COMMON_MODE;

import android.content.res.Configuration;
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
import com.example.impressmap.ui.activity.AuthViewModel;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.map.MapFragment;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;
import com.example.impressmap.util.ViewVisibility;
import com.example.impressmap.util.WindowStatusBar;
import com.google.android.material.snackbar.Snackbar;

public class AuthFragment extends Fragment
{
    private final SuccessCallback successCallback = () ->
    {
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                MainViewModel.class);
        mainViewModel.setMode(COMMON_MODE);

        MapFragment fragment = MapFragment.newInstance();
        requireActivity().getSupportFragmentManager()
                         .beginTransaction()
                         .replace(R.id.container, fragment)
                         .commit();
    };
    private final FieldEmptyCallback fieldEmptyCallback = () -> Snackbar.make(requireView(),
            R.string.field_is_necessary, Snackbar.LENGTH_LONG).show();
    private FragmentAuthBinding binding;
    private AuthViewModel viewModel;

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
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        WindowStatusBar.setLight(requireActivity().getWindow(),
                currentNightMode == Configuration.UI_MODE_NIGHT_NO);
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
                    binding.dontLogoutCheckBox.isChecked(), successCallback, () ->
                    {
                    }, fieldEmptyCallback);
        });

        binding.nextButton.setOnClickListener(v ->
        {
            String emailText = binding.emailView.getText().toString().trim();
            String passwordText = binding.passwordView.getText().toString().trim();

            viewModel.signIn(emailText, passwordText, binding.dontLogoutCheckBox.isChecked(),
                    successCallback, () ->
                    {
                    }, fieldEmptyCallback);
        });

        binding.signUpCheckBox.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            binding.registerView.setVisibility(ViewVisibility.show(checked));
            binding.nextButton.setVisibility(ViewVisibility.show(!checked));
            binding.signUpButton.setVisibility(ViewVisibility.show(checked));
        });
    }
}
