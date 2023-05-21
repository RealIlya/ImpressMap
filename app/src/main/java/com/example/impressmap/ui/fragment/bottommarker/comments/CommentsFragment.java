package com.example.impressmap.ui.fragment.bottommarker.comments;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.comment.CommentsAdapter;
import com.example.impressmap.adapter.comment.OnCommentsButtonClickListener;
import com.example.impressmap.adapter.comment.OnReplyButtonClickListener;
import com.example.impressmap.adapter.comment.SubCommentsAdapter;
import com.example.impressmap.databinding.FragmentCommentsBinding;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.ui.fragment.inputmessage.InputMessageFragment;
import com.example.impressmap.util.MessageViewTextWatcher;

import java.text.DateFormat;
import java.util.List;

public class CommentsFragment extends Fragment
        implements OnCommentsButtonClickListener, OnReplyButtonClickListener
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
                v -> getParentFragmentManager().popBackStack());

        requireActivity().getOnBackPressedDispatcher()
                         .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
                         {
                             @Override
                             public void handleOnBackPressed()
                             {
                                 getParentFragmentManager().popBackStack();
                             }
                         });

        binding.fullNameView.setText(post.getOwnerUser().getFullName());
        binding.textView.setText(post.getText());
        binding.dateView.setText(
                DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(post.getDate()));

        RecyclerView commentsRecyclerView = binding.commentsRecyclerView;
        CommentsAdapter commentsAdapter = new CommentsAdapter(requireActivity());
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LiveData<List<String>> idsByOwnerId = viewModel.getIdsByOwner(post);
        if (!idsByOwnerId.hasActiveObservers())
        {
            idsByOwnerId.observe(getViewLifecycleOwner(), ids ->
            {
                commentsAdapter.clear();
                for (String id : ids)
                {
                    LiveData<Comment> byId = viewModel.getById(id);
                    if (!byId.hasActiveObservers())
                    {
                        byId.observeForever(commentsAdapter::addComment);
                    }
                }
            });
        }

        commentsAdapter.setOnCommentsButtonClickListener(this);
        commentsAdapter.setOnReplyButtonClickListener(this);

        binding.messageText.addTextChangedListener(new MessageViewTextWatcher(
                binding.senderToolbar.getMenu().findItem(R.id.menu_send)));

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
                    }, () ->
                    {
                    });
                }
                return true;
            }
        });
    }

    @Override
    public void onReplyClick(View view,
                             Comment comment)
    {
        if (!InputMessageFragment.active)
        {
            getParentFragmentManager().beginTransaction()
                                      .add(R.id.bottom_container,
                                              InputMessageFragment.newInstance(comment))
                                      .commit();
        }
    }

    @Override
    public void onCommentClick(View v,
                               RecyclerView commentsRecyclerView,
                               Comment comment)
    {
        LiveData<List<String>> commentIdsLiveData = viewModel.getIdsByOwner(comment);

        if (!commentIdsLiveData.hasActiveObservers())
        {
            commentIdsLiveData.observe(getViewLifecycleOwner(), ids ->
            {
                if (!ids.isEmpty())
                {
                    v.setVisibility(View.GONE);
                }

                SubCommentsAdapter subCommentsAdapter = new SubCommentsAdapter();
                commentsRecyclerView.setAdapter(subCommentsAdapter);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                subCommentsAdapter.setOnCommentsButtonClickListener(this);
                subCommentsAdapter.setOnReplyButtonClickListener(this);

                for (String id : ids)
                {
                    LiveData<Comment> byId = viewModel.getById(id);
                    if (!byId.hasActiveObservers())
                    {
                        byId.observeForever(subCommentsAdapter::addComment);
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public Object getSharedElementEnterTransition()
    {
        return TransitionInflater.from(requireContext())
                                 .inflateTransition(android.R.transition.move);
    }
}
