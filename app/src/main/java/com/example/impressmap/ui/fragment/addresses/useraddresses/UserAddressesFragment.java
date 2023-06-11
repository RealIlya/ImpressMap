package com.example.impressmap.ui.fragment.addresses.useraddresses;

import static com.example.impressmap.ui.fragment.map.MapFragment.ADDING_MODE;

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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.addresses.useraddresses.UserAddressesAdapter;
import com.example.impressmap.adapter.addresses.useraddresses.UserAddressCallback;
import com.example.impressmap.databinding.FragmentUserAddressesBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.addresses.AddressesFragment;
import com.example.impressmap.util.WindowStatusBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class UserAddressesFragment extends Fragment
{
    private FragmentUserAddressesBinding binding;
    private UserAddressesFragmentViewModel viewModel;

    @NonNull
    public static UserAddressesFragment newInstance()
    {
        return new UserAddressesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        WindowStatusBar.setLight(requireActivity().getWindow(), false);
        binding = FragmentUserAddressesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(UserAddressesFragmentViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

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

        binding.toolbar.addMenuProvider(new MenuProvider()
        {
            @Override
            public void onCreateMenu(@NonNull Menu menu,
                                     @NonNull MenuInflater menuInflater)
            {
                menuInflater.inflate(R.menu.menu_user_addresses, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem)
            {
                if (menuItem.getItemId() == R.id.menu_addresses)
                {
                    String name = AddressesFragment.class.getSimpleName();
                    requireActivity().getSupportFragmentManager()
                                     .beginTransaction()
                                     .replace(R.id.container, AddressesFragment.newInstance())
                                     .addToBackStack(name)
                                     .commit();
                    return true;
                }

                return false;
            }
        });

        RecyclerView recyclerView = binding.addressesRecyclerView;
        UserAddressesAdapter userAddressesAdapter = new UserAddressesAdapter(getContext(), requireActivity());
        recyclerView.setAdapter(userAddressesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userAddressesAdapter.setOnAddressClickListener(new UserAddressCallback()
        {
            @Override
            public void onAddressClick(Address address)
            {
                viewModel.setSelectedAddressesCount(userAddressesAdapter.getSelectedAddressCount());
                mainViewModel.setSelectedAddresses(userAddressesAdapter.getSelectedAddresses());
            }

            @Override
            public void onMaxAddresses()
            {
                Snackbar.make(view, R.string.max_addresses_count, Snackbar.LENGTH_LONG).show();
            }
        });

        viewModel.setAddressesCount(userAddressesAdapter.getItemCount());
        viewModel.setSelectedAddressesCount(userAddressesAdapter.getSelectedAddressCount());

        LiveData<List<Address>> byUser = viewModel.getByUser();
        if (!byUser.hasActiveObservers())
        {
            byUser.observeForever(addressList ->
            {
                userAddressesAdapter.setAddressList(addressList);
                viewModel.setAddressesCount(addressList.size());
                viewModel.setSelectedAddressesCount(userAddressesAdapter.getSelectedAddressCount());
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
