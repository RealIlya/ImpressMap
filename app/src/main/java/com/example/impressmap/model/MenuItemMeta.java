package com.example.impressmap.model;

public class MenuItemMeta
{
    private final OnClickListener listener;

    public MenuItemMeta(OnClickListener listener)
    {
        this.listener = listener;
    }

    public void onClick()
    {
        if (listener != null)
        {
            listener.onClick();
        }
    }

    public interface OnClickListener
    {
        void onClick();
    }
}
