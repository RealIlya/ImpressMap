package com.example.impressmap.ui.fragment.addresses;

import static com.example.impressmap.ui.fragment.main.MainFragment.ADDING_MODE;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.address.AddressesAdapter;
import com.example.impressmap.databinding.FragmentAddressesBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

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

        addressesAdapter.setOnAddressClickListener(new AddressesAdapter.OnAddressClickListener()
        {
            @Override
            public void onAddressClick(Address address)
            {
                viewModel.setSelectedAddressesCount(addressesAdapter.getSelectedAddressCount());
                mainViewModel.setSelectedAddresses(addressesAdapter.getSelectedAddresses());
            }

            @Override
            public void onMaxAddresses()
            {
                Snackbar.make(view, R.string.max_addresses_count, Snackbar.LENGTH_LONG).show();
            }
        });

        viewModel.setAddressesCount(addressesAdapter.getItemCount());
        viewModel.setSelectedAddressesCount(addressesAdapter.getSelectedAddressCount());

        LiveData<List<Address>> byUser = viewModel.getByUser();
        if (!byUser.hasActiveObservers())
        {
            byUser.observeForever(addressList ->
            {
                addressesAdapter.setAddresses(addressList);
                viewModel.setAddressesCount(addressList.size());
                viewModel.setSelectedAddressesCount(addressesAdapter.getSelectedAddressCount());
            });
        }
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
