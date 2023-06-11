package com.example.impressmap.ui.fragment.bottommarker.showmorecomments;

import static com.example.impressmap.util.Constants.AUTH;
import static com.example.impressmap.util.Constants.UID;

import android.content.Context;
import android.os.Bundle;
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
import com.example.impressmap.adapter.comments.CommentsAdapter;
import com.example.impressmap.adapter.comments.OnCommentsButtonClickListener;
import com.example.impressmap.adapter.comments.OnReplyButtonClickListener;
import com.example.impressmap.databinding.FragmentShowMoreCommentsBinding;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.model.data.OwnerUser;
import com.example.impressmap.util.DateStrings;
import com.example.impressmap.util.MessageViewTextWatcher;

import java.util.List;

public class ShowMoreCommentsFragment extends Fragment
        implements OnCommentsButtonClickListener, OnReplyButtonClickListener
{
    private static final String OWNER_KEY = "OWNER_KEY";

    private ShowMoreCommentsFragmentViewModel viewModel;

    private FragmentShowMoreCommentsBinding binding;

    public static ShowMoreCommentsFragment newInstance(Comment comment)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(OWNER_KEY, comment);

        ShowMoreCommentsFragment showMoreCommentsFragment = new ShowMoreCommentsFragment();
        showMoreCommentsFragment.setArguments(arguments);
        return showMoreCommentsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentShowMoreCommentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(ShowMoreCommentsFragmentViewModel.class);

        Comment comment = requireArguments().getParcelable(OWNER_KEY);

        binding.toolbar.setTitle(comment.getOwnerUser().getFullName());
        binding.toolbar.setNavigationOnClickListener(
                v -> getParentFragmentManager().beginTransaction().remove(this).commit());

        requireActivity().getOnBackPressedDispatcher()
                         .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
                         {
                             @Override
                             public void handleOnBackPressed()
                             {
                                 getParentFragmentManager().beginTransaction()
                                                           .remove(ShowMoreCommentsFragment.this)
                                                           .commit();
                             }
                         });

        binding.fullNameView.setText(comment.getOwnerUser().getFullName());
        binding.textView.setText(comment.getText());
        binding.dateView.setText(DateStrings.getDateString(getResources(), comment.getDateTime()));

        LiveData<List<String>> commentIdsLiveData = viewModel.getIdsByOwner(comment);

        RecyclerView commentsRecyclerView = binding.commentsRecyclerView;
        CommentsAdapter commentsAdapter = new CommentsAdapter(requireContext());
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (!commentIdsLiveData.hasActiveObservers())
        {
            commentIdsLiveData.observe(getViewLifecycleOwner(), ids ->
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

                    Comment subComment = new Comment();
                    subComment.setText(message);
                    subComment.setOwnerUser(ownerUser);
                    viewModel.insert(subComment, comment, () ->
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
        getParentFragmentManager().beginTransaction()
                                  .add(R.id.bottom_container,
                                          ShowMoreCommentsFragment.newInstance(comment))
                                  .commit();
    }

    @Override
    public void onCommentClick(View v,
                               Comment comment)
    {
        getParentFragmentManager().beginTransaction()
                                  .add(R.id.bottom_container,
                                          ShowMoreCommentsFragment.newInstance(comment))
                                  .commit();
    }
}
