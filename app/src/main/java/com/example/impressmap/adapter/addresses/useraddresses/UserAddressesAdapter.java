package com.example.impressmap.adapter.addresses.useraddresses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.databinding.ItemUserAddressBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.Location;
import com.example.impressmap.util.Converter;
import com.example.impressmap.util.Locations;

import java.util.List;
import java.util.Objects;

public class UserAddressesAdapter
        extends RecyclerView.Adapter<UserAddressesAdapter.UserAddressViewHolder>
{
    private final Context context;
    private final UserAddressesAdapterViewModel viewModel;
    private UserAddressCallback userAddressCallback;

    public UserAddressesAdapter(Context context,
                                ViewModelStoreOwner viewModelStoreOwner)
    {
        this.context = context;
        viewModel = new ViewModelProvider(viewModelStoreOwner).get(
                UserAddressesAdapterViewModel.class);
    }

    @NonNull
    @Override
    public UserAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType)
    {
        return new UserAddressViewHolder(
                ItemUserAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAddressViewHolder holder,
                                 int position)
    {
        holder.bind();
    }

    public void setAddresses(@NonNull List<Address> addresses)
    {
        if (viewModel.setAddresses(addresses))
        {
            notifyItemRangeRemoved(0, viewModel.getAddressesCount());
            notifyItemRangeInserted(0, addresses.size());
        }
    }

    @Override
    public int getItemCount()
    {
        return viewModel.getAddressesCount();
    }

    public int getSelectedAddressCount()
    {
        return viewModel.getSelectedAddresses().size();
    }

    public List<Address> getSelectedAddresses()
    {
        return viewModel.getSelectedAddresses();
    }

    public void setOnAddressClickListener(UserAddressCallback listener)
    {
        userAddressCallback = listener;
    }

    protected static class UserAddressViewHolder extends RecyclerView.ViewHolder
            implements UserAddressCallback
    {
        private final ItemUserAddressBinding binding;
        private final UserAddressesAdapter adapter;

        public UserAddressViewHolder(@NonNull ItemUserAddressBinding binding,
                                     @NonNull UserAddressesAdapter adapter)
        {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void bind()
        {
            Address address = adapter.viewModel.getAddress(getAdapterPosition());

            Location location = Objects.requireNonNull(
                    Locations.getOneFromLatLng(adapter.context, address.getPosition()));

            binding.addressPrimaryView.setText(
                    String.format("%s %s", location.getCountry(), location.getCity()));
            binding.addressSecondaryView.setText(
                    String.format("%s %s", location.getStreet(), location.getHouse()));

            if (address.isSelected())
            {
                binding.getRoot()
                       .setBackgroundColor(Converter.getAttributeColor(adapter.context,
                               R.attr.backgroundSelectedItem));
            }
            else
            {
                binding.getRoot()
                       .setBackground(
                               Converter.getDrawable(adapter.context, R.drawable.ripple_effect));
            }

            binding.getRoot().setOnClickListener(v ->
            {
                if (adapter.viewModel.getSelectedAddresses().size() < 5 || address.isSelected())
                {
                    address.setSelected(!address.isSelected());
                    onAddressClick(address);
                    adapter.notifyItemChanged(getAdapterPosition());
                }
                else
                {
                    onMaxAddresses();
                }
            });
        }


        @Override
        public void onAddressClick(Address address)
        {
            if (adapter.userAddressCallback != null)
            {
                adapter.userAddressCallback.onAddressClick(address);
            }
        }

        @Override
        public void onMaxAddresses()
        {
            if (adapter.userAddressCallback != null)
            {
                adapter.userAddressCallback.onMaxAddresses();
            }
        }
    }
}
