package com.example.impressmap.util;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.example.impressmap.R;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.TimeZone;

public abstract class DateStrings
{
    /**
     * <b>Returns formatted dateTime string</b>
     * <p>If dateTime equals now dateTime returns "Today at {time}"</p>
     * <p>If difference between datetime and now dateTime <= 7 returns "{days} ago"</p>
     * <p>If dateTime's year equals now dateTime's year returns "{month's number} {month}"</p>
     * <p>In other cases returns dateTime in dd.MM.yyyy format depends on Device's locale</p>
     */
    public static String getDateString(Resources resources,
                                       @NonNull LocalDateTime dateTime)
    {
        LocalDateTime nowDateTime = LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());

        if (nowDateTime.getYear() == dateTime.getYear())
        {
            long diff = Math.abs(nowDateTime.getDayOfYear() - dateTime.getDayOfYear());

            if (diff <= 7)
            {
                if (diff == 0)
                {
                    return resources.getString(R.string.today,
                            dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
                }

                return resources.getString(R.string.ago,
                        resources.getQuantityString(R.plurals.days, (int) diff, diff));
            }

            Month month = dateTime.getMonth();
            return month.getLong(ChronoField.MONTH_OF_YEAR) + " " + month.getDisplayName(
                    TextStyle.FULL, Locale.getDefault());
        }

        return dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
    }
}
