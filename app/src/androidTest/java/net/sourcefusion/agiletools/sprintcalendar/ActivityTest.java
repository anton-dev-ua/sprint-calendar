package net.sourcefusion.agiletools.sprintcalendar;

import android.graphics.drawable.Drawable;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kotlin.Pair;

import static android.support.test.espresso.action.ViewActions.longClick;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityTest {

    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityRule.getActivity();

    }

    @Test
    public void longClickChangesMemberPresenceAndViewBackground() {

        SprintCalendar sprintCalendar = mainActivity.getSprintCalendar();
        TeamMember member = sprintCalendar.getTeam().member(3);
        SprintDay day = sprintCalendar.day(2);
        View view = mainActivity.getUi().getMemberDayViews().get(new Pair<>(member, day.getIndex()));

        assertThat(member.presence(day.getDate()), is(PresenceType.FULL_DAY));
        Drawable prevBackground = view.getBackground();

        onView(view).perform(longClick());

        assertThat(member.presence(day.getDate()), is(PresenceType.NONE));
        assertThat(view.getBackground(), is(not(prevBackground)));

    }

    private ViewInteraction onView(View view) {
        return Espresso.onView(equalTo(view));
    }

}
