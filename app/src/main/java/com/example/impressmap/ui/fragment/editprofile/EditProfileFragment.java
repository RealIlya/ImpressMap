package com.example.impressmap.ui.fragment.editprofile;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentEditProfileBinding;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Contract;

public class EditProfileFragment extends Fragment
{
    private final SuccessCallback successCallback;
    private final FieldEmptyCallback fieldEmptyCallback;
    private FragmentEditProfileBinding binding;
    private EditProfileFragmentViewModel viewModel;

    protected EditProfileFragment()
    {
        successCallback = () -> requireActivity().getSupportFragmentManager().popBackStack();

        fieldEmptyCallback = () -> Snackbar.make(requireView(), R.string.field_is_necessary,
                Snackbar.LENGTH_LONG).show();
    }

    @NonNull
    public static EditProfileFragment newInstance()
    {
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(EditProfileFragmentViewModel.class);

        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.toolbar.setTitle(R.string.change_profile);

        binding.toolbar.addMenuProvider(new MenuProvider()
        {
            @Override
            public void onCreateMenu(@NonNull Menu menu,
                                     @NonNull MenuInflater menuInflater)
            {
                menuInflater.inflate(R.menu.menu_change_profile, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem)
            {
                String nameText = binding.nameView.getText().toString().trim();
                String surnameText = binding.surnameView.getText().toString().trim();

                if (menuItem.getItemId() == R.id.menu_create)
                {
                    viewModel.changeFullName(nameText, surnameText, successCallback,
                            fieldEmptyCallback);
                    return true;
                }

                return false;
            }
        });
    }

    @Nullable
    @Override
    public Object getEnterTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.fade);
    }

    @Nullable
    @Override
    public Object getExitTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.fade);
    }
}
