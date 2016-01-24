package net.sourcefusion.agiletools.sprintcalendar

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
    val colorHeaderText = 0x000000.opaque
    val sizeHeaderText = 26f

    constructor() : this(SprintCalendar(
            Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
            DefaultDateProvider(),
            DefaultHolidayProvider(LocalDate(2016, 1, 22)))) {
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 27), PresenceType.HALF_DAY)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 28), NONE)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 29), NONE)
    }

    var drawablePresenceNone: Drawable by Delegates.notNull<Drawable>()
    var drawablePresenceHalfDay: Drawable by Delegates.notNull<Drawable>()
    var drawablePresenceFullDay: Drawable by Delegates.notNull<Drawable>()
    val memberDayViews = hashMapOf<Pair<TeamMember, Int>, View>()
    val dayViews = hashMapOf<Int, View>()

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        drawablePresenceNone = readDrawable(ui, R.drawable.presense_none)
        drawablePresenceHalfDay = readDrawable(ui, R.drawable.precense_half_day)
        drawablePresenceFullDay = readDrawable(ui, R.drawable.precense_full_day)

        linearLayout {
            id = 1001
            calendarLayout {
                rightPadding = dip(6)

                for (week in 0..1) {

                    weekLayout {
                        weekHeaderLayout {

                            textView("Week ${week + 1}") {
                                backgroundColor = colorMainBackground
                                textSize = sizeHeaderText
                                textColor = colorHeaderText
                                leftPadding = dip(10)
                                gravity = Gravity.CENTER_VERTICAL + Gravity.LEFT
                            }.lparams {
                                width = matchParent
                                height = 0
                                weight = 2f
                            }

                            for (member in sprintCalendar.team) {
                                textView {
                                    text = member.name
                                    backgroundColor = colorMainBackground
                                    textSize = sizeHeaderText
                                    textColor = colorHeaderText
                                    leftPadding = dip(10)
                                    gravity = Gravity.CENTER_VERTICAL + Gravity.LEFT
                                }.lparams {
                                    weight = 1f
                                    width = matchParent
                                    height = 0
                                    topMargin = dip(2)
                                }
                            }

                        }.lparams { weight = 1f; width = 0; height = matchParent }

                        for (dayIndex in (0 + week * 5)..(4 + week * 5)) {
                            val day = sprintCalendar.day(dayIndex)

                            frameLayout {

                                dayLayout {

                                    dayViews[day.index] = textView {
                                        text = "${Format.date(day.date)}"
                                        background = dayBackground(day)
                                        textSize = sizeHeaderText
                                        textColor = colorHeaderText
                                        gravity = Gravity.CENTER
                                    }.lparams {
                                        weight = 2f
                                        width = matchParent
                                        height = 0
                                    }.onLongClick { sprintCalendar.onDay(day) }

                                    for (member in sprintCalendar.team) {
                                        memberDayViews[Pair(member, day.index)] = textView {
                                            background = memberDayBackground(member, day)
                                        }.lparams {
                                            width = matchParent
                                            height = 0
                                            weight = 1f
                                            topMargin = dip(2)
                                        }.onLongClick { sprintCalendar.onMemberDay(member, day) }
                                    }

                                }.lparams { width = matchParent; height = matchParent }

                                view {
                                    backgroundResource = R.drawable.today_overlay
                                    visibility = if (sprintCalendar.day(dayIndex).isToday) View.VISIBLE else View.GONE
                                }

                            }.lparams { weight = 1f; width = 0; height = matchParent; leftMargin = dip(2) }
                        }

                    }.lparams { weight = 1f; width = matchParent; height = 0; backgroundColor = colorDarkBorder; margin = dip(2) }

                }
            }.lparams { weight = 12f; width = 0; height = matchParent; backgroundColor = colorDarkBorder }




            summaryLayout {
                textView("Anko")
            }.lparams { weight = 4f; width = 0; height = matchParent; backgroundColor = colorLightBorder }


            sprintCalendar
                    .onMemberDayChange { member: TeamMember, day: SprintDay ->
                        memberDayViews[Pair(member, day.index)]?.background = memberDayBackground(member, day)
                    }
                    .onDayChange { day: SprintDay ->
                        dayViews[day.index]?.background = dayBackground(day)
                        for (member in sprintCalendar.team) {
                            memberDayViews[Pair(member, day.index)]?.background = memberDayBackground(member, day)
                        }
                    }

        }
    }


    private fun memberDayBackground(member: TeamMember, day: SprintDay) =
            if (day.isHoliday)
                drawablePresenceNone
            else
                when (member.presence(day.date)) {
                    NONE -> drawablePresenceNone
                    PresenceType.HALF_DAY -> drawablePresenceHalfDay
                    FULL_DAY -> drawablePresenceFullDay
                }

    private fun dayBackground(day: SprintDay) = if (day.isHoliday) drawablePresenceNone else drawablePresenceFullDay

    private fun <T> readDrawable(ui: AnkoContext<T>, resID: Int): Drawable {
        val d = ui.resources.getDrawable(resID)
        d.setLevel(5000)
        return d
    }

    inline fun ViewManager.calendarLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.weekHeaderLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.dayLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.summaryLayout(init: _LinearLayout.() -> Unit): LinearLayout = verticalLayout(init)
    inline fun ViewManager.weekLayout(init: _LinearLayout.() -> Unit): LinearLayout = linearLayout(init)
    //    inline fun ViewManager.memberDayView(init: android.widget.TextView.() -> Unit): TextView = textView(init).style(DAY_TEAM_STYLE)

    fun View.onLongClick(l: (v: android.view.View?) -> Boolean): View {
        setOnLongClickListener(l)
        return this
    }
}