package com.example.impressmap.ui.fragment;

import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.comment.CommentsAdapter;
import com.example.impressmap.databinding.FragmentCommentsBinding;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.ui.viewmodel.CommentsViewModel;
import com.example.impressmap.ui.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;

public class CommentsFragment extends Fragment
{
    private static final String POST_KEY = "POST_KEY";

    private CommentsViewModel viewModel;
    private FragmentCommentsBinding binding;

    protected CommentsFragment()
    {
    }

    @NonNull
    public static CommentsFragment newInstance(Post post)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(POST_KEY, post);

        CommentsFragment commentsFragment = new CommentsFragment();
        commentsFragment.setArguments(arguments);
        return commentsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentCommentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(CommentsViewModel.class);

        Post post = requireArguments().getParcelable(POST_KEY);

        binding.toolbar.setTitle(post.getTitle());
        binding.toolbar.setNavigationOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack());

        binding.fullNameView.setText(post.getOwnerUser().getFullName());
        binding.textView.setText(post.getText());
        binding.dateView.setText(
                DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(post.getDate()));

        RecyclerView commentsRecyclerView = binding.commentsRecyclerView;
        CommentsAdapter commentsAdapter = new CommentsAdapter(requireActivity());
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getIdsByOwnerId(post).observeForever(ids ->
        {
            for (String id : ids)
            {
                viewModel.getById(id).observeForever(comment ->
                {
                    commentsAdapter.addComment(comment);
                });
            }
        });

        binding.senderToolbar.addMenuProvider(new MenuProvider()
        {
            @Override
            public void onCreateMenu(@NonNull Menu menu,
                                     @NonNull MenuInflater menuInflater)
            {
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem)
            {
                if (menuItem.getItemId() == R.id.menu_send)
                {
                    String message = binding.messageText.getText().toString().trim();
                    OwnerUser ownerUser = new OwnerUser();
                    ownerUser.setId(UID);
                    ownerUser.setFullName(AUTH.getCurrentUser().getEmail());

                    Comment comment = new Comment();
                    comment.setText(message);
                    comment.setOwnerUser(ownerUser);
                    viewModel.insert(comment, post, () ->
                    {
                        binding.messageText.clearFocus();
                        binding.messageText.getText().clear();
                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        commentsRecyclerView.smoothScrollToPosition(commentsAdapter.getItemCount());
                    }, () -> Snackbar.make(view, R.string.message_is_empty, Snackbar.LENGTH_LONG)
                                     .show());
                }
                return true;
            }
        });
    }

    @Nullable
    @Override
    public Object getSharedElementEnterTransition()
    {
        return TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move);
    }
}
