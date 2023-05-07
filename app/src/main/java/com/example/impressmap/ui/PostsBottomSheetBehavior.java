package com.example.impressmap.ui;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class PostsBottomSheetBehavior<T extends View>
{
    private final BottomSheetBehavior<T> bottomSheetBehavior;
    private Animation animation = new Animation()
    {
        @Override
        public void onStateChanged(int newState)
        {

        }

        @Override
        public void onSlide(float slideOffset)
        {

        }
    };

    public PostsBottomSheetBehavior(BottomSheetBehavior<T> bottomSheetBehavior)
    {
        this.bottomSheetBehavior = bottomSheetBehavior;

        bottomSheetBehavior.setHalfExpandedRatio(0.4f);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.addBottomSheetCallback(new PostsBottomSheetCallback());
        hide();
    }

    private void setState(int state)
    {
        bottomSheetBehavior.setState(state);
    }

    public void hide()
    {
        setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void showLittle()
    {
        setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void showHalf()
    {
        setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    public void showFull()
    {
        setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void setAnimation(Animation animation)
    {
        this.animation = animation;
    }

    public interface Animation
    {
        void onStateChanged(int newState);

        void onSlide(float slideOffset);
    }

    private class PostsBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback
    {
        private boolean collapsing = false;
        private boolean expanding = false;
        private boolean dragging = false;
        private float oldOffset = 0f;

        private void setDefaultState()
        {
            collapsing = false;
            expanding = false;
            dragging = false;
        }

        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   int newState)
        {
            switch (newState)
            {
                case BottomSheetBehavior.STATE_EXPANDED:
                case BottomSheetBehavior.STATE_HIDDEN:
                    setDefaultState();
                    break;
                case BottomSheetBehavior.STATE_DRAGGING:
                    dragging = true;
                    break;
            }

            animation.onStateChanged(newState);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet,
                            float slideOffset)
        {
            expanding = oldOffset < slideOffset;
            collapsing = oldOffset > slideOffset;
            if ((slideOffset < 0.8 && slideOffset >= 0.45 && collapsing || slideOffset < 0.3 && slideOffset >= 0 && expanding) && dragging)
            {
                showHalf();
            }

            oldOffset = slideOffset;

            // fix
            if (slideOffset < 0 && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            {
                showLittle();
            }

            animation.onSlide(slideOffset);
        }
    }
}
