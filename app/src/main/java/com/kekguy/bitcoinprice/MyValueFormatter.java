package com.kekguy.bitcoinprice;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.time.LocalTime;
import java.util.Date;

public class MyValueFormatter extends ValueFormatter implements IAxisValueFormatter, IValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return null;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String getFormattedValue(float value) {
        Log.d("index", Float.toString(value));
        //String localTime = LocalTime.of(Int(value), 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        String currentDateandTime = sdf.format(new Date((long) value));
        return currentDateandTime;
    }
}
