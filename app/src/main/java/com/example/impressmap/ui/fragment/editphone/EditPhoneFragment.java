package com.example.impressmap.ui.fragment.editphone;

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
import com.example.impressmap.databinding.FragmentEditPhoneBinding;
import com.example.impressmap.model.data.User;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicReference;

public class EditPhoneFragment extends Fragment
{
    private final SuccessCallback successCallback = () -> requireActivity().getSupportFragmentManager()
                                                                           .popBackStack();
    private final FieldEmptyCallback fieldEmptyCallback = () -> Snackbar.make(requireView(),
            R.string.field_is_necessary, Snackbar.LENGTH_LONG).show();
    private FragmentEditPhoneBinding binding;

    @NonNull
    public static EditPhoneFragment newInstance()
    {
        return new EditPhoneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        new WindowInsetsControllerCompat(requireActivity().getWindow(),
                requireActivity().getWindow().getDecorView()).setAppearanceLightStatusBars(false);
        binding = FragmentEditPhoneBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.toolbar.setTitle(R.string.edit_phone_number);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                MainViewModel.class);

        AtomicReference<User> userAtomic = new AtomicReference<>();
        mainViewModel.getUser().observe(getViewLifecycleOwner(), user ->
        {
            userAtomic.set(user);
            binding.phoneView.setText(userAtomic.get().getPhoneNumber());
        });

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
                String phoneText = binding.phoneView.getText().toString().trim();

                if (menuItem.getItemId() == R.id.menu_create)
                {
                    if (phoneText.isEmpty())
                    {
                        fieldEmptyCallback.onEmpty();
                    }
                    else
                    {
                        userAtomic.get().setPhoneNumber(phoneText);
                        mainViewModel.setUser(userAtomic.get(), successCallback);
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
