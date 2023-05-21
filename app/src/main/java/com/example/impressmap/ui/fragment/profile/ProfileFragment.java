package com.example.impressmap.ui.fragment.profile;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentProfileBinding;
import com.example.impressmap.model.data.User;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.addresses.UserAddressesFragment;
import com.example.impressmap.ui.fragment.auth.AuthFragment;
import com.example.impressmap.ui.fragment.editprofile.EditProfileFragment;

public class ProfileFragment extends Fragment
{
    private FragmentProfileBinding binding;
    private ProfileFragmentViewModel viewModel;

    protected ProfileFragment()
    {
    }

    @NonNull
    public static ProfileFragment newInstance()
    {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);

        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                MainViewModel.class);
        binding.toolbar.getMenu().findItem(R.id.logout_item).setOnMenuItemClickListener(menuItem ->
        {
            viewModel.signOut(() ->
            {
                mainViewModel.clearCache();
                requireActivity().getSupportFragmentManager()
                                 .beginTransaction()
                                 .replace(R.id.container, AuthFragment.newInstance())
                                 .commit();
            });
            return true;
        });

        binding.settingsAddressesView.setOnClickListener(v ->
        {
            String name = UserAddressesFragment.class.getSimpleName();
            requireActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.container, UserAddressesFragment.newInstance())
                             .addToBackStack(name)
                             .commit();
        });

        binding.editProfileFab.setOnClickListener(v ->
        {
            String name = EditProfileFragment.class.getSimpleName();
            requireActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.container, EditProfileFragment.newInstance())
                             .addToBackStack(name)
                             .commit();
        });

        User user = mainViewModel.getUser();
        binding.collapsingToolbar.setTitle(user.getFullName());
        binding.phoneNumberView.setText(user.getPhoneNumber() != null ? user.getPhoneNumber()
                                                                      : getText(
                                                                              R.string.phone_number_not_set));

        viewModel.getUser().observe(getViewLifecycleOwner(), newUser ->
        {
            mainViewModel.setUser(newUser);

            binding.collapsingToolbar.setTitle(newUser.getFullName());
            binding.phoneNumberView.setText(
                    newUser.getPhoneNumber() != null ? newUser.getPhoneNumber()
                                                     : getText(R.string.phone_number_not_set));
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
