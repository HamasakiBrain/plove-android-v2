package com.octarine.plove.common;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.lang.reflect.Field;

/**
 * Created by rustam on 27.02.17.
 */

public class RangeTimePickerDialog extends TimePickerDialog {

    private int mMinHour = -1;
    private int mMinMinute = -1;
    private int mMaxHour = 100;
    private int mMaxMinute = 100;
    private int mCurrentHour;
    private int mCurrentMinute;

    public RangeTimePickerDialog(Context context, int themeResId, OnTimeSetListener callBack, int hourOfDay,
                                 int minute, boolean is24HourView) {
        super(context, themeResId, callBack, hourOfDay, minute, is24HourView);
        mCurrentHour = hourOfDay;
        mCurrentMinute = minute;
        // Somehow the onTimeChangedListener is not set by TimePickerDialog
        // in some Android Versions, so, Adding the listener using
        // reflections
        try {
            Class<?> superclass = getClass().getSuperclass();
            Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
            mTimePickerField.setAccessible(true);
            TimePicker mTimePicker = (TimePicker) mTimePickerField.get(this);
            mTimePicker.setOnTimeChangedListener(this);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public void setMin(int hour, int minute) {
        mMinHour = hour;
        mMinMinute = minute;
    }

    public void setMax(int hour, int minute) {
        mMaxHour = hour;
        mMaxMinute = minute;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);
        boolean validTime;
        if (((hourOfDay < mMinHour ) || (hourOfDay == mMinHour && minute < mMinMinute))
                || ((hourOfDay > mMaxHour) || (hourOfDay == mMaxHour && minute > mMaxMinute))) {
            validTime = false;
        } else {
            validTime = true;
        }
        if (validTime) {
            mCurrentHour = hourOfDay;
            mCurrentMinute = minute;
        } else {
            updateTime(mCurrentHour, mCurrentMinute);
        }
    }
}