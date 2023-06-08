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

public class UserAddressesAdapter
        extends RecyclerView.Adapter<UserAddressesAdapter.UserAddressViewHolder>
        implements UserAddressCallback
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
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAddressViewHolder holder,
                                 int position)
    {
        Address address = viewModel.getAddress(position);

        Location location = Locations.getOneFromLatLng(context, address.getPosition());

        holder.binding.addressPrimaryView.setText(
                String.format("%s %s", location.getCountry(), location.getCity()));
        holder.binding.addressSecondaryView.setText(
                String.format("%s %s", location.getStreet(), location.getHouse()));

        if (address.isSelected())
        {
            holder.binding.getRoot()
                          .setBackgroundColor(Converter.getAttributeColor(context,
                                  R.attr.backgroundSelectedItem));
        }
        else
        {
            holder.binding.getRoot()
                          .setBackground(Converter.getDrawable(context, R.drawable.ripple_effect));
        }

        holder.binding.getRoot().setOnClickListener(v ->
        {
            if (viewModel.getSelectedAddresses().size() < 5 || address.isSelected())
            {
                address.setSelected(!address.isSelected());
                onAddressClick(address);
                notifyItemChanged(holder.getAdapterPosition());
            }
            else
            {
                onMaxAddresses();
            }
        });
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

    @Override
    public void onAddressClick(Address address)
    {
        userAddressCallback.onAddressClick(address);
    }

    @Override
    public void onMaxAddresses()
    {
        userAddressCallback.onMaxAddresses();
    }

    protected static class UserAddressViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemUserAddressBinding binding;

        public UserAddressViewHolder(@NonNull ItemUserAddressBinding addressBinding)
        {
            super(addressBinding.getRoot());
            binding = addressBinding;
        }
    }
}
