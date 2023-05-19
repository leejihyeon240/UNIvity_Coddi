package com.example.univity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class CalSelectedDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public CalSelectedDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.cal_my_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
