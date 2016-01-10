package com.example.anton.sprintcalendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.joda.time.LocalDate;

import static com.example.anton.sprintcalendar.PresenceType.FULL_DAY;
import static com.example.anton.sprintcalendar.PresenceType.HALF_DAY;
import static com.example.anton.sprintcalendar.PresenceType.NONE;

public class TeamMemberDayView extends View {

    private TeamMember teamMember;
    private SprintDay day;
    @ColorInt
    private int presentColor = getResources().getColor(R.color.colorWhite);
    private int absentColor = getResources().getColor(R.color.colorAbsence);
    private Paint presentPaint = null;
    private Paint absentPaint = null;


    public TeamMemberDayView(Context context) {
        super(context);
        init(context, null);
    }

    public TeamMemberDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        if (isInEditMode()) {
            teamMember = new TeamMember("John");
            day = new SprintDay(new LocalDate(2016, 1, 7), false, false);
            teamMember.setPresence(day.getDate(), PresenceType.HALF_DAY);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.sprintCalendar, 0, 0);

        try {
            presentPaint = buildPaint(a.getColor(R.styleable.sprintCalendar_present, presentColor));
            absentPaint = buildPaint(a.getColor(R.styleable.sprintCalendar_absent, absentColor));
        } finally {
            a.recycle();
        }
    }

    private Paint buildPaint(int color) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        return paint;
    }

    public TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (day.isHoliday()) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), presentPaint);
        } else {
            switch (teamMember.presence(day.getDate())) {
                case FULL_DAY:
                    canvas.drawRect(0, 0, getWidth(), getHeight(), presentPaint);
                    break;
                case HALF_DAY:
                    canvas.drawRect(0, 0, getWidth() / 2, getHeight(), presentPaint);
                    canvas.drawRect(getWidth() / 2, 0, getWidth(), getHeight(), absentPaint);
                    break;
                case NONE:
                    canvas.drawRect(0, 0, getWidth(), getHeight(), absentPaint);
            }
        }
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public void setDay(SprintDay day) {
        this.day = day;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public SprintDay getDay() {
        return day;
    }
}
