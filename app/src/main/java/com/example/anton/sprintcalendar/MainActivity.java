package com.example.anton.sprintcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date startDate = null;
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            startDate = formatter.parse("04.01.2016");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        ((TextView)findViewById(R.id.headerWeek1Day1)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek1Day2)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek1Day3)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek1Day4)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek1Day5)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek2Day1)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek2Day2)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek2Day3)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek2Day4)).setText(formatter.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        ((TextView)findViewById(R.id.headerWeek2Day5)).setText(formatter.format(calendar.getTime()));



    }
}
