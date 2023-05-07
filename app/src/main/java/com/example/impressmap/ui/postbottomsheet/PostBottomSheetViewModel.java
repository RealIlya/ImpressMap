package com.example.impressmap.ui.postbottomsheet;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

//TODO not work
public class PostBottomSheetViewModel extends ViewModel
{
    private int lastState = BottomSheetBehavior.STATE_HIDDEN;

    public PostBottomSheetViewModel()
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
