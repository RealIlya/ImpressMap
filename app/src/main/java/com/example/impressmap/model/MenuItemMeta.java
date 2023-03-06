package com.example.impressmap.model;

public class MenuItemMeta
{

    private OnClickListener clickListener;

    public MenuItemMeta(OnClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public OnClickListener getClickListener()
    {
        return clickListener;
    }

    public interface OnClickListener
    {
        void onClick();
    }
}
