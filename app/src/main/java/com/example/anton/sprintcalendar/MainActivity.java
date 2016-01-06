package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.DateTime;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SprintCalendar sprintCalendar = new SprintCalendar(
                new DefaultDateProvider(),
                new DefaultHolidayProvider(new DateTime(2016, 1, 6, 0, 0).toDate())
        );


        sprintCalendar.initByCurrentDate();

        activityMainBinding.setSprintCalendar(sprintCalendar);

    }

}
