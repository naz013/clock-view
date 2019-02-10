package com.github.naz013.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
 * Simple Analogue Clock view based on canvas. Height of view is always same as width.
 */
public class ClockView extends View {

    private static final String TAG = "ClockView";
    private static final int SHADOW_RADIUS = 15;
    private static final float HOUR_ARROW_WIDTH = 0.05f;
    private static final float MINUTE_ARROW_WIDTH = 0.03f;
    private static final float SECOND_ARROW_WIDTH = 0.02f;
    private static final float HOUR_ARROW_LENGTH = 0.55f;
    private static final float MINUTE_ARROW_LENGTH = 0.75f;
    private static final float SECOND_ARROW_LENGTH = 0.35f;

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
    @NonNull
    private Point[] mLabelPoints = new Point[4];

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

    private int mHour = 3;
    private int mMinute = 0;
    private int mSecond = 0;

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
    public boolean isShowHourArrow() {
        return mShowHourArrow;
    }

    @SuppressWarnings("unused")
    public void setShowMinuteArrow(boolean showMinuteArrow) {
        this.mShowMinuteArrow = showMinuteArrow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isShowMinuteArrow() {
        return mShowMinuteArrow;
    }

    @SuppressWarnings("unused")
    public void setShowSecondArrow(boolean showSecondArrow) {
        this.mShowSecondArrow = showSecondArrow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isShowSecondArrow() {
        return mShowSecondArrow;
    }

    @SuppressWarnings("unused")
    public void setShowHourLabels(boolean showHourLabels) {
        this.mShowHourLabels = showHourLabels;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isShowHourLabels() {
        return mShowHourLabels;
    }

    @SuppressWarnings("unused")
    public void setShowCircles(boolean showCircles) {
        this.mShowCircles = showCircles;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isShowCircles() {
        return mShowShadow;
    }

    @SuppressWarnings("unused")
    public void setShowShadow(boolean showShadow) {
        this.mShowShadow = showShadow;
        this.invalidate();
    }

    @SuppressWarnings("unused")
    public boolean isShowShadow() {
        return mShowShadow;
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
        this.mShadowPaint.setColor(shadowColor);
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
        int textSize = 25;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ClockView, defStyleAttr, 0);
            try {
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
                textSize = a.getDimensionPixelSize(R.styleable.ClockView_cv_labelTextSize, textSize);
            } catch (Exception ignored) {
            } finally {
                a.recycle();
            }
        }
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(mShadowColor);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(dp2px(SHADOW_RADIUS), BlurMaskFilter.Blur.OUTER));
        mShadowPaint.setStyle(Paint.Style.FILL);
        setLayerType(LAYER_TYPE_SOFTWARE, mShadowPaint);

        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setColor(mArrowsColor);
        mArrowPaint.setStyle(Paint.Style.FILL);

        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setColor(mHourLabelsColor);
        mLabelPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLabelPaint.setTextSize(textSize);

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
        if (mClockRect != null && !mSecondArrow.isEmpty()) {
            canvas.save();
            canvas.rotate(secondAngle(), mClockRect.centerX(), mClockRect.centerY());
            canvas.drawPath(mSecondArrow, mArrowPaint);
            canvas.restore();
        }
    }

    private void drawMinuteArrow(Canvas canvas) {
        if (mClockRect != null && !mMinuteArrow.isEmpty()) {
            canvas.save();
            canvas.rotate(minuteAngle(), mClockRect.centerX(), mClockRect.centerY());
            canvas.drawPath(mMinuteArrow, mArrowPaint);
            canvas.restore();
        }
    }

    private void drawHourArrow(Canvas canvas) {
        if (mClockRect != null && !mHourArrow.isEmpty()) {
            canvas.save();
            canvas.rotate(hourAngle(), mClockRect.centerX(), mClockRect.centerY());
            canvas.drawPath(mHourArrow, mArrowPaint);
            canvas.restore();
        }
    }

    private void drawHourLabels(Canvas canvas) {
        Point p = mLabelPoints[0];
        if (p != null) {
            drawText(canvas, p, "12");
        }
        p = mLabelPoints[1];
        if (p != null) {
            drawText(canvas, p, "3");
        }
        p = mLabelPoints[2];
        if (p != null) {
            drawText(canvas, p, "6");
        }
        p = mLabelPoints[3];
        if (p != null) {
            drawText(canvas, p, "9");
        }
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

    private void drawText(Canvas canvas, Point p, String text) {
        Rect r = new Rect();
        mLabelPaint.setTextAlign(Paint.Align.LEFT);
        mLabelPaint.getTextBounds(text, 0, text.length(), r);

        float x = p.x - (r.width() / 2f) - r.left;
        float y = p.y + (r.height() / 2f) - r.bottom;
        canvas.drawText(text, x, y, mLabelPaint);
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
            angle = (float) minutes * 0.5f;
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
        mClockRect = new Rect(margin, margin, width - margin, width - margin);
        int middleCircleMargin = (int) (((float) mClockRect.width() * 0.33f) / 2f);
        int smallCircleMargin = (int) (((float) mClockRect.width() * 0.66f) / 2f);
        mInnerCirclesRects[0] = new Rect(mClockRect.left + middleCircleMargin, mClockRect.top + middleCircleMargin,
                mClockRect.right - middleCircleMargin, mClockRect.bottom - middleCircleMargin);
        mInnerCirclesRects[1] = new Rect(mClockRect.left + smallCircleMargin, mClockRect.top + smallCircleMargin,
                mClockRect.right - smallCircleMargin, mClockRect.bottom - smallCircleMargin);

        int mLabelLength = (int) ((float) mClockRect.width() * 0.85f / 2f);

        mLabelPoints[0] = circlePoint(mClockRect.centerX(), mClockRect.centerY(), mLabelLength, 270);
        mLabelPoints[1] = circlePoint(mClockRect.centerX(), mClockRect.centerY(), mLabelLength, 0);
        mLabelPoints[2] = circlePoint(mClockRect.centerX(), mClockRect.centerY(), mLabelLength, 90);
        mLabelPoints[3] = circlePoint(mClockRect.centerX(), mClockRect.centerY(), mLabelLength, 180);

        int mHourArrowWidth = (int) ((float) mClockRect.width() * HOUR_ARROW_WIDTH);
        int mHourArrowLength = (int) ((float) mClockRect.width() / 2f * HOUR_ARROW_LENGTH);

        int mMinuteArrowWidth = (int) ((float) mClockRect.width() * MINUTE_ARROW_WIDTH);
        int mMinuteArrowLength = (int) ((float) mClockRect.width() / 2f * MINUTE_ARROW_LENGTH);

        int mSecondArrowWidth = (int) ((float) mClockRect.width() * SECOND_ARROW_WIDTH);
        int mSecondArrowLength = (int) ((float) mClockRect.width() / 2f * SECOND_ARROW_LENGTH);

        create(mSecondArrow, mClockRect.centerX(), mClockRect.centerY(), mSecondArrowWidth, mSecondArrowLength);
        create(mMinuteArrow, mClockRect.centerX(), mClockRect.centerY(), mMinuteArrowWidth, mMinuteArrowLength);
        create(mHourArrow, mClockRect.centerX(), mClockRect.centerY(), mHourArrowWidth, mHourArrowLength);
    }

    private void create(@NonNull Path path, int cx, int cy, int width, int length) {
        path.reset();
        path.moveTo(cx + (width / 2f), cy);
        path.lineTo(cx, cy - length);
        path.lineTo(cx - (width / 2f), cy);
        path.lineTo(cx, cy + (length * 0.1f));
        path.lineTo(cx + (width / 2f), cy);
        path.close();
    }

    private Point circlePoint(int cx, int cy, int length, float angle) {
        double rad = Math.toRadians(angle);
        double circleX = cx + (length * Math.cos(rad));
        double circleY = cy + (length * Math.sin(rad));
        return new Point((int) circleX, (int) circleY);
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
