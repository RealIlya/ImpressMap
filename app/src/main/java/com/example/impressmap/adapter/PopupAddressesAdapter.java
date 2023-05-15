package com.example.impressmap.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemAddressBinding;
import com.example.impressmap.model.data.Address;

import java.util.List;

public class PopupAddressesAdapter
        extends RecyclerView.Adapter<PopupAddressesAdapter.AddressViewHolder>
{
    private final List<Address> addresses;
    private OnAddressClickListener onAddressClickListener;

    public PopupAddressesAdapter(List<Address> addresses)
    {
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType)
    {
        return new PopupAddressesAdapter.AddressViewHolder(
                ItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder,
                                 int position)
    {
        Address address = addresses.get(position);

        holder.binding.addressPrimaryView.setText(
                String.format("%s %s", address.getCountry(), address.getCity()));
        holder.binding.addressSecondaryView.setText(
                String.format("%s %s", address.getStreet(), address.getHouse()));

        holder.binding.getRoot().setOnClickListener(v -> onAddressClicked(address));
    }

    @Override
    public int getItemCount()
    {
        return addresses.size();
    }

    private void onAddressClicked(Address address)
    {
        onAddressClickListener.onAddressClick(address);
    }

    public void setOnAddressClickListener(OnAddressClickListener listener)
    {
        onAddressClickListener = listener;
    }

    public interface OnAddressClickListener
    {
        void onAddressClick(Address address);
    }

    protected static class AddressViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemAddressBinding binding;

        public AddressViewHolder(@NonNull ItemAddressBinding addressBinding)
        {
            super(addressBinding.getRoot());
            binding = addressBinding;
        }
    }
}
