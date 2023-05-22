package com.example.impressmap.ui.fragment.bottommap.marker;

import static com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragment.NAVIGATING_MODE;
import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentCreatorCommonMarkerInputBinding;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.ui.activity.main.MainViewModel;
import com.example.impressmap.ui.fragment.bottommap.mapinfo.MapInfoFragmentViewModel;
import com.example.impressmap.util.SwitchableMode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class CreatorCommonMarkerInputFragment extends Fragment implements MenuProvider
{
    private FragmentCreatorCommonMarkerInputBinding binding;
    private MapInfoFragmentViewModel viewModel;

    protected CreatorCommonMarkerInputFragment()
    {
    }

    @NonNull
    public static CreatorCommonMarkerInputFragment newInstance()
    {
        return new CreatorCommonMarkerInputFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentCreatorCommonMarkerInputBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(requireParentFragment()).get(
                MapInfoFragmentViewModel.class);

        ((SwitchableMode) requireParentFragment()).switchMode(NAVIGATING_MODE);
        ((MenuHost) requireParentFragment()).addMenuProvider(this, getViewLifecycleOwner());
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu,
                             @NonNull MenuInflater menuInflater)
    {
        menuInflater.inflate(R.menu.menu_creator_common_marker, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.menu_create)
        {
            String title = binding.markerTitleView.getText().toString();
            String text = binding.markerTextView.getText().toString();
            if (!title.isEmpty() && !text.isEmpty())
            {
                GMarkerMetadata gMarkerMetadata = new GMarkerMetadata();
                gMarkerMetadata.setTitle(title);
                gMarkerMetadata.setPositionLatLng(viewModel.getLatLng());
                gMarkerMetadata.setType(GMarkerMetadata.COMMON_MARKER);

                OwnerUser ownerUser = new OwnerUser();
                ownerUser.setId(UID);
                ownerUser.setFullName(AUTH.getCurrentUser().getEmail());

                Post post = new Post();
                post.setTitle(title);
                post.setText(text);
                post.setOwnerUser(ownerUser);

                MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(
                        MainViewModel.class);
                viewModel.insertCommonMarker(mainViewModel.getSelectedAddressId().getValue(),
                        gMarkerMetadata, post,
                        () -> ((BottomSheetDialogFragment) requireParentFragment()).dismiss());
            }
            else
            {
                Snackbar.make(requireView(), R.string.field_is_necessary, Snackbar.LENGTH_LONG)
                        .show();
            }
            return true;
        }

        return false;
    }
}
