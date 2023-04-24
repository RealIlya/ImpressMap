package com.example.impressmap.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentProfileBinding;
import com.example.impressmap.model.data.User;
import com.example.impressmap.ui.viewModels.ProfileFragmentViewModel;

public class ProfileFragment extends Fragment
{
    private FragmentProfileBinding binding;

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
        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.settingsAddressesView.setOnClickListener(v ->
        {
            String name = AddressesFragment.class.getSimpleName();
            requireActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .replace(R.id.container, new AddressesFragment())
                             .addToBackStack(name)
                             .commit();
        });

        ProfileFragmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(
                ProfileFragmentViewModel.class);
        LiveData<User> userLiveData = viewModel.getUser();
        if (!userLiveData.hasObservers())
        {
            userLiveData.observe(requireActivity(), user ->
            {
                binding.collapsingToolbar.setTitle(user.getFullName());
                binding.phoneNumberView.setText(user.getPhoneNumber() != null ? user.getPhoneNumber()
                                                                              : getText(
                                                                                      R.string.phone_number_not_set));
            });
        }
    }
}
