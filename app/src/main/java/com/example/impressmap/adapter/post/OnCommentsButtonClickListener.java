package com.example.impressmap.adapter.post;

import android.view.View;

import com.example.impressmap.model.data.Post;

public interface OnCommentsButtonClickListener
{
    void onCommentClick(View view,
                        Post post);
}
