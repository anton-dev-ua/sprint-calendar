package com.example.anton.sprintcalendar;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class ActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public ActivityTest() {
        super(MainActivity.class);
    }

    public ActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        mainActivity = getActivity();

    }

    public void testSprintDates() {

        assertEquals("04.01.2016", getText(R.id.headerWeek1Day1));
        assertEquals("05.01.2016", getText(R.id.headerWeek1Day2));
        assertEquals("06.01.2016", getText(R.id.headerWeek1Day3));
        assertEquals("07.01.2016", getText(R.id.headerWeek1Day4));
        assertEquals("08.01.2016", getText(R.id.headerWeek1Day5));

        assertEquals("11.01.2016", getText(R.id.headerWeek2Day1));
        assertEquals("12.01.2016", getText(R.id.headerWeek2Day2));
        assertEquals("13.01.2016", getText(R.id.headerWeek2Day3));
        assertEquals("14.01.2016", getText(R.id.headerWeek2Day4));
        assertEquals("15.01.2016", getText(R.id.headerWeek2Day5));
    }

    private CharSequence getText(int vewId) {
        return ((TextView) mainActivity.findViewById(vewId)).getText();
    }

}
