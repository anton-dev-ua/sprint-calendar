package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.LocalDate;

import static com.example.anton.sprintcalendar.BR.*;
import static com.example.anton.sprintcalendar.PresenceType.FULL_DAY;
import static com.example.anton.sprintcalendar.PresenceType.HALF_DAY;
import static com.example.anton.sprintcalendar.PresenceType.NONE;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    View summaryView;
    private ActivityMainBinding activityMainBinding;
    private SprintCalendar sprintCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        int weekIds[] = {R.id.firstWeek, R.id.secondWeek};
        int dayIds[] = {R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5};
        int memberIds[] = {R.id.user1, R.id.user2, R.id.user3, R.id.user4, R.id.user5, R.id.user6};

        for (int weekId : weekIds) {
            View weekView = findViewById(weekId);
            for (int dayId : dayIds) {
                View dayView = weekView.findViewById(dayId);
                for (int memberId : memberIds) {
                    dayView.findViewById(memberId).setOnLongClickListener(this);
                }
            }
        }

        summaryView = findViewById(R.id.sprintTotalHours);

        TeamMember pavel = new TeamMember("Pavel");
        pavel.setPresence(new LocalDate(2016, 1, 13), HALF_DAY);
        pavel.setPresence(new LocalDate(2016, 1, 14), NONE);
        TeamMember alex = new TeamMember("Alex");
        alex.setPresence(new LocalDate(2016, 1, 6), NONE);
        Team team = new Team(
                alex,
                new TeamMember("Anton"),
                new TeamMember("Dima"),
                new TeamMember("Paulina"),
                pavel
        );

        sprintCalendar = new SprintCalendar(
                team, new DefaultDateProvider(),
                new DefaultHolidayProvider(new LocalDate(2016, 1, 6))
        );


        sprintCalendar.initByCurrentDate();

        activityMainBinding.setSprintCalendar(sprintCalendar);
        activityMainBinding.setTeam(team);

    }

    @Override
    public boolean onLongClick(View v) {
        if (v instanceof TeamMemberDayView) {
            TeamMemberDayView dayView = (TeamMemberDayView) v;
            TeamMember teamMember = dayView.getTeamMember();
            SprintDay day = dayView.getDay();
            PresenceType presence = teamMember.presence(day.getDate());
            teamMember.setPresence(day.getDate(), presence == NONE ? HALF_DAY : presence == HALF_DAY ? FULL_DAY : NONE);
            dayView.invalidate();
            dayView.requestLayout();
            sprintCalendar.notifyPropertyChanged(com.example.anton.sprintcalendar.BR.totalHours);
            sprintCalendar.notifyPropertyChanged(com.example.anton.sprintcalendar.BR.hoursLeft);
            return true;
        } else {
            return false;
        }
    }
}
