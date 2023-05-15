package com.example.impressmap.adapter.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.databinding.ItemPostBinding;
import com.example.impressmap.model.data.Post;

import java.text.DateFormat;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder>
{
    private final PostsAdapterViewModel viewModel;


    private OnCommentsButtonClickListener onCommentsButtonClickListener;

    public PostsAdapter(ViewModelStoreOwner viewModelStoreOwner)
    {
        viewModel = new ViewModelProvider(viewModelStoreOwner).get(PostsAdapterViewModel.class);
        clear();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType)
    {
        return new PostViewHolder(
                ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder,
                                 int position)
    {
        Post post = viewModel.getPost(position);

        holder.binding.textView.setText(post.getText());
        holder.binding.fullNameView.setText(post.getOwnerUser().getFullName());
        String dateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                                      .format(post.getDate());
        holder.binding.dateView.setText(dateString);

        holder.binding.showReactionsButton.setOnClickListener(v ->
        {

        });

        holder.binding.showCommentsButton.setOnClickListener(v ->
        {
            holder.binding.getRoot().setTransitionName(post.getId());
            onCommentsButtonClickListener.onClick(holder.binding.getRoot(), post);
        });

        // try to avoid this method
        LiveData<List<String>> ownerId = viewModel.getIdsByOwnerId(post);
        if (!ownerId.hasActiveObservers())
        {
            ownerId.observeForever(ids ->
            {
                holder.binding.showCommentsButton.setText(String.valueOf(ids.size()));
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return viewModel.getPostsCount();
    }

    public void addPost(Post post)
    {
        viewModel.addPost(post);
        notifyItemInserted(getItemCount());
    }

    public void clear()
    {
        int count = getItemCount();
        viewModel.clearCache();
        notifyItemRangeRemoved(0, count);
    }

    public void setOnCommentsButtonClickListener(OnCommentsButtonClickListener listener)
    {
        this.onCommentsButtonClickListener = listener;
    }

    public interface OnCommentsButtonClickListener
    {
        void onClick(View view,
                     Post post);
    }

    protected static class PostViewHolder extends RecyclerView.ViewHolder
    {
        private final ItemPostBinding binding;

        public PostViewHolder(@NonNull ItemPostBinding itemPostBinding)
        {
            super(itemPostBinding.getRoot());
            binding = itemPostBinding;
        }
    }
}
