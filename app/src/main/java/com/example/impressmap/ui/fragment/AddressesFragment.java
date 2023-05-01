package com.example.impressmap.ui.fragment;

import static com.example.impressmap.ui.fragment.main.MainFragment.ADDING_MODE;

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

import com.example.impressmap.R;
import com.example.impressmap.adapter.AddressesAdapter;
import com.example.impressmap.databinding.FragmentAddressesBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.ui.viewmodel.AddressesFragmentViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

public class AddressesFragment extends Fragment
{
    private FragmentAddressesBinding binding;
    private AddressesFragmentViewModel viewModel;

    protected AddressesFragment()
    {
    }

    @NonNull
    public static AddressesFragment newInstance()
    {
        return new AddressesFragment();
    }

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
        addressesAdapter.setOnAddressClickListener(address ->
        {
            mainViewModel.switchSelectionAddress(address,
                    () -> Snackbar.make(requireView(), R.string.max_addresses_count,
                            Snackbar.LENGTH_LONG).show());
        });

        viewModel.getByUser().observe(getViewLifecycleOwner(), addresses ->
        {
            mainViewModel.setAddresses(addresses);

            mainViewModel.getAddresses().observe(getViewLifecycleOwner(), addressList ->
            {
                int count = 0;
                for (Address address : mainViewModel.getSelectedAddresses().getValue())
                {
                    for (Address addressSub : addressList)
                    {
                        if (address.getId().equals(addressSub.getId()))
                        {
                            addressSub.setSelected(true);
                            count++;
                        }
                    }
                }

                addressesAdapter.setAddressList(addressList);
                binding.toolbar.setTitle("Addresses " + addressList.size());
                binding.toolbar.setSubtitle(count + " selected");
            });
        });

        mainViewModel.getSelectedAddresses().observe(getViewLifecycleOwner(), addressList ->
        {
            binding.toolbar.setSubtitle(addressList.size() + " selected");
        });
    }
}
