package com.github.naz013.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;

/**
 * Simple Clock view based on canvas. Height of view is always same as width.
 */
public class ClockView extends View {

    private static final String TAG = "ClockView";
    private static final int SHADOW_RADIUS = 15;
    private static final float HOUR_ARROW_WIDTH = 0.05f;
    private static final float MINUTE_ARROW_WIDTH = 0.05f;
    private static final float SECOND_ARROW_WIDTH = 0.05f;
    private static final float HOUR_ARROW_LENGTH = 0.75f;
    private static final float MINUTE_ARROW_LENGTH = 0.55f;
    private static final float SECOND_ARROW_LENGTH = 0.35f;

    @Nullable
    private Rect mBorderRect;
    @Nullable
    private Rect mClockRect;
    @NonNull
    private Rect[] mInnerCirclesRects = new Rect[2];
    @NonNull
    private Path mHourArrow = new Path();
    @NonNull
    private Path mMinuteArrow = new Path();
    @NonNull
    private Path mSecondArrow = new Path();

    @ColorInt
    private int mBgColor = Color.WHITE;
    private int mCirclesColor = Color.DKGRAY;
    private int mArrowsColor = Color.DKGRAY;
    private int mHourLabelsColor = Color.DKGRAY;
    private int mShadowColor = Color.parseColor("#20000000");

    private boolean mShowHourArrow = true;
    private boolean mShowMinuteArrow = true;
    private boolean mShowSecondArrow = false;
    private boolean mShowHourLabels = true;
    private boolean mShowCircles = true;
    private boolean mShowShadow = true;
    private boolean mShowRectangle = false;

    private int mHour = 3;
    private int mMinute = 0;
    private int mSecond = 0;
    private int mMinuteArrowLength = 0;
    private int mMinuteArrowWidth = 0;
    private int mHourArrowLength = 0;
    private int mHourArrowWidth = 0;
    private int mSecondArrowLength = 0;
    private int mSecondArrowWidth = 0;

    @NonNull
    private final Paint mBgPaint = new Paint();
    @NonNull
    private final Paint mShadowPaint = new Paint();
    @NonNull
    private final Paint mLabelPaint = new Paint();
    @NonNull
    private final Paint mArrowPaint = new Paint();
    @NonNull
    private final Paint mCirclePaint = new Paint();

