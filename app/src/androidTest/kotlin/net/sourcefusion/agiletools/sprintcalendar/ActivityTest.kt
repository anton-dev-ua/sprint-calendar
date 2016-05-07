package net.sourcefusion.agiletools.sprintcalendar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.withTagValue
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import com.orm.SugarRecord
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils
import net.sourcefusion.agiletools.sprintcalendar.persisting.sugar.TeamMemberPresenceEntry
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.joda.time.LocalDate
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityTest {


    @Rule @JvmField
    var mActivityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private var mainActivity by Delegates.notNull<MainActivity>()
    private var sprintCalendar by Delegates.notNull<SprintCalendar>()

    val member by lazy { sprintCalendar.team.member(3) }
    val day by lazy { sprintCalendar.day(2) }

    private val backgrounds by lazy {
        listOf(
                BitmapWrapper(R.drawable.presence_none),
                BitmapWrapper(R.drawable.presence_full_day),
                BitmapWrapper(R.drawable.presence_half_day_afternoon),
                BitmapWrapper(R.drawable.presence_half_day_morning),
                BitmapWrapper(R.drawable.presence_business_trip)
        )
    }

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

        member.setPresence(day.date, PresenceType.FULL_DAY)
    }

    @Test
    fun longClickOnMemberDayToggleFullDayPresenceAndChangesViewBackground() {

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .perform(longClick())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_none))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.ABSENT))

    }

    @Test
    fun swipeToRightOnMemberSetsFullDayAbsentAndChangesViewBackground() {

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_full_day))
                }
                .perform(swipeRight())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_none))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.ABSENT))

    }

    @Test
    fun secondSwipeToRightOnMemberSetsBusinessTripAndChangesViewBackground() {

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_full_day))
                }
                .perform(swipeRight())
                .perform(swipeRight())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_business_trip))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.BUSINESS_TRIP))

    }

    private fun backgroundOf(view: View) = backgrounds.firstOrNull { toBitmap(view.background).sameAs(it.bitmap) }

    private fun sameAs(resId: Int) = sameAs(BitmapWrapper(resId))

    private fun sameAs(expectedBackground: BitmapWrapper): Matcher<in BitmapWrapper?> {
        return object : BaseMatcher<BitmapWrapper>() {
            override fun describeTo(description: Description?) {
                description?.appendText("<${expectedBackground?.name}>")
            }

            override fun matches(actualBackground: Any?) = actualBackground is BitmapWrapper && actualBackground.bitmap.sameAs(expectedBackground?.bitmap)

        }
    }


    @Test
    fun swipeToLeftOnMemberDayToggleFullDayPresenceAndChangesViewBackground() {

        val latchDown = CountDownLatch(1)

        mainActivity.runOnUiThread({
            sprintCalendar.toggleFullDayMember(member, 2)
            latchDown.countDown()
        })

        latchDown.await(1, TimeUnit.SECONDS)

        assertThat(member.presence(day.date), equalTo(PresenceType.ABSENT))

        onView(withTag("member-3-day-2"))
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_none))
                }
                .perform(swipeLeft())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_full_day))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

    }

    @Test
    fun doubleClickOnMemberDayToggleHalfDayPresenceAndChangesViewBackground() {

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .perform(doubleClick())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_half_day_afternoon))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.HALF_DAY))

        onView(withTag("member-3-day-2"))
                .perform(doubleClick())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_half_day_morning))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.HALF_DAY_MORNING))

    }

    @Test
    fun longClickOnDayChangesPresenceOfAllMembersAndMakesDayHoliday() {

        onView(withTag("day-3"))
                .perform(longClick())
                .check { view, exception ->
                    assertThat(backgroundOf(view), sameAs(R.drawable.presence_none))
                }

        assertThat(sprintCalendar.day(3).isHoliday, equalTo(true))

        onView(withTag("member-0-day-3")).check { view, exception -> assertThat(backgroundOf(view), sameAs(R.drawable.presence_none)) }
        onView(withTag("member-1-day-3")).check { view, exception -> assertThat(backgroundOf(view), sameAs(R.drawable.presence_none)) }
        onView(withTag("member-2-day-3")).check { view, exception -> assertThat(backgroundOf(view), sameAs(R.drawable.presence_none)) }
        onView(withTag("member-3-day-3")).check { view, exception -> assertThat(backgroundOf(view), sameAs(R.drawable.presence_none)) }
        onView(withTag("member-4-day-3")).check { view, exception -> assertThat(backgroundOf(view), sameAs(R.drawable.presence_none)) }
        onView(withTag("member-5-day-3")).check { view, exception -> assertThat(backgroundOf(view), sameAs(R.drawable.presence_none)) }
    }


    private fun withTag(tagValue: Any): Matcher<View> {
        return withTagValue(equalTo(tagValue))
    }

    private fun readDrawable(ui: Context, resID: Int, level: Int): Drawable {
        val d = ui.resources.getDrawable(resID)
        d.level = level
        return d
    }

    private fun toBitmap(drawable: Drawable): Bitmap {
        var result: Bitmap
        if (drawable is BitmapDrawable) {
            result = drawable.bitmap
        } else {
            var width = drawable.intrinsicWidth;
            var height = drawable.intrinsicHeight;
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

    private inner class BitmapWrapper(resId: Int) {
        val bitmap by lazy { toBitmap(readDrawable(mainActivity, resId, 10000)) }
        val name by lazy { mainActivity.resources.getResourceEntryName(resId) }

        override fun toString() = name
    }
}


