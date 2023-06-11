package com.example.impressmap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.databinding.ItemUserAddressBinding;
import com.example.impressmap.model.data.Location;
import com.example.impressmap.util.Converter;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>
{
    private final Context context;
    private final List<Location> locationList;

    public LocationsAdapter(Context context)
    {
        this.context = context;
        locationList = new ArrayList<>();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType)
    {
        return new LocationViewHolder(
                ItemUserAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder,
                                 int position)
    {
        holder.bind();
    }

    @Override
    public int getItemCount()
    {
        return locationList.size();
    }

    /**
     * <p>Sets a new locations list in adapter</p>
     */
    public void setLocationList(List<Location> locationList)
    {
        this.locationList.addAll(locationList);
        notifyItemRangeInserted(0, getItemCount());
    }

    public Location getSelectedAddress()
    {
        for (Location address : locationList)
        {
            if (address.isSelected())
            {
                return address;
            }
        }

        return null;
    }

    public void deselectAddress()
    {
        for (int i = 0; i < locationList.size(); i++)
        {
            if (locationList.get(i).isSelected())
            {
                locationList.get(i).setSelected(false);
                notifyItemChanged(i);
                break;
            }
        }
    }

    protected static class LocationViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemUserAddressBinding binding;
        private final LocationsAdapter adapter;

        public LocationViewHolder(@NonNull ItemUserAddressBinding binding,
                                  @NonNull LocationsAdapter adapter)
        {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void bind()
        {
            Location location = adapter.locationList.get(getAdapterPosition());

            binding.addressPrimaryView.setText(
                    String.format("%s %s", location.getCountry(), location.getCity()));
            binding.addressSecondaryView.setText(
                    String.format("%s %s", location.getStreet(), location.getHouse()));

            if (location.isSelected())
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
                boolean addressDeselected = !location.isSelected();
                if (addressDeselected)
                {
                    adapter.deselectAddress();
                }

                location.setSelected(addressDeselected);
                adapter.notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
