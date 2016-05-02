package net.sourcefusion.agiletools.sprintcalendar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.doubleClick
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.matcher.ViewMatchers.withTagValue
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import com.orm.SugarRecord
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils
import net.sourcefusion.agiletools.sprintcalendar.persisting.sugar.TeamMemberPresenceEntry
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher
import org.joda.time.LocalDate
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.properties.Delegates


@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityTest {


    @Rule @JvmField
    var mActivityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private var mainActivity by Delegates.notNull<MainActivity>()
    private var sprintCalendar by Delegates.notNull<SprintCalendar>()

    private val TODAY = LocalDate(2016, 1, 4)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        println("test setup")
        Injector.holidayProvider = BasicHolidayProvider()
        Injector.dateProvider = object : DateProvider {
            override fun isToday(date: LocalDate): Boolean = date.equals(TODAY)

            override val today: LocalDate
                get() = TODAY

        }
        SugarRecord.find(TeamMemberPresenceEntry::class.java, "date < ?", "${PersistingUtils.toDate(TODAY.plusDays(14)).time}").forEach { it.delete() }
        mActivityRule.launchActivity(null)
        mainActivity = mActivityRule.activity
        sprintCalendar = mainActivity.sprintCalendar

        val member = sprintCalendar.team.member(3)
        val day = sprintCalendar.day(2)
        member.setPresence(day.date, PresenceType.FULL_DAY)
    }

    @Test
    fun longClickOnMemberDayChangesMemberPresenceAndViewBackground() {

        val drawablePresenceNone = readDrawable(mainActivity, R.drawable.presense_none, 10000)

        val member = sprintCalendar.team.member(3)
        val day = sprintCalendar.day(2)

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .perform(longClick())
                .check { view, exception ->
                    assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.ABSENT))

    }

    @Test
    fun doubleClickOnMemberDayChangesMemberPresenceAndViewBackground() {

        val drawableHalfDay = readDrawable(mainActivity, R.drawable.precense_half_day, 10000)
        val drawableHalfDayMoring = readDrawable(mainActivity, R.drawable.precense_half_day_morning, 10000)

        val member = sprintCalendar.team.member(3)
        val day = sprintCalendar.day(2)

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .perform(doubleClick())
                .check { view, exception ->
                    assertThat(toBitmap(view.background).sameAs(toBitmap(drawableHalfDay)), `is`(true))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.HALF_DAY))

        onView(withTag("member-3-day-2"))
                .perform(doubleClick())
                .check { view, exception ->
                    assertThat(toBitmap(view.background).sameAs(toBitmap(drawableHalfDayMoring)), `is`(true))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.HALF_DAY_MORNING))

    }

    @Test
    fun longClickOnDayChangesPresenceOfAllMembersAndMakesDayHoliday() {

        val drawablePresenceNone = readDrawable(mainActivity, R.drawable.presense_none, 10000)

        onView(withTag("day-3"))
                .perform(longClick())
                .check { view, exception ->
                    assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true))
                }

        assertThat(sprintCalendar.day(3).isHoliday, equalTo(true))

        onView(withTag("member-0-day-3")).check { view, exception -> assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true)) }
        onView(withTag("member-1-day-3")).check { view, exception -> assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true)) }
        onView(withTag("member-2-day-3")).check { view, exception -> assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true)) }
        onView(withTag("member-3-day-3")).check { view, exception -> assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true)) }
        onView(withTag("member-4-day-3")).check { view, exception -> assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true)) }
        onView(withTag("member-5-day-3")).check { view, exception -> assertThat(toBitmap(view.background).sameAs(toBitmap(drawablePresenceNone)), `is`(true)) }
    }

    private fun toBitmap(drawable: Drawable): Bitmap {
        var result: Bitmap
        if (drawable is BitmapDrawable) {
            result = drawable.bitmap
        } else {
            var width = drawable.intrinsicWidth;
            var height = drawable.intrinsicHeight;
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            val canvas = Canvas(result);
            val newDrawable = drawable.constantState.newDrawable()
            newDrawable.setBounds(0, 0, canvas.width, canvas.height);
            newDrawable.draw(canvas);
        }
        return result
    }

    private fun withTag(tagValue: Any): Matcher<View> {
        return withTagValue(equalTo(tagValue))
    }

    private fun readDrawable(ui: Context, resID: Int, level: Int): Drawable {
        val d = ui.resources.getDrawable(resID)
        d.level = level
        return d
    }

}
