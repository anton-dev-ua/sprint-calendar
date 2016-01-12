package net.sourcefusion.agiletools.sprintcalendar;

import org.joda.time.LocalDate;

public interface DateProvider {
    boolean isToday(LocalDate date);

    LocalDate getToday();
}
