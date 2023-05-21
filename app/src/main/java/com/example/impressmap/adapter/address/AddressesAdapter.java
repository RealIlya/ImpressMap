package com.example.impressmap.adapter.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.databinding.ItemAddressBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.util.Converter;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>
        implements AddressCallback
{
    private final Context context;
    private final AddressesAdapterViewModel viewModel;
    private AddressCallback addressCallback;

    public AddressesAdapter(Context context,
                            ViewModelStoreOwner viewModelStoreOwner)
    {
        this.context = context;

        viewModel = new ViewModelProvider(viewModelStoreOwner).get(AddressesAdapterViewModel.class);
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType)
    {
        return new AddressViewHolder(
                ItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder,
                                 int position)
    {
        Address address = viewModel.getAddress(position);

        holder.binding.addressPrimaryView.setText(
                String.format("%s %s", address.getCountry(), address.getCity()));
        holder.binding.addressSecondaryView.setText(
                String.format("%s %s", address.getStreet(), address.getHouse()));

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

    public void setOnAddressClickListener(AddressCallback listener)
    {
        addressCallback = listener;
    }

    @Override
    public void onAddressClick(Address address)
    {
        addressCallback.onAddressClick(address);
    }

    @Override
    public void onMaxAddresses()
    {
        addressCallback.onMaxAddresses();
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
