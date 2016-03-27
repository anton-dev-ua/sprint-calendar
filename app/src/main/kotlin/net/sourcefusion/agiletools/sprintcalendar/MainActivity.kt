package net.sourcefusion.agiletools.sprintcalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    val holidayProvider: HolidayProvider by lazy {
        println("lazy holiday provider")
        Injector.holidayProvider
    }

    val sprintCalendar: SprintCalendar by lazy {
        SprintCalendar(
                Team(TeamMember("John"), TeamMember("Peter"), TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
                DefaultDateProvider(),
                holidayProvider
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("activity on create")

        sprintCalendar.initByCurrentDate();

        val ui = CalendarActivityUI(sprintCalendar)
        println("$sprintCalendar")
        ui.setContentView(this)

    }
}
