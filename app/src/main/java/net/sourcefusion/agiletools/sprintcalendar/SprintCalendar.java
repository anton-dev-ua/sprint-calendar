package net.sourcefusion.agiletools.sprintcalendar;

import com.google.common.base.Preconditions;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import static org.joda.time.DateTimeConstants.MONDAY;

public class SprintCalendar {

    public static final SprintDay DAY_PLACEHOLDER = new SprintDay(new LocalDate(1970, 1, 1), false, false);

    private LocalDate sprintBaseDate = new LocalDate(2015, 12, 7);

    private LocalDate firstDate;
    private LocalDate lastDate;
    private SprintDay[] day;
    private DateProvider dateProvider;
    private HolidayProvider holidayProvider;
    private Team team;

    public SprintCalendar(Team team, DateProvider dateProvider, HolidayProvider holidayProvider) {
        this.team = team;
        Preconditions.checkArgument(dateProvider != null, "date provider cannot be null");
        Preconditions.checkArgument(holidayProvider != null, "holiday provider cannot be null");
        this.dateProvider = dateProvider;
        this.holidayProvider = holidayProvider;
    }

    public void initByCurrentDate() {
        LocalDate today = dateProvider.getToday();
        int modDiff = Days.daysBetween(sprintBaseDate, today).getDays() % 14;
        if(modDiff < 0) modDiff+=14;
        LocalDate startDate = today.minusDays(modDiff);
        initByStartDate(startDate);
    }

    private void initByStartDate(LocalDate sprintStartDate) {
        Preconditions.checkArgument(MONDAY == sprintStartDate.getDayOfWeek(), "Start date of sprint should be monday");
        calculateDates(sprintStartDate);
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public SprintDay day(int index) {
        return index < day.length ? day[index] : DAY_PLACEHOLDER;
    }

    public int getDaysLeft() {
        return daysBetween(dateProvider.getToday());
    }

    public int getTotalDays() {
        return daysBetween(firstDate);
    }

    public Team getTeam() {
        return team;
    }

    public int getTotalHours() {
        return (int) hoursBetween(firstDate);
    }

    public int getHoursLeft() {
        return (int) hoursBetween(dateProvider.getToday());
    }

    private void calculateDates(LocalDate sprintStartDate) {
        day = new SprintDay[10];
        firstDate = sprintStartDate;
        lastDate = firstDate.plusDays(11);
        int dayIndex = 0;
        for (LocalDate date = firstDate; date.compareTo(lastDate) <= 0; date = date.plusDays(1)) {
            if (!holidayProvider.isWeekend(date)) {
                day[dayIndex++] = new SprintDay(date, holidayProvider.isHoliday(date), dateProvider.isToday(date));
            }
        }
    }

    private int daysBetween(LocalDate startDate) {
        int days = 0;
        for (SprintDay sprintDay : day) {
            if (!sprintDay.isHoliday() && sprintDay.getDate().compareTo(startDate) >= 0) {
                days++;
            }
        }
        return days;
    }

    private float hoursBetween(LocalDate startDate) {
        float totalHours = 0;
        for (SprintDay sprintDay : day) {
            if (!sprintDay.isHoliday() && sprintDay.getDate().compareTo(startDate) >= 0) {
                for (TeamMember member : team) {
                    totalHours += member.presence(sprintDay.getDate()).getHours();
                }
            }
        }
        return totalHours;
    }
}
