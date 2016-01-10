package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.LocalDate;

import static com.example.anton.sprintcalendar.PresenceType.FULL_DAY;
import static com.example.anton.sprintcalendar.PresenceType.HALF_DAY;
import static com.example.anton.sprintcalendar.PresenceType.NONE;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private SprintCalendar sprintCalendar;
    private DefaultHolidayProvider holidayProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ViewGroup rootView = (ViewGroup) findViewById(R.id.rootView);

        attachListenerToAll("teamMemberDayView", rootView,
                new MemberDayOnLongClickListener(),
                new MemberDayOnDoubleClickListener());

        attachListenerToAll("dayViewHeader", rootView,
                new DayHeaderOnLongClickListener(),
                null);

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

        holidayProvider = new DefaultHolidayProvider(new LocalDate(2016, 1, 6));
        sprintCalendar = new SprintCalendar(
                team,
                new DefaultDateProvider(),
                holidayProvider
        );


        sprintCalendar.initByCurrentDate();

        activityMainBinding.setSprintCalendar(sprintCalendar);

    }

    private void attachListenerToAll(String tag, ViewGroup rootView, View.OnLongClickListener listener, View.OnClickListener listener2) {
        int childCount = rootView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View view = rootView.getChildAt(childIndex);
            if (view instanceof ViewGroup) {
                attachListenerToAll(tag, (ViewGroup) view, listener, listener2);
            } else if (tag.equals(view.getTag())) {
                if (listener != null) view.setOnLongClickListener(listener);
                if (listener2 != null) view.setOnClickListener(listener2);
            }
        }
    }

    private boolean processTeamMemberDayViewClick(View view, PresenceType oppositePresence) {
        if (view instanceof TeamMemberDayView) {
            TeamMemberDayView dayView = (TeamMemberDayView) view;
            TeamMember teamMember = dayView.getTeamMember();
            SprintDay day = dayView.getDay();
            PresenceType presence = teamMember.presence(day.getDate());
            teamMember.setPresence(day.getDate(), presence == FULL_DAY ? oppositePresence : FULL_DAY);
            activityMainBinding.setSprintCalendar(sprintCalendar);
            return true;
        } else {
            return false;
        }
    }

    private boolean processDayViewClick(View view) {
        SprintDay day = (SprintDay) ((View) view.getParent()).getTag();
        day.setHoliday(!day.isHoliday());
        activityMainBinding.setSprintCalendar(sprintCalendar);
        return true;
    }

    private class MemberDayOnDoubleClickListener implements View.OnClickListener {
        static final long DOUBLE_CLICK_DELAY = 500;
        long lastClickTime = 0;

        @Override
        public void onClick(View view) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_DELAY) {
                processTeamMemberDayViewClick(view, PresenceType.HALF_DAY);
            }
            lastClickTime = clickTime;
        }
    }

    private class MemberDayOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            return processTeamMemberDayViewClick(view, PresenceType.NONE);
        }
    }

    private class DayHeaderOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            return processDayViewClick(view);
        }
    }
}
