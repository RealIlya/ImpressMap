package com.example.impressmap.ui.fragment.bottommarker.posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.R;
import com.example.impressmap.adapter.posts.PostsAdapter;
import com.example.impressmap.databinding.FragmentPostsBinding;
import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.model.data.GMarkerWithChildrenMetadata;
import com.example.impressmap.model.data.Post;
import com.example.impressmap.ui.fragment.bottommarker.comments.CommentsFragment;

import java.util.List;

public class PostsFragment extends Fragment
{
    private static final String METADATA_KEY = "METADATA_KEY";
    private FragmentPostsBinding binding;
    private PostsFragmentViewModel viewModel;
    private View.OnClickListener onDeselectItemClickListener;
    private OnBackPressedCallback onBackPressedCallback;

    @NonNull
    public static PostsFragment newInstance(GMarkerMetadata gMarkerMetadata)
    {
        Bundle arguments = new Bundle();
        arguments.putParcelable(METADATA_KEY, gMarkerMetadata);

        PostsFragment postsFragment = new PostsFragment();
        postsFragment.setArguments(arguments);
        return postsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState)
    {
        viewModel = new ViewModelProvider(this).get(PostsFragmentViewModel.class);

        if (onDeselectItemClickListener != null)
        {
            binding.bottomToolbar.setNavigationOnClickListener(onDeselectItemClickListener);
        }

        if (onBackPressedCallback != null)
        {
            requireActivity().getOnBackPressedDispatcher()
                             .addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        }

        GMarkerMetadata gMarkerMetadata = requireArguments().getParcelable(METADATA_KEY);

        PostsAdapter postsAdapter = new PostsAdapter(requireActivity());

        RecyclerView recyclerView = binding.postsRecyclerView;
        recyclerView.setAdapter(postsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        postsAdapter.setOnCommentsButtonClickListener((v, post) ->
        {
            String name = CommentsFragment.class.getSimpleName();
            getParentFragmentManager().beginTransaction()
                                      .replace(R.id.bottom_container,
                                              CommentsFragment.newInstance(post))
                                      .addToBackStack(name)
                                      .commit();
        });

        Toolbar bottomToolbar = binding.bottomToolbar;
        bottomToolbar.setTitle(gMarkerMetadata.getTitle());

        if (gMarkerMetadata.getType() == GMarkerMetadata.COMMON_MARKER)
        {
            LiveData<Post> postByGMarker = viewModel.getPostByGMarker(gMarkerMetadata);
            if (!postByGMarker.hasActiveObservers())
            {
                postByGMarker.observeForever(postsAdapter::addPost);
            }
        }
        else if (gMarkerMetadata.getType() == GMarkerMetadata.ADDRESS_MARKER)
        {
            if (gMarkerMetadata instanceof GMarkerWithChildrenMetadata)
            {
                List<GMarkerMetadata> gMarkerMetadataList = ((GMarkerWithChildrenMetadata) gMarkerMetadata).getGMarkerMetadata();
                if (gMarkerMetadataList != null)
                {
                    for (GMarkerMetadata metadata : gMarkerMetadataList)
                    {
                        LiveData<Post> postByGMarker = viewModel.getPostByGMarker(metadata);
                        if (!postByGMarker.hasActiveObservers())
                        {
                            postByGMarker.observeForever(postsAdapter::addPost);
                        }
                    }
                }
            }
        }
    }

    public void setOnDeselectItemClickListener(View.OnClickListener listener)
    {
        onDeselectItemClickListener = listener;
    }

    public void setOnBackPressedCallback(OnBackPressedCallback callback)
    {
        onBackPressedCallback = callback;
    }
}
