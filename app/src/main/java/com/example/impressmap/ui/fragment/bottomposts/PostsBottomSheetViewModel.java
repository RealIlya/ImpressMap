package com.example.impressmap.ui.fragment.bottomposts;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

//TODO not work
public class PostsBottomSheetViewModel extends ViewModel
{
    private int lastState = BottomSheetBehavior.STATE_HIDDEN;

    public PostsBottomSheetViewModel()
    {
    }

    public int getLastState()
    {
        return lastState;
    }

    public void setLastState(int lastState)
    {
        this.lastState = lastState;
    }
}
