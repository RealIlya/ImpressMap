package com.example.impressmap.adapter.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemCommentBinding;
import com.example.impressmap.model.data.Comment;
import com.example.impressmap.util.DateStrings;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>
        implements OnCommentsButtonClickListener, OnReplyButtonClickListener
{

    private final Context context;
    private final List<Comment> commentList;

    private OnCommentsButtonClickListener onCommentsButtonClickListener;
    private OnReplyButtonClickListener onReplyButtonClickListener;

    public CommentsAdapter(Context context)
    {
        this.context = context;
        commentList = new ArrayList<>();
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
        Comment comment = commentList.get(position);

        holder.binding.textView.setText(comment.getText());
        holder.binding.fullNameView.setText(comment.getOwnerUser().getFullName());
        String dateString = DateStrings.getDateString(context.getResources(),
                comment.getDateTime());

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
        return commentList.size();
    }

    public void addComment(Comment comment)
    {
        commentList.add(comment);
        notifyItemInserted(getItemCount());
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
