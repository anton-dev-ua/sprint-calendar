package net.sourcefusion.agiletools.sprintcalendar;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityRule.getActivity();

    }

    @Test
    public void testSprintDates() {

        assertThat(getText(R.id.headerWeek1Day1), is("04.01.2016"));
//        assertEquals("05.01.2016", getText(R.id.headerWeek1Day2));
//        assertEquals("06.01.2016", getText(R.id.headerWeek1Day3));
//        assertEquals("07.01.2016", getText(R.id.headerWeek1Day4));
//        assertEquals("08.01.2016", getText(R.id.headerWeek1Day5));
//
//        assertEquals("11.01.2016", getText(R.id.headerWeek2Day1));
//        assertEquals("12.01.2016", getText(R.id.headerWeek2Day2));
//        assertEquals("13.01.2016", getText(R.id.headerWeek2Day3));
//        assertEquals("14.01.2016", getText(R.id.headerWeek2Day4));
//        assertEquals("15.01.2016", getText(R.id.headerWeek2Day5));
    }

    private String getText(int vewId) {
        return "" + ((TextView) mainActivity.findViewById(vewId)).getText();
    }

}
