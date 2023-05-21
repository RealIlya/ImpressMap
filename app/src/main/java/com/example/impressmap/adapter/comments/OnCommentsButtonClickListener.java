package com.example.impressmap.adapter.comments;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.impressmap.model.data.Comment;

public interface OnCommentsButtonClickListener
{
    void onCommentClick(View view,
                        Comment comment);
}
