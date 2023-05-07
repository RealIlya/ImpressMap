package com.example.impressmap.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

public class Converter
{
    public static Bitmap drawableIdToBitmap(Context context,
                                            @DrawableRes int drawableId)
    {
        Resources resources = context.getResources();
        Drawable drawable = ResourcesCompat.getDrawable(resources, drawableId, context.getTheme());
        return drawableToBitmap(drawable);
    }


    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
        {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private static int getAttribute(@NonNull Context context,
                                    @AttrRes int resId)
    {
        TypedValue typedValue = new TypedValue();
        boolean successful = context.getTheme().resolveAttribute(resId, typedValue, true);
        return successful ? typedValue.resourceId : 0;
    }

    @ColorInt
    public static int getAttributeColor(@NonNull Context context,
                                        @AttrRes int resId)
    {
        return ResourcesCompat.getColor(context.getResources(), getAttribute(context, resId),
                context.getTheme());
    }

    public static Drawable getDrawable(@NonNull Context context,
                                       @DrawableRes int resId)
    {
        return ResourcesCompat.getDrawable(context.getResources(), resId, context.getTheme());
    }

    public int getAttributeColorWithAlpha(Context context,
                                          @AttrRes int resId,
                                          float ratio)
    {
        return getColorWithAlpha(getAttributeColor(context, resId), ratio);
    }

    public int getColorWithAlpha(@ColorInt int color,
                                 float ratio)
    {
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(alpha, r, g, b);
    }
}
