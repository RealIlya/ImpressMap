package com.example.impressmap.adapter.posts;

import android.view.View;

import com.example.impressmap.model.data.Post;

public interface OnCommentsButtonClickListener
{
    void onCommentClick(View view,
                        Post post);
}
