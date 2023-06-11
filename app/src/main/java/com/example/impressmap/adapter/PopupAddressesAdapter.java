package com.example.impressmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemUserAddressBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.Location;
import com.example.impressmap.util.Locations;

import java.util.List;

public class PopupAddressesAdapter
        extends RecyclerView.Adapter<PopupAddressesAdapter.AddressViewHolder>
{
    private final Context context;
    private final List<Address> addressList;
    private OnAddressClickListener onAddressClickListener;

    public PopupAddressesAdapter(Context context,
                                 List<Address> addressList)
    {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType)
    {
        return new PopupAddressesAdapter.AddressViewHolder(
                ItemUserAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder,
                                 int position)
    {
        holder.bind();
    }

    @Override
    public int getItemCount()
    {
        return addressList.size();
    }

    public void setOnAddressClickListener(OnAddressClickListener listener)
    {
        onAddressClickListener = listener;
    }

    protected static class AddressViewHolder extends RecyclerView.ViewHolder
            implements OnAddressClickListener
    {
        private final ItemUserAddressBinding binding;
        private final PopupAddressesAdapter adapter;

        public AddressViewHolder(@NonNull ItemUserAddressBinding binding,
                                 PopupAddressesAdapter adapter)
        {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void bind()
        {
            Address address = adapter.addressList.get(getAdapterPosition());

            Location location = Locations.getOneFromLatLng(adapter.context, address.getPositionLatLng());

            if (location != null)
            {
                binding.addressPrimaryView.setText(
                        String.format("%s %s", location.getCountry(), location.getCity()));
                binding.addressSecondaryView.setText(
                        String.format("%s %s", location.getStreet(), location.getHouse()));
            }

            binding.getRoot().setOnClickListener(v -> onAddressClick(address));
        }

        @Override
        public void onAddressClick(Address address)
        {
            if (adapter.onAddressClickListener != null)
            {
                adapter.onAddressClickListener.onAddressClick(address);
            }
        }
    }
}
