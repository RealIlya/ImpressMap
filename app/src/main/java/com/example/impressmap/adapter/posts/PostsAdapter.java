package com.example.impressmap.adapter.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemPostBinding;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.util.DateStrings;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder>
{
    private final Context context;
    private final List<Post> postList;

    private OnCommentsButtonClickListener onCommentsButtonClickListener;

    public PostsAdapter(Context context)
    {
        this.context = context;
        postList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType)
    {
        return new PostViewHolder(
                ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false),
                this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder,
                                 int position)
    {
        holder.bind();
    }

    @Override
    public int getItemCount()
    {
        return postList.size();
    }

    /**
     * <p>Adds a new post in adapter</p>
     */
    public void addPost(Post post)
    {
        postList.add(post);
        notifyItemInserted(getItemCount());
    }

    public void setOnCommentsButtonClickListener(OnCommentsButtonClickListener listener)
    {
        this.onCommentsButtonClickListener = listener;
    }

    protected static class PostViewHolder extends RecyclerView.ViewHolder
            implements OnCommentsButtonClickListener

    {
        private final ItemPostBinding binding;
        private final PostsAdapter adapter;

        public PostViewHolder(@NonNull ItemPostBinding binding,
                              @NonNull PostsAdapter adapter)
        {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void bind()
        {
            Post post = adapter.postList.get(getAdapterPosition());

            binding.textView.setText(post.getText());
            binding.fullNameView.setText(post.getOwnerUser().getFullName());
            String dateString = DateStrings.getDateString(adapter.context.getResources(),
                    post.getDateTime());
            binding.dateView.setText(dateString);

            binding.showCommentsButton.setOnClickListener(v ->
            {
                binding.getRoot().setTransitionName(post.getId());
                onCommentClick(binding.getRoot(), post);
            });
        }

        @Override
        public void onCommentClick(View view,
                                   Post post)
        {
            if (adapter.onCommentsButtonClickListener != null)
            {
                adapter.onCommentsButtonClickListener.onCommentClick(view, post);
            }
        }
    }
}
