package com.example.impressmap.adapter.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemCommentBinding;
import com.example.impressmap.model.data.Comment;

import java.text.DateFormat;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>
        implements OnCommentsButtonClickListener, OnReplyButtonClickListener
{
    private final CommentsAdapterViewModel viewModel;

    private OnCommentsButtonClickListener onCommentsButtonClickListener;
    private OnReplyButtonClickListener onReplyButtonClickListener;

    public CommentsAdapter(ViewModelStoreOwner viewModelStoreOwner)
    {
        viewModel = new ViewModelProvider(viewModelStoreOwner).get(CommentsAdapterViewModel.class);
        clear();
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType)
    {
        return new CommentsViewHolder(
                ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder,
                                 int position)
    {
        Comment comment = viewModel.getComment(position);

        holder.binding.textView.setText(comment.getText());
        holder.binding.fullNameView.setText(comment.getOwnerUser().getFullName());
        String dateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                                      .format(comment.getDateTime());
        holder.binding.dateView.setText(dateString);

        holder.binding.showCommentsButton.setOnClickListener(v ->
        {
            onCommentClick(v, comment);
        });

        holder.binding.replyView.setOnClickListener(v ->
        {
            onReplyClick(v, comment);
        });

    }

    @Override
    public int getItemCount()
    {
        return viewModel.getCommentsCount();
    }

    public void addComment(Comment comment)
    {
        viewModel.addComment(comment);
        notifyItemInserted(getItemCount());
    }

    public void clear()
    {
        int size = getItemCount();
        viewModel.clearCache();
        notifyItemRangeRemoved(0, size);
    }

    public void setOnCommentsButtonClickListener(OnCommentsButtonClickListener listener)
    {
        onCommentsButtonClickListener = listener;
    }

    public void setOnReplyButtonClickListener(OnReplyButtonClickListener listener)
    {
        onReplyButtonClickListener = listener;
    }

    @Override
    public void onCommentClick(View view,
                               Comment comment)
    {
        if (onCommentsButtonClickListener != null)
        {
            onCommentsButtonClickListener.onCommentClick(view, comment);
        }
    }

    @Override
    public void onReplyClick(View view,
                             Comment comment)
    {
        if (onReplyButtonClickListener != null)
        {
            onReplyButtonClickListener.onReplyClick(view, comment);
        }
    }

    protected static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemCommentBinding binding;

        public CommentsViewHolder(ItemCommentBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
