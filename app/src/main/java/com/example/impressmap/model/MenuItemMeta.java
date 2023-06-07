package com.example.impressmap.model;

public class MenuItemMeta
{
    // Все переменные которые присваиваются один раз и больше не изменяются стоит помечать как final
    // Учитвая что это модель данных, то изменяться оно врядли будет.
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
