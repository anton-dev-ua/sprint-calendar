package com.example.anton.sprintcalendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import org.joda.time.LocalDate;

public class TeamMemberDayView extends View {

    private TeamMember teamMember;
    private SprintDay day;


    public TeamMemberDayView(Context context) {
        super(context);
        System.out.println("TeamMemberDayView(Context context)");

    }

    public TeamMemberDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("TeamMemberDayView(Context context, AttributeSet attrs)");
//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.TeamMemberDayView,
//                0, 0);

//        System.out.println(a);

        try {
//            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
//            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
        } finally {
//            a.recycle();
        }
    }

    public TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        System.out.println("TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr)");

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        System.out.println("TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
//        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
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
