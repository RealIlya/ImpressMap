package com.example.impressmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.databinding.ItemUserAddressBinding;
import com.example.impressmap.model.data.Address;
import com.example.impressmap.model.data.Location;
import com.example.impressmap.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>
{
    private final Context context;
    private final List<Location> addresses;

    public LocationsAdapter(Context context)
    {
        this.context = context;
        addresses = new ArrayList<>();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType)
    {
        return new LocationViewHolder(
                ItemUserAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder,
                                 int position)
    {
        Location location = addresses.get(position);

        holder.binding.addressPrimaryView.setText(
                String.format("%s %s", location.getCountry(), location.getCity()));
        holder.binding.addressSecondaryView.setText(
                String.format("%s %s", location.getStreet(), location.getHouse()));

        if (location.isSelected())
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
            boolean addressDeselected = !location.isSelected();
            if (addressDeselected)
            {
                for (int i = 0; i < addresses.size(); i++)
                {
                    if (addresses.get(i).isSelected())
                    {
                        addresses.get(i).setSelected(false);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }

            location.setSelected(addressDeselected);
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount()
    {
        return addresses.size();
    }

    public void setAddresses(List<Location> addresses)
    {
        this.addresses.addAll(addresses);
        notifyItemRangeInserted(0, getItemCount());
    }

    public Location getSelectedAddress()
    {
        for (Location address : addresses)
        {
            if (address.isSelected())
            {
                return address;
            }
        }

        return null;
    }

    protected static class LocationViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemUserAddressBinding binding;

        public LocationViewHolder(@NonNull ItemUserAddressBinding addressBinding)
        {
            super(addressBinding.getRoot());
            binding = addressBinding;
        }
    }
}
