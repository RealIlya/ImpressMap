package com.example.impressmap.adapter.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemCommentBinding;
import com.example.impressmap.model.data.Comment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class SubCommentsAdapter extends RecyclerView.Adapter<SubCommentsAdapter.CommentsViewHolder>
        implements OnCommentsButtonClickListener, OnReplyButtonClickListener
{
    private List<Comment> comments;

    private OnCommentsButtonClickListener onCommentsButtonClickListener;
    private OnReplyButtonClickListener onReplyButtonClickListener;

    public SubCommentsAdapter()
    {
        comments = new ArrayList<>();
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
        Comment comment = comments.get(position);

        holder.binding.textView.setText(comment.getText());
        holder.binding.fullNameView.setText(comment.getOwnerUser().getFullName());
        String dateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                                      .format(comment.getDate());
        holder.binding.dateView.setText(dateString);

        holder.binding.showCommentsButton.setOnClickListener(v ->
        {
            onCommentClick(v, holder.binding.commentsRecyclerView, comment);
        });

        holder.binding.replyView.setOnClickListener(v ->
        {
            onReplyClick(v, comment);
        });

    }

    @Override
    public int getItemCount()
    {
        return comments.size();
    }

    public void addComment(Comment comment)
    {
        comments.add(comment);
        notifyItemInserted(getItemCount());
    }

    public void clear()
    {
        int size = getItemCount();
        comments.clear();
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
                               RecyclerView commentsRecyclerView,
                               Comment comment)
    {
        if (onCommentsButtonClickListener != null)
        {
            onCommentsButtonClickListener.onCommentClick(view, commentsRecyclerView, comment);
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
