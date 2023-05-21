package com.example.impressmap.ui.fragment.inputmessage;

import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.impressmap.R;
import com.example.impressmap.databinding.FragmentInputMessageBinding;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.Owner;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.util.MessageViewTextWatcher;

public class InputMessageFragment extends Fragment
{
    private static final String OWNER_KEY = "OWNER_KEY";

    public static boolean active = false;

    private InputMessageFragmentViewModel viewModel;
    private FragmentInputMessageBinding binding;

    protected InputMessageFragment()
    {
    }

    @NonNull
    public static InputMessageFragment newInstance(Owner owner)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(OWNER_KEY, owner);

        InputMessageFragment inputMessageFragment = new InputMessageFragment();
        inputMessageFragment.setArguments(arguments);
        return inputMessageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentInputMessageBinding.inflate(inflater, container, false);
        active = true;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(InputMessageFragmentViewModel.class);

        Owner owner = requireArguments().getParcelable(OWNER_KEY);

        binding.toolbar.setNavigationOnClickListener(
                v -> getParentFragmentManager().beginTransaction().remove(this).commit());
        binding.toolbar.setTitle("");

        binding.messageText.addTextChangedListener(new MessageViewTextWatcher(
                binding.senderToolbar.getMenu().findItem(R.id.menu_send)));
        binding.messageText.requestFocus(View.FOCUS_DOWN);
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.messageText, InputMethodManager.SHOW_IMPLICIT);

        binding.senderToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (item.getItemId() == R.id.menu_send)
                {
                    String message = binding.messageText.getText().toString().trim();
                    OwnerUser ownerUser = new OwnerUser();
                    ownerUser.setId(UID);
                    ownerUser.setFullName(AUTH.getCurrentUser().getEmail());

                    Comment comment = new Comment();
                    comment.setText(message);
                    comment.setOwnerUser(ownerUser);
                    viewModel.insert(comment, owner, () ->
                    {
                        getParentFragmentManager().beginTransaction()
                                                  .remove(InputMessageFragment.this)
                                                  .commit();
                    }, () ->
                    {
                    });
                }

                return false;
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        active = false;
    }
}
