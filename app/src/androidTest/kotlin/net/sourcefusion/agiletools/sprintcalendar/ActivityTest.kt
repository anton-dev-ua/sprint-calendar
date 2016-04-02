package net.sourcefusion.agiletools.sprintcalendar

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.matcher.ViewMatchers.withTagValue
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.graphics.drawable.Drawable
import com.orm.SugarRecord
import net.sourcefusion.agiletools.sprintcalendar.persisting.PersistingUtils
import net.sourcefusion.agiletools.sprintcalendar.persisting.sugar.TeamMemberPresenceEntry
import org.joda.time.LocalDate
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

        val member = sprintCalendar.team.member(3)
        val day = sprintCalendar.day(2)
        var prevBackground: Drawable? = null

        assertThat(member.presence(day.date), equalTo(PresenceType.FULL_DAY))

        onView(withTag("member-3-day-2"))
                .check({ view, e -> prevBackground = view.background })
                .perform(longClick())
                .check { view, exception ->
                    assertThat(view.background, not(equalTo(prevBackground)))
                }

        assertThat(member.presence(day.date), equalTo(PresenceType.ABSENT))

    }

    @Test
    fun longClickOnDayChangesPresenceOfAllMembersAndMakesDayHoliday() {
        var prevBackground: Drawable? = null
        var newBackground: Drawable? = null

        onView(withTag("day-3"))
                .check({ view, e -> prevBackground = view.background })
                .perform(longClick())
                .check { view, exception ->
                    newBackground = view.background
                    assertThat(newBackground, not(equalTo(prevBackground)))
                }

        assertThat(sprintCalendar.day(3).isHoliday, equalTo(true))

        onView(withTag("member-0-day-3")).check { view, exception -> assertThat(view.background, equalTo(newBackground)) }
        onView(withTag("member-1-day-3")).check { view, exception -> assertThat(view.background, equalTo(newBackground)) }
        onView(withTag("member-2-day-3")).check { view, exception -> assertThat(view.background, equalTo(newBackground)) }
        onView(withTag("member-3-day-3")).check { view, exception -> assertThat(view.background, equalTo(newBackground)) }
        onView(withTag("member-4-day-3")).check { view, exception -> assertThat(view.background, equalTo(newBackground)) }
        onView(withTag("member-5-day-3")).check { view, exception -> assertThat(view.background, equalTo(newBackground)) }
    }

    private fun withTag(tagValue: Any): Matcher<View> {
        return withTagValue(equalTo(tagValue))
    }

}