    public ClockView(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public void setShowHourArrow(boolean showHourArrow) {
        this.mShowHourArrow = showHourArrow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowMinuteArrow(boolean showMinuteArrow) {
        this.mShowMinuteArrow = showMinuteArrow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowSecondArrow(boolean showSecondArrow) {
        this.mShowSecondArrow = showSecondArrow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowHourLabels(boolean showHourLabels) {
        this.mShowHourLabels = showHourLabels;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowCircles(boolean showCircles) {
        this.mShowCircles = showCircles;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowShadow(boolean showShadow) {
        this.mShowShadow = showShadow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowRectangle(boolean showRectangle) {
        this.mShowRectangle = showRectangle;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setBgColor(@ColorInt int bgColor) {
        this.mBgColor = bgColor;
        this.mShadowPaint.setColor(bgColor);
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setCirclesColor(@ColorInt int circlesColor) {
        this.mCirclesColor = circlesColor;
        this.mCirclePaint.setColor(circlesColor);
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setArrowsColor(@ColorInt int arrowsColor) {
        this.mArrowsColor = arrowsColor;
        this.mArrowPaint.setColor(arrowsColor);
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setHourLabelsColor(@ColorInt int hourLabelsColor) {
        this.mHourLabelsColor = hourLabelsColor;
        this.mShadowPaint.setColor(hourLabelsColor);
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setShadowColor(@ColorInt int shadowColor) {
        this.mShadowColor = shadowColor;
        this.mShadowPaint.setShadowLayer(dp2px(SHADOW_RADIUS), 0, 0, shadowColor);
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public long attachTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, mSecond);

        return calendar.getTimeInMillis();
    }

    @SuppressWarnings("unused")
    public long getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, mSecond);

        return calendar.getTimeInMillis();
    }

    @SuppressWarnings("unused")
    public void setTime(long millis) {
        this.initTime(millis);
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public void setTime(@IntRange(from = 0, to = 23) int hourOfDay, @IntRange(from = 0, to = 59) int minute) {
        this.setTime(hourOfDay, minute, 0);
    }

    @SuppressWarnings("unused")
    public void setTime(@IntRange(from = 0, to = 23) int hourOfDay, @IntRange(from = 0, to = 59) int minute,
                        @IntRange(from = 0, to = 59) int second) {
        this.mHour = hourOfDay;
        this.mMinute = minute;
        this.mSecond = second;
        this.invalidate();
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        initTime(System.currentTimeMillis());
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ClockView, defStyleAttr, 0);
            try {
                mShowRectangle = a.getBoolean(R.styleable.ClockView_cv_showRectangle, mShowRectangle);
                mShowShadow = a.getBoolean(R.styleable.ClockView_cv_showShadow, mShowShadow);
                mShowCircles = a.getBoolean(R.styleable.ClockView_cv_showCircles, mShowCircles);
                mShowHourLabels = a.getBoolean(R.styleable.ClockView_cv_showHourLabels, mShowHourLabels);
                mShowSecondArrow = a.getBoolean(R.styleable.ClockView_cv_showSecondArrow, mShowSecondArrow);
                mShowMinuteArrow = a.getBoolean(R.styleable.ClockView_cv_showMinuteArrow, mShowMinuteArrow);
                mShowHourArrow = a.getBoolean(R.styleable.ClockView_cv_showHourArrow, mShowHourArrow);

                mBgColor = a.getColor(R.styleable.ClockView_cv_backgroundColor, mBgColor);
                mShadowColor = a.getColor(R.styleable.ClockView_cv_shadowColor, mShadowColor);
                mArrowsColor = a.getColor(R.styleable.ClockView_cv_arrowsColor, mArrowsColor);
                mHourLabelsColor = a.getColor(R.styleable.ClockView_cv_hourLabelsColor, mHourLabelsColor);
                mCirclesColor = a.getColor(R.styleable.ClockView_cv_circlesColor, mCirclesColor);
            } catch (Exception ignored) {
            } finally {
                a.recycle();
            }
        }
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(mBgColor);
        mShadowPaint.setShadowLayer(dp2px(SHADOW_RADIUS), 0, 0, mShadowColor);
        mShadowPaint.setStyle(Paint.Style.FILL);
        setLayerType(LAYER_TYPE_SOFTWARE, mShadowPaint);

        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setColor(mArrowsColor);
        mArrowPaint.setStyle(Paint.Style.FILL);

        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setColor(mHourLabelsColor);
        mLabelPaint.setStyle(Paint.Style.STROKE);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCirclesColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    private void initTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mSecond = calendar.get(Calendar.SECOND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long millis = System.currentTimeMillis();
        if (mShowRectangle) {
            drawRoundedRect(canvas);
        }
        if (mShowShadow) {
            drawClockShadow(canvas);
        }
        drawClockBackground(canvas);
        if (mShowCircles) {
            drawInnerCircles(canvas);
        }
        if (mShowHourLabels) {
            drawHourLabels(canvas);
        }
        if (mShowHourArrow) {
            drawHourArrow(canvas);
        }
        if (mShowMinuteArrow) {
            drawMinuteArrow(canvas);
        }
        if (mShowSecondArrow) {
            drawSecondArrow(canvas);
        }
        Log.d(TAG, "onDraw: " + (System.currentTimeMillis() - millis));
    }

    private void drawSecondArrow(Canvas canvas) {
        if (mShowSecondArrow && mClockRect != null && !mSecondArrow.isEmpty()) {
            create(mSecondArrow, mClockRect.centerX(), mClockRect.centerY(), mSecondArrowWidth, mSecondArrowLength, secondAngle());
            canvas.drawPath(mSecondArrow, mArrowPaint);
        }
    }

    private void drawMinuteArrow(Canvas canvas) {
        if (mShowMinuteArrow && mClockRect != null && !mMinuteArrow.isEmpty()) {
            create(mMinuteArrow, mClockRect.centerX(), mClockRect.centerY(), mMinuteArrowWidth, mMinuteArrowLength, minuteAngle());
            canvas.drawPath(mMinuteArrow, mArrowPaint);
        }
    }

    private void drawHourArrow(Canvas canvas) {
        if (mShowHourArrow && mClockRect != null && !mHourArrow.isEmpty()) {
            create(mHourArrow, mClockRect.centerX(), mClockRect.centerY(), mHourArrowWidth, mHourArrowLength, hourAngle());
            canvas.drawPath(mHourArrow, mArrowPaint);
        }
    }

    private void drawHourLabels(Canvas canvas) {

    }

    private void drawInnerCircles(Canvas canvas) {
        for (Rect rect : mInnerCirclesRects) {
            if (rect != null) {
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2f, mCirclePaint);
            }
        }
    }

    private void drawClockBackground(Canvas canvas) {
        if (mClockRect != null) {
            canvas.drawCircle(mClockRect.centerX(), mClockRect.centerY(), mClockRect.width() / 2f, mBgPaint);
        }
    }

    private void drawClockShadow(Canvas canvas) {
        if (mClockRect != null) {
            canvas.drawCircle(mClockRect.centerX(), mClockRect.centerY(), mClockRect.width() / 2f, mShadowPaint);
        }
    }

    private void drawRoundedRect(Canvas canvas) {
        if (mBorderRect != null) {
            canvas.drawRoundRect(mBorderRect.left, mBorderRect.top, mBorderRect.right, mBorderRect.bottom, dp2px(15), dp2px(15), mShadowPaint);
        }
    }

    private float hourAngle() {
        float angle = 0.0f;
        int hour = mHour;
        if (validateValue(hour, 0, 23)) {
            if (hour > 11) {
                hour -= 12;
            }
            int minutes = hourToMinutes(hour);
            if (validateValue(mMinute, 0, 59)) {
                minutes += mMinute;
            }
            angle = (float) minutes / 3.6f;
        }
        Log.d(TAG, "hourAngle: " + angle);
        return angle;
    }

    private float minuteAngle() {
        if (mMinute == 0) return 0.0f;
        float angle = 0.0f;
        int minute = mMinute;
        if (validateValue(minute, 0, 59)) {
            angle = (float) minute * 6f;
        }
        Log.d(TAG, "minuteAngle: " + angle);
        return angle;
    }

    private float secondAngle() {
        if (mSecond == 0) return 0.0f;
        float angle = 0.0f;
        int second = mSecond;
        if (validateValue(second, 0, 59)) {
            angle = (float) second * 6f;
        }
        Log.d(TAG, "secondAngle: " + angle);
        return angle;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(width, width);
        processCalculations(width);
    }

    private void processCalculations(int width) {
        if (width <= 0) return;
        int margin = (int) (((float) width * 0.05f) / 2f);
        Log.d(TAG, "processCalculations: rectangle margin -> " + margin);
        mBorderRect = new Rect(margin, margin, width - margin, width - margin);
        int clockMargin = (int) (((float) width * 0.25f) / 2f);
        Log.d(TAG, "processCalculations: clock margin -> " + clockMargin);
        mClockRect = new Rect(clockMargin, clockMargin, width - clockMargin, width - clockMargin);
        int middleCircleMargin = (int) (((float) mClockRect.width() * 0.33f) / 2f);
        int smallCircleMargin = (int) (((float) mClockRect.width() * 0.66f) / 2f);
        mInnerCirclesRects[0] = new Rect(mClockRect.left + middleCircleMargin, mClockRect.top + middleCircleMargin,
                mClockRect.right - middleCircleMargin, mClockRect.bottom - middleCircleMargin);
        mInnerCirclesRects[1] = new Rect(mClockRect.left + smallCircleMargin, mClockRect.top + smallCircleMargin,
                mClockRect.right - smallCircleMargin, mClockRect.bottom - smallCircleMargin);

        mHourArrowWidth = (int) ((float) mClockRect.width() * HOUR_ARROW_WIDTH);
        mHourArrowLength = (int) ((float) mClockRect.width() / 2f * HOUR_ARROW_LENGTH);

        mMinuteArrowWidth = (int) ((float) mClockRect.width() * MINUTE_ARROW_WIDTH);
        mMinuteArrowLength = (int) ((float) mClockRect.width() / 2f * MINUTE_ARROW_LENGTH);

        mSecondArrowWidth = (int) ((float) mClockRect.width() * SECOND_ARROW_WIDTH);
        mSecondArrowLength = (int) ((float) mClockRect.width() / 2f * SECOND_ARROW_LENGTH);

        create(mSecondArrow, mClockRect.centerX(), mClockRect.centerY(), mSecondArrowWidth, mSecondArrowLength, secondAngle());
        create(mMinuteArrow, mClockRect.centerX(), mClockRect.centerY(), mMinuteArrowWidth, mMinuteArrowLength, minuteAngle());
        create(mHourArrow, mClockRect.centerX(), mClockRect.centerY(), mHourArrowWidth, mHourArrowLength, hourAngle());
    }

    private void create(@NonNull Path path, int cx, int cy, int width, int length, float angle) {
        double circleX = cx + (length * Math.cos(Math.toRadians(angle)));
        double circleY = cy + (length * Math.sin(Math.toRadians(angle)));
        path.reset();
        path.moveTo(cx - (width / 2f), cy);
        path.lineTo(cx + (width / 2f), cy);
        path.lineTo((float) circleX, (float) circleY);
        path.lineTo(cx - (width / 2f), cy);
        path.close();
    }

    @Px
    private int dp2px(int dp) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        if (display != null) {
            display.getMetrics(displaymetrics);
        }
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    private int hourToMinutes(int hour) {
        return hour * 60;
    }

    @SuppressWarnings("SameParameterValue")
    private boolean validateValue(int value, int from, int to) {
        return value >= from && value <= to;
    }
}
