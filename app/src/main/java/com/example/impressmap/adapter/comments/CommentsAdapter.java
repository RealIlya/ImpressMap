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
                ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder,
                                 int position)
    {
        holder.bind();
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

    protected static class CommentsViewHolder extends RecyclerView.ViewHolder
            implements OnCommentsButtonClickListener, OnReplyButtonClickListener
    {
        private final ItemCommentBinding binding;
        private final CommentsAdapter adapter;

        public CommentsViewHolder(@NonNull ItemCommentBinding binding,
                                  @NonNull CommentsAdapter adapter)
        {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void bind()
        {
            Comment comment = adapter.commentList.get(getAdapterPosition());

            binding.textView.setText(comment.getText());
            binding.fullNameView.setText(comment.getOwnerUser().getFullName());
            String dateString = DateStrings.getDateString(adapter.context.getResources(),
                    comment.getDateTime());

            binding.dateView.setText(dateString);

            binding.showCommentsButton.setOnClickListener(v -> onCommentClick(v, comment));

            binding.replyView.setOnClickListener(v -> onReplyClick(v, comment));
        }

        @Override
        public void onCommentClick(View view,
                                   Comment comment)
        {
            if (adapter.onCommentsButtonClickListener != null)
            {
                adapter.onCommentsButtonClickListener.onCommentClick(view, comment);
            }
        }

        @Override
        public void onReplyClick(View view,
                                 Comment comment)
        {
            if (adapter.onReplyButtonClickListener != null)
            {
                adapter.onReplyButtonClickListener.onReplyClick(view, comment);
            }
        }
    }
}
