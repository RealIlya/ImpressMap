package com.example.impressmap.ui.fragment.addresses;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.addresses.AddressesAdapter;
import com.example.impressmap.databinding.FragmentAddressesBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.util.WindowStatusBar;

import java.util.List;

public class AddressesFragment extends Fragment
{
    private FragmentAddressesBinding binding;
    private AddressesFragmentViewModel viewModel;

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
        WindowStatusBar.setLight(requireActivity().getWindow(), false);
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
        binding.toolbar.setTitle(R.string.addresses);

        RecyclerView recyclerView = binding.addressesRecyclerView;
        AddressesAdapter addressesAdapter = new AddressesAdapter(requireContext(),
                requireActivity());
        recyclerView.setAdapter(addressesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addressesAdapter.setOnJoinToAddressButtonClickListener(address ->
        {
            viewModel.join(address, () ->
            {
            });
        });

        LiveData<List<Address>> addresses = viewModel.getAll();
        if (!addresses.hasActiveObservers())
        {
            addresses.observeForever(addressesAdapter::setAddressList);
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
