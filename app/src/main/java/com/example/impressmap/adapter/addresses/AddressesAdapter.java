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

    private OnJoinToAddressClickListener onJoinToAddressClickListener;

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

    /**
     * <p>Sets a new address list in adapter</p>
     */
    public void setAddressList(@NonNull List<Address> addressList)
    {
        if (viewModel.setAddressList(addressList))
        {
            notifyItemRangeRemoved(0, viewModel.getAddressesCount());
            notifyItemRangeInserted(0, addressList.size());
        }
    }

    @Override
    public int getItemCount()
    {
        return viewModel.getAddressesCount();
    }

    public void setOnJoinToAddressButtonClickListener(OnJoinToAddressClickListener listener)
    {
        this.onJoinToAddressClickListener = listener;
    }

    protected static class AddressViewHolder extends RecyclerView.ViewHolder
            implements OnJoinToAddressClickListener
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
                    Locations.getOneFromLatLng(adapter.context, address.getPositionLatLng()));

            binding.addressPrimaryView.setText(
                    String.format("%s %s", location.getCountry(), location.getCity()));
            binding.addressSecondaryView.setText(
                    String.format("%s %s", location.getStreet(), location.getHouse()));


            if (address.isSelected())
            {
                binding.joinToAddressButton.setImageDrawable(
                        Converter.getDrawable(adapter.context, R.drawable.ic_check));
                binding.joinToAddressButton.getDrawable()
                                           .setTint(adapter.context.getColor(R.color.positive));
            }
            else
            {
                binding.joinToAddressButton.setImageDrawable(
                        Converter.getDrawable(adapter.context, R.drawable.ic_add_group));
            }

            binding.joinToAddressButton.setOnClickListener(v ->
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
            if (adapter.onJoinToAddressClickListener != null)
            {
                adapter.onJoinToAddressClickListener.onJoinToAddressClick(address);
            }
        }
    }
}
