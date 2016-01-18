package net.sourcefusion.agiletools.sprintcalendar

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewManager
import android.widget.AnalogClock
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import kotlin.properties.Delegates

public class SomeActivityUI(var sprintCalendar: SprintCalendar =
                            SprintCalendar(
                                    Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
                                    DefaultDateProvider(),
                                    DefaultHolidayProvider())) : AnkoComponent<MainActivity> {

    val colorDarkBorder = 0x303F9F.opaque
    val colorLightBorder = 0x7684cf.opaque
    val colorMainBackground = 0xFFFFFF.opaque

    val BASIC_STYLE = fun(view: View) {
        when (view) {
            is View -> with(view) {
                backgroundColor = colorMainBackground
            }
        }
    }

    val BASIC_HEADER_STYLE = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                BASIC_STYLE(view)
                textSize = 26f
                textColor = 0x000000.opaque
            }
        }
    }

    val HEADER_TEXT = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                BASIC_HEADER_STYLE(view)
                leftPadding = dip(10)
                gravity = android.view.Gravity.CENTER_VERTICAL + android.view.Gravity.LEFT
            }
        }
    }

    val HEADER_TEXT_2 = fun(view: View) {
        when (view) {
            is TextView -> with(view) {
                BASIC_HEADER_STYLE(view)
                gravity = Gravity.CENTER
            }
        }
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        linearLayout {
            calendarLayout() {
                for (week in 1..2) {
                    rightPadding = dip(6)

                    weekLayout {
                        weekHeaderLayout {
                            textView("week $week").style(HEADER_TEXT).lparams { weight = 2f; width = matchParent; height = 0 }
                            for (member in sprintCalendar.team) {
                                textView(text = member.name).style(HEADER_TEXT).lparams { weight = 1f; width = matchParent; height = 0; topMargin = dip(2) }
                            }
                        }.lparams { weight = 1f; width = 0; height = matchParent }
                        for (day in 1..5) {
                            dayLayout {
                                textView("${Format.date(sprintCalendar.day(day - 1).date)}").style(HEADER_TEXT_2).lparams { weight = 2f; width = matchParent; height = 0; gravity = Gravity.CENTER }
                                for (member in sprintCalendar.team) {
                                    textView("").style(BASIC_STYLE).lparams { weight = 1f; width = matchParent; height = 0; topMargin = dip(2) }
                                }
                            }.lparams { weight = 1f; width = 0; height = matchParent; leftMargin = dip(2) }
                        }
                    }.lparams { weight = 1f; width = matchParent; height = 0; backgroundColor = colorDarkBorder; margin = dip(2) }
                }
            }.lparams { weight = 12f; width = 0; height = matchParent; backgroundColor = colorDarkBorder }

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

}