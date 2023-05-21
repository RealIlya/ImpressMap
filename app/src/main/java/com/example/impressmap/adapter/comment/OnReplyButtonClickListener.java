package com.example.impressmap.adapter.comment;

import android.view.View;

import com.example.impressmap.model.data.Comment;

public interface OnReplyButtonClickListener
{
    void onReplyClick(View view,
                      Comment comment);
}
