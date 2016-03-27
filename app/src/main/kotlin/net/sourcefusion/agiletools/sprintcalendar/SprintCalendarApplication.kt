package net.sourcefusion.agiletools.sprintcalendar

import android.app.Application
import com.facebook.stetho.Stetho
import com.orm.SugarContext
import net.sourcefusion.agiletools.sprintcalendar.persisting.sugar.SugarSprintCalendarDao

class SprintCalendarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        println("creating application")
        SugarContext.init(this)
        Stetho.initializeWithDefaults(this);

        val sprintCalendarDao = SugarSprintCalendarDao()
        val holidayProvider = PersistedHolidayProvider(sprintCalendarDao)

        println("Dates: " + sprintCalendarDao.readHolidays());

        Injector.holidayProvider = holidayProvider;

    }

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate();
    }
}
