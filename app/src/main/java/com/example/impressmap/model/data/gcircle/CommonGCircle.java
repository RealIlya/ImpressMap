package com.example.impressmap.model.data.gcircle;

import android.content.Context;

import com.example.impressmap.util.Converter;
import com.google.android.gms.maps.model.Circle;

public class CommonGCircle extends GCircle
{
    public CommonGCircle(Context context,
                         Circle circle)
    {
        super(context, circle, Converter.getAttributeColor(context,
                        com.google.android.material.R.attr.colorSecondaryVariant),
                Converter.getAttributeColor(context, android.R.attr.colorSecondary),
                Converter.getAttributeColor(context,
                        com.google.android.material.R.attr.colorSecondaryVariant));
    }
}
