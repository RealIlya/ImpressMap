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
        implements OnAddressClickListener
{
    private final Context context;
    private final List<Address> addresses;
    private OnAddressClickListener onAddressClickListener;

    public PopupAddressesAdapter(Context context,
                                 List<Address> addresses)
    {
        this.context = context;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType)
    {
        return new PopupAddressesAdapter.AddressViewHolder(
                ItemUserAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder,
                                 int position)
    {
        /*
         * Удобнее всего добавить в холдер фунцию bind внутри которой будет располагаться логика
         * отображения данных, тогда тут надо будет просто вызвать эту функцию, а сама логика
         * установки данных будет находиться в холдере
         */
        Address address = addresses.get(position);

        Location location = Locations.getOneFromLatLng(context, address.getPosition());

        /*
         * При работе с ресайлером всегда нужно обрабатывать обе ветки ветвления из-за того что вью
         * переиспользуются. когда придет айтем с location == null у тебя не сотруться старые данные
         * и будут отображаться не верно
         */
        if (location != null)
        {
            holder.binding.addressPrimaryView.setText(
                    String.format("%s %s", location.getCountry(), location.getCity()));
            holder.binding.addressSecondaryView.setText(
                    String.format("%s %s", location.getStreet(), location.getHouse()));
        }

        holder.binding.getRoot().setOnClickListener(v -> onAddressClick(address));
    }

    @Override
    public int getItemCount()
    {
        return addresses.size();
    }

    public void setOnAddressClickListener(OnAddressClickListener listener)
    {
        onAddressClickListener = listener;
    }

    @Override
    public void onAddressClick(Address address)
    {
        if (onAddressClickListener != null)
        {
            onAddressClickListener.onAddressClick(address);
        }
    }

    protected static class AddressViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemUserAddressBinding binding;

        public AddressViewHolder(@NonNull ItemUserAddressBinding addressBinding)
        {
            super(addressBinding.getRoot());
            binding = addressBinding;
        }
    }
}
