package net.sourcefusion.agiletools.sprintcalendar

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.TextView
import net.sourcefusion.agiletools.sprintcalendar.PresenceType.FULL_DAY
import net.sourcefusion.agiletools.sprintcalendar.PresenceType.NONE
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.joda.time.LocalDate
import kotlin.properties.Delegates

public class CalendarActivityUI(var sprintCalendar: SprintCalendar) : AnkoComponent<MainActivity> {

    val colorDarkBorder = 0x303F9F.opaque
    val colorLightBorder = 0x7684cf.opaque
    val colorMainBackground = 0xFFFFFF.opaque
    val colorRed = 0xFF0000.opaque

    constructor() : this(SprintCalendar(
            Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
            DefaultDateProvider(),
            DefaultHolidayProvider(LocalDate(2016, 1, 22)))) {
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 27), PresenceType.HALF_DAY)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 28), NONE)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 29), NONE)
    }

    val BASIC_HEADER_STYLE = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                textSize = 26f
                textColor = 0x000000.opaque
            }
        }
    }

    val WEEK_HEADER_STYLE = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                BASIC_HEADER_STYLE(view)
                backgroundColor = colorMainBackground
                leftPadding = dip(10)
                gravity = android.view.Gravity.CENTER_VERTICAL + android.view.Gravity.LEFT
            }
        }
    }

    val DAY_HEADER_STYLE = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                BASIC_HEADER_STYLE(view)
                gravity = Gravity.CENTER
            }
        }
    }

    val DAY_TEAM_STYLE = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                textSize = 18f
                textColor = 0x33.gray.opaque
                gravity = Gravity.CENTER
            }
        }
    }

    var drawablePresenceNone: Drawable by Delegates.notNull<Drawable>()
    var drawablePresenceHalfDay: Drawable by Delegates.notNull<Drawable>()
    var drawablePresenceFullDay: Drawable by Delegates.notNull<Drawable>()

    fun memberDayBackground(member: TeamMember, day: SprintDay) =
            if (day.isHoliday)
                drawablePresenceNone
            else
                when (member.presence(day.date)) {
                    NONE -> drawablePresenceNone
                    PresenceType.HALF_DAY -> drawablePresenceHalfDay
                    FULL_DAY -> drawablePresenceFullDay
                }

    fun <T> readDrawable(ui: AnkoContext<T>, resID: Int): Drawable {
        val d = ui.resources.getDrawable(resID)
        d.setLevel(5000)
        return d
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        drawablePresenceNone = readDrawable(ui, R.drawable.presense_none)
        drawablePresenceHalfDay = readDrawable(ui, R.drawable.precense_half_day)
        drawablePresenceFullDay = readDrawable(ui, R.drawable.precense_full_day)

        linearLayout {
            val memberDayViews = hashMapOf<Pair<TeamMember, Int>, View>()
            val sCalendarView = calendarLayout {
                for (week in 0..1) {
                    rightPadding = dip(6)

                    weekLayout {
                        weekHeaderLayout {
                            textView("week ${week + 1}").style(WEEK_HEADER_STYLE).lparams { weight = 2f; width = matchParent; height = 0 }
                            for (member in sprintCalendar.team) {
                                textView {
                                    text = member.name
                                }.style(WEEK_HEADER_STYLE)
                                        .lparams { weight = 1f; width = matchParent; height = 0; topMargin = dip(2) }
                            }
                        }.lparams { weight = 1f; width = 0; height = matchParent }
                        for (day in (0 + week * 5)..(4 + week * 5)) {
                            val sDay = sprintCalendar.day(day)
                            frameLayout {
                                dayLayout {
                                    textView {
                                        backgroundResource = if (sprintCalendar.day(day).isHoliday) R.drawable.presense_none else R.drawable.precense_full_day
                                        text = "${Format.date(sDay.date)}"
                                    }.style(DAY_HEADER_STYLE).lparams { weight = 2f; width = matchParent; height = 0; gravity = Gravity.CENTER }
                                    for (member in sprintCalendar.team) {
                                        val memberDayView = memberDayView {
                                            background = memberDayBackground(member, sDay)
                                            text = ""
                                        }.lparams { width = matchParent; height = 0; weight = 1f; topMargin = dip(2) }
                                        memberDayView.onLongClick { sprintCalendar.onMemberDay(member, sDay) }
                                        memberDayViews[Pair(member, sDay.index)] = memberDayView
                                    }
                                }.lparams { width = matchParent; height = matchParent }

                                view {
                                    backgroundResource = R.drawable.today_overlay
                                    visibility = if (sprintCalendar.day(day).isToday) View.VISIBLE else View.GONE
                                }
                            }.lparams { weight = 1f; width = 0; height = matchParent; leftMargin = dip(2) }
                        }
                    }.lparams { weight = 1f; width = matchParent; height = 0; backgroundColor = colorDarkBorder; margin = dip(2) }
                }
            }.lparams { weight = 12f; width = 0; height = matchParent; backgroundColor = colorDarkBorder }


            sprintCalendar.onMemberDayChange { member: TeamMember, day: SprintDay ->
                memberDayViews[Pair(member, day.index)]?.background = memberDayBackground(member, day)
            }

            summaryLayout {
                textView("Anko")
            }.lparams { weight = 4f; width = 0; height = matchParent; backgroundColor = colorLightBorder }

        }
    }


    inline fun ViewManager.calendarLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.weekHeaderLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.dayLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.summaryLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.weekLayout(init: _LinearLayout.() -> Unit): LinearLayout = linearLayout(init)

    inline fun ViewManager.memberDayView(init: android.widget.TextView.() -> Unit): TextView = textView(init).style(DAY_TEAM_STYLE)

}