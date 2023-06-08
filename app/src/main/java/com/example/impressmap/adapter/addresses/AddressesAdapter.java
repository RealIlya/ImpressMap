package com.example.impressmap.adapter.addresses;

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
import com.example.impressmap.model.data.Location;
import com.example.impressmap.util.Converter;
import com.example.impressmap.util.Locations;

import java.util.List;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>
        implements OnJoinToAddressButtonClickListener
{
    private final Context context;
    private final AddressesAdapterViewModel viewModel;

    private OnJoinToAddressButtonClickListener onJoinToAddressButtonClickListener;

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

        Location location = Locations.getOneFromLatLng(context, address.getPosition());

        holder.binding.addressPrimaryView.setText(
                String.format("%s %s", location.getCountry(), location.getCity()));
        holder.binding.addressSecondaryView.setText(
                String.format("%s %s", location.getStreet(), location.getHouse()));


        if (address.isSelected())
        {
            holder.binding.joinAddressButton.setImageDrawable(
                    Converter.getDrawable(context, R.drawable.ic_check));
            holder.binding.joinAddressButton.getDrawable()
                                            .setTint(context.getColor(R.color.positive));
        }
        else
        {
            holder.binding.joinAddressButton.setImageDrawable(
                    Converter.getDrawable(context, R.drawable.ic_add_group));
        }

        holder.binding.joinAddressButton.setOnClickListener(v ->
        {
            if (address.isSelected())
            {
                address.setSelected(false);
            }
            else
            {
                address.setSelected(true);
                onJoinToAddressClick(address);
            }
            notifyItemChanged(holder.getAdapterPosition());
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

    public void setOnJoinToAddressButtonClickListener(OnJoinToAddressButtonClickListener listener)
    {
        this.onJoinToAddressButtonClickListener = listener;
    }

    @Override
    public void onJoinToAddressClick(Address address)
    {
        if (onJoinToAddressButtonClickListener != null)
        {
            onJoinToAddressButtonClickListener.onJoinToAddressClick(address);
        }
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
