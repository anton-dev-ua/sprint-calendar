package net.sourcefusion.agiletools.sprintcalendar

import android.app.Application
import com.facebook.stetho.Stetho
import com.orm.SugarContext
import com.orm.util.ManifestHelper
import net.sourcefusion.agiletools.sprintcalendar.persisting.sugar.SugarHolidaysCalendarRepository

class SprintCalendarApplication : Application() {

    override fun onCreate() {

        println("database: " + ManifestHelper.getDatabaseName(this))

        SugarContext.init(this)

        super.onCreate()
        println("creating application")
        Stetho.initializeWithDefaults(this);

        val sprintCalendarDao = SugarHolidaysCalendarRepository()
        val holidayProvider = PersistedHolidayProvider(sprintCalendarDao)

        println("Dates: " + sprintCalendarDao.readHolidays());

        Injector.holidayProvider = holidayProvider;
//        Injector.dateProvider = object: DateProvider {
//            override fun isToday(date: LocalDate) = LocalDate(2016, 3, 30).equals(date)
//
//            override val today: LocalDate
//                get() = LocalDate(2016, 3, 30)
//        };

    }

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate();
    }
}
