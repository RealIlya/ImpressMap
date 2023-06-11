package com.example.impressmap.model.data.gcircle;

import android.content.Context;

import com.example.impressmap.R;
import com.example.impressmap.model.data.GCircleMeta;
import com.example.impressmap.util.Converter;
import com.google.android.gms.maps.model.Circle;

public class CommonGCircle extends GCircle
{
    public CommonGCircle(Context context,
                         Circle circle,
                         GCircleMeta gCircleMeta)
    {
        super(circle, gCircleMeta,
                Converter.getAttributeColor(context, R.attr.backgroundSelectedMarker),
                Converter.getAttributeColor(context, R.attr.backgroundDeselectedMarker),
                Converter.getAttributeColor(context, R.attr.backgroundToolbar));
    }
}
