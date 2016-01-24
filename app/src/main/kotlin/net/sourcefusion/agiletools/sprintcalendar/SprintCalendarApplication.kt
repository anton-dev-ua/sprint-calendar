package net.sourcefusion.agiletools.sprintcalendar

import android.app.Application

class SprintCalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        println("creating application")
    }
}
