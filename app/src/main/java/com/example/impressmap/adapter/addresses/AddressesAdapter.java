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
import java.util.Objects;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>
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
                ItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                this);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder,
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

    public void setOnJoinToAddressButtonClickListener(OnJoinToAddressButtonClickListener listener)
    {
        this.onJoinToAddressButtonClickListener = listener;
    }

    protected static class AddressViewHolder extends RecyclerView.ViewHolder
            implements OnJoinToAddressButtonClickListener
    {
        private final ItemAddressBinding binding;
        private final AddressesAdapter adapter;

        public AddressViewHolder(@NonNull ItemAddressBinding binding,
                                 @NonNull AddressesAdapter adapter)
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
                binding.joinAddressButton.setImageDrawable(
                        Converter.getDrawable(adapter.context, R.drawable.ic_check));
                binding.joinAddressButton.getDrawable()
                                         .setTint(adapter.context.getColor(R.color.positive));
            }
            else
            {
                binding.joinAddressButton.setImageDrawable(
                        Converter.getDrawable(adapter.context, R.drawable.ic_add_group));
            }

            binding.joinAddressButton.setOnClickListener(v ->
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
                adapter.notifyItemChanged(getAdapterPosition());
            });
        }

        @Override
        public void onJoinToAddressClick(Address address)
        {
            if (adapter.onJoinToAddressButtonClickListener != null)
            {
                adapter.onJoinToAddressButtonClickListener.onJoinToAddressClick(address);
            }
        }
    }
}
