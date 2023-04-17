package com.example.impressmap.model;

public class MenuItemMeta
{

    private OnClickListener clickListener;

    public MenuItemMeta(OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public void onClick()
    {
        if (clickListener != null)
        {
            clickListener.onClick();
        }
    }

    public interface OnClickListener
    {
        void onClick();
    }
}
