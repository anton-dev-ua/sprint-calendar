package net.sourcefusion.agiletools.sprintcalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*
import org.joda.time.LocalDate

class MainActivity : AppCompatActivity() {

    val sprintCalendar = SprintCalendar(
            Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
            DefaultDateProvider(),
            DefaultHolidayProvider(LocalDate(2016, 1, 22), LocalDate(2016, 1, 20)))


    init {
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 26), PresenceType.HALF_DAY)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 27), PresenceType.NONE)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 28), PresenceType.NONE)
        sprintCalendar.team.member(3).setPresence(LocalDate(2016, 1, 29), PresenceType.NONE)
    }

    val holidayProvider: HolidayProvider by lazy {
        println("lazy holiday provider")
        Injector.holidayProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("activity on create")

        sprintCalendar.holidayProvider = holidayProvider
        sprintCalendar.initByCurrentDate();


        val ui = CalendarActivityUI(sprintCalendar)
        println("$sprintCalendar")
        ui.setContentView(this)

    }

}
