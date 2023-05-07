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

import com.example.impressmap.adapter.address.AddressesAdapter;
import com.example.impressmap.databinding.FragmentAddressesBinding;
import com.example.impressmap.ui.viewmodel.AddressesFragmentViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;

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
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

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
        AddressesAdapter addressesAdapter = new AddressesAdapter(getContext(), requireActivity());
        recyclerView.setAdapter(addressesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addressesAdapter.setOnAddressClickListener(address ->
        {
            viewModel.setSelectedAddressesCount(addressesAdapter.getSelectedAddressCount());

            mainViewModel.setSelectedAddresses(addressesAdapter.getSelectedAddresses());
        });

        viewModel.setAddressesCount(addressesAdapter.getItemCount());
        viewModel.setSelectedAddressesCount(addressesAdapter.getSelectedAddressCount());

        viewModel.getByUser().observe(getViewLifecycleOwner(), addressList ->
        {
            addressesAdapter.setAddressList(addressList);
            viewModel.setAddressesCount(addressList.size());
            viewModel.setSelectedAddressesCount(addressesAdapter.getSelectedAddressCount());
        });
    }
}
