package com.example.impressmap.util;

import android.content.res.Resources;

import com.example.impressmap.R;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.TimeZone;

public class DateStrings
{
    public static String getDateString(Resources resources,
                                       LocalDateTime dateTime)
    {
        LocalDateTime nowDateTime = LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());

        if (nowDateTime.getYear() == dateTime.getYear())
        {
            long diff = Math.abs(nowDateTime.getDayOfYear() - dateTime.getDayOfYear());

            if (diff <= 7)
            {
                if (diff == 0)
                {
                    return resources.getString(R.string.today);
                }

                return resources.getQuantityString(R.plurals.days, (int) diff, diff);
            }

            Month month = dateTime.getMonth();
            return month.getLong(ChronoField.MONTH_OF_YEAR) + " " + month.getDisplayName(
                    TextStyle.FULL, Locale.getDefault());
        }

        return dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }
}
