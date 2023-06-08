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
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentEditProfileBinding;
import com.example.impressmap.model.data.User;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.material.snackbar.Snackbar;

public class EditProfileFragment extends Fragment
{
    private final SuccessCallback successCallback = () -> requireActivity().getSupportFragmentManager()
                                                                           .popBackStack();
    private final FieldEmptyCallback fieldEmptyCallback = () -> Snackbar.make(requireView(),
            R.string.field_is_necessary, Snackbar.LENGTH_LONG).show();
    private FragmentEditProfileBinding binding;
    private EditProfileFragmentViewModel viewModel;

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
        new WindowInsetsControllerCompat(requireActivity().getWindow(),
                requireActivity().getWindow().getDecorView()).setAppearanceLightStatusBars(false);
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
        binding.toolbar.setTitle(R.string.edit_profile);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                MainViewModel.class);

        User user = mainViewModel.getUser();
        binding.nameView.setText(user.getName());
        binding.surnameView.setText(user.getSurname());

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
                    if (nameText.isEmpty())
                    {
                        fieldEmptyCallback.onEmpty();
                    }
                    else
                    {
                        user.setFullName(nameText + " " + surnameText);
                        viewModel.update(user, successCallback);
                    }
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
