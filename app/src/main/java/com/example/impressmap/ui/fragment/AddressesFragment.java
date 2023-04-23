package com.example.impressmap.ui.fragment;

import static com.example.impressmap.ui.fragment.MainFragment.ADDING_MODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.adapter.AddressesAdapter;
import com.example.impressmap.databinding.FragmentAddressesBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.ui.viewModels.AddressesFragmentViewModel;
import com.example.impressmap.ui.viewModels.MainViewModel;

public class AddressesFragment extends Fragment
{
    private FragmentAddressesBinding binding;
    private AddressesFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentAddressesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(AddressesFragmentViewModel.class);

        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                MainViewModel.class);
        binding.addAddressButton.setOnClickListener(v ->
        {
            mainViewModel.setMode(ADDING_MODE);

            FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
            supportFragmentManager.popBackStack(
                    supportFragmentManager.getBackStackEntryAt(0).getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });

        RecyclerView recyclerView = binding.addressesRecyclerView;
        AddressesAdapter addressesAdapter = new AddressesAdapter(getContext());
        recyclerView.setAdapter(addressesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addressesAdapter.setOnAddressClickListener(mainViewModel::switchSelectionAddress);
        viewModel.getByUser().observe(getViewLifecycleOwner(), addressList ->
        {
            for (Address address : mainViewModel.getSelectedAddresses().getValue())
            {
                for (Address addressSub : addressList)
                {
                    if (address.getId().equals(addressSub.getId()))
                    {
                        addressSub.setSelected(true);
                    }
                }
            }

            addressesAdapter.setAddressList(addressList);
        });
    }
}
