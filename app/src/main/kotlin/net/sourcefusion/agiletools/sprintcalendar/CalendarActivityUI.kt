package net.sourcefusion.agiletools.sprintcalendar

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.LinearLayout
import android.widget.TextView
import net.sourcefusion.agiletools.sprintcalendar.PresenceType.*
import net.sourcefusion.agiletools.sprintcalendar.persisting.stubs.StubTeamRepository
import org.jetbrains.anko.*
import org.joda.time.LocalDate
import kotlin.properties.Delegates

class CalendarActivityUI(var sprintCalendar: SprintCalendar) : AnkoComponent<MainActivity> {

    private val colorDarkBorder = 0x303F9F.opaque
    private val colorLightBorder = 0x7684cf.opaque
    private val colorMainBackground = 0xFFFFFF.opaque
    private val colorHeaderText = 0x000000.opaque
    private val sizeHeaderText = 26f
    private val colorRed = 0xFF0000.opaque

    constructor() : this(SprintCalendar(
            StubTeamRepository(Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha"))),
            DefaultDateProvider(),
            BasicHolidayProvider(LocalDate(2016, 3, 8)))) {
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 27), HALF_DAY)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 28), ABSENT)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 29), ABSENT)
    }

    private var drawablePresenceNone: Drawable by Delegates.notNull<Drawable>()
    private var drawablePresenceHalfDay: Drawable by Delegates.notNull<Drawable>()
    private var drawablePresenceFullDay: Drawable by Delegates.notNull<Drawable>()
    private var drawableDaysLeftBackground: Drawable by Delegates.notNull<Drawable>()
    private var drawableHoursLeftBackground: Drawable by Delegates.notNull<Drawable>()
    private val memberDayViews = hashMapOf<Pair<TeamMember, Int>, View>()
    private val dayViews = hashMapOf<Int, View>()
    private var totalDaysView: TextView by Delegates.notNull()
    private var totalHoursView: TextView by Delegates.notNull()
    private var daysLeftView: TextView by Delegates.notNull()
    private var hoursLeftView: TextView by Delegates.notNull()

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        drawablePresenceNone = readDrawable(ui, R.drawable.presense_none)
        drawablePresenceHalfDay = readDrawable(ui, R.drawable.precense_half_day)
        drawablePresenceFullDay = readDrawable(ui, R.drawable.precense_full_day)
        drawableDaysLeftBackground = readDrawable(ui, R.drawable.days_left_background)
        drawableHoursLeftBackground = readDrawable(ui, R.drawable.days_left_background)

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
                                val view = textView {
                                    text = member.name
                                    tag = Pair(member, week)
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

                                ui.owner.registerForContextMenu(view)
                            }

                        }.lparams { weight = 1f; width = 0; height = matchParent }

                        for (dayIndex in (0 + week * 5)..(4 + week * 5)) {
                            val day = sprintCalendar.day(dayIndex)

                            frameLayout {

                                dayLayout {

                                    dayViews[day.index] = textView {
                                        tag = "day-$dayIndex"
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

                                    for ((memberIndex, member) in sprintCalendar.team.withIndex()) {
                                        memberDayViews[Pair(member, day.index)] = textView {
                                            tag = "member-$memberIndex-day-$dayIndex"
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
                verticalLayout {

                    textView {
                        text = "Current Sprint"
                        textSize = 26f
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        gravity = Gravity.CENTER_HORIZONTAL
                    }

                    linearLayout {
                        textView {
                            text = "${Format.date(sprintCalendar.firstDate)}"
                            textSize = 26f
                            gravity = Gravity.RIGHT
                        }.lparams { width = 0; height = wrapContent; weight = 1f }

                        textView {
                            text = "-"
                            textSize = 26f
                            gravity = Gravity.CENTER_HORIZONTAL
                            leftPadding = dip(10)
                            rightPadding = dip(10)
                        }.lparams { width = wrapContent; height = wrapContent; weight = 0f }

                        textView {
                            text = "${Format.date(sprintCalendar.lastDate)}"
                            textSize = 26f
                            gravity = Gravity.LEFT
                        }.lparams { width = 0; height = wrapContent; weight = 1f }

                    }.lparams {
                        width = matchParent
                        height = wrapContent
                    }

                    linearLayout {

                        textView {
                            text = "days:"
                            textSize = 26f
                            leftPadding = dip(10)
                        }.lparams { width = wrapContent; height = wrapContent }

                        totalDaysView = textView {
                            text = "${sprintCalendar.totalDays}"
                            textSize = 46f
                        }.lparams { width = 0; height = wrapContent; weight = 1f }

                        textView {
                            text = "hours:"
                            textSize = 26f
                            rightPadding = dip(10)
                        }.lparams { width = wrapContent; height = wrapContent }

                        totalHoursView = textView {
                            text = "${sprintCalendar.totalHours}"
                            textSize = 46f
                            rightPadding = dip(10)
                        }.lparams { width = 0; height = wrapContent; weight = 1f }


                    }.lparams {
                        width = matchParent
                        height = wrapContent
                    }

                    verticalLayout {
                        gravity = Gravity.CENTER

                        textView {
                            text = "days left:"
                            textSize = 26f
                        }.lparams { width = wrapContent; height = wrapContent }

                        daysLeftView = textView {
                            text = "${sprintCalendar.daysLeft}"
                            textSize = 186f
                            gravity = Gravity.CENTER
                            background = drawableDaysLeftBackground
                        }.lparams { width = matchParent; height = wrapContent; leftMargin = dip(20); rightMargin = dip(20) }

                    }.lparams {
                        topMargin = dip(20)
                        width = matchParent
                        height = wrapContent
                    }

                    verticalLayout {
                        gravity = Gravity.CENTER

                        textView {
                            text = "hours left:"
                            textSize = 26f
                        }.lparams { width = wrapContent; height = wrapContent }

                        hoursLeftView = textView {
                            text = "${sprintCalendar.hoursLeft}"
                            textSize = 112f
                            gravity = Gravity.CENTER
                            background = drawableHoursLeftBackground
                        }.lparams { width = matchParent; height = wrapContent; leftMargin = dip(20); rightMargin = dip(20) }

                    }.lparams {
                        topMargin = dip(20)
                        width = matchParent
                        height = wrapContent
                    }

                }.lparams {
                    width = matchParent
                    height = matchParent
                    padding = dip(4)
                }
            }.lparams { weight = 4f; width = 0; height = matchParent; backgroundColor = colorLightBorder }


            sprintCalendar
                    .onMemberDayChange { member: TeamMember, day: SprintDay ->
                        memberDayViews[Pair(member, day.index)]?.background = memberDayBackground(member, day)
                        updateSummary()
                    }
                    .onMemberChange { member ->
                        for(dayIndex in 0..9) {
                            memberDayViews[Pair(member, dayIndex)]?.background = memberDayBackground(member, sprintCalendar.day(dayIndex))
                        }
                    }
                    .onDayChange { day: SprintDay ->
                        dayViews[day.index]?.background = dayBackground(day)
                        for (member in sprintCalendar.team) {
                            memberDayViews[Pair(member, day.index)]?.background = memberDayBackground(member, day)
                        }
                        updateSummary()
                    }

        }
    }

    private fun updateSummary() {
        totalDaysView.text = "${sprintCalendar.totalDays}"
        totalHoursView.text = "${sprintCalendar.totalHours}"
        daysLeftView.text = "${sprintCalendar.daysLeft}"
        hoursLeftView.text = "${sprintCalendar.hoursLeft}"
    }

    private fun memberDayBackground(member: TeamMember, day: SprintDay) =
            if (day.isHoliday)
                drawablePresenceNone
            else
                when (member.presence(day.date)) {
                    ABSENT -> drawablePresenceNone
                    HALF_DAY -> drawablePresenceHalfDay
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
