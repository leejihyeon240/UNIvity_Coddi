package com.example.univity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class CalTodayDecorator implements DayViewDecorator {

    private CalendarDay date;
    private final Drawable drawable;

    public CalTodayDecorator(Activity context) {
        date = CalendarDay.today();
        drawable = context.getResources().getDrawable(R.drawable.cal_ellipse_today);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.addSpan(new StyleSpan(Typeface.BOLD));
        //view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.rgb(7,7,7)));
        view.setBackgroundDrawable(drawable);
    }

    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}