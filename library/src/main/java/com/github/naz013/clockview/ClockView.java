package com.github.naz013.clockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
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

/**
 * Simple Clock view based on canvas. Height of view is always same as width.
 */
public class ClockView extends View {

    private static final String TAG = "ClockView";
    private static final int SHADOW_RADIUS = 5;

    @Nullable
    private Rect mBorderRect;
    @Nullable
    private Rect mClockRect;
    @NonNull
    private Rect[] mInnerCirclesRects = new Rect[2];

    @ColorInt
    private int mBgColor = Color.WHITE;
    private int mCirclesColor = Color.DKGRAY;
    private int mArrowsColor = Color.DKGRAY;
    private int mHourLabelsColor = Color.DKGRAY;
    private int mShadowColor = Color.parseColor("#40000000");

    private boolean mShowHourArrow = true;
    private boolean mShowMinuteArrow = true;
    private boolean mShowSecondArrow = true;
    private boolean mShowHourLabels = true;
    private boolean mShowCircles = true;
    private boolean mShowShadow = true;
    private boolean mShowRectangle = true;

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
        initView(context, null, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public void setShowHourArrow(boolean showHourArrow) {
        this.mShowHourArrow = showHourArrow;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowMinuteArrow(boolean showMinuteArrow) {
        this.mShowMinuteArrow = showMinuteArrow;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowSecondArrow(boolean showSecondArrow) {
        this.mShowSecondArrow = showSecondArrow;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowHourLabels(boolean showHourLabels) {
        this.mShowHourLabels = showHourLabels;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowCircles(boolean showCircles) {
        this.mShowCircles = showCircles;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowShadow(boolean showShadow) {
        this.mShowShadow = showShadow;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShowRectangle(boolean showRectangle) {
        this.mShowRectangle = showRectangle;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setBgColor(@ColorInt int bgColor) {
        this.mBgColor = bgColor;
        this.mShadowPaint.setColor(bgColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setCirclesColor(@ColorInt int circlesColor) {
        this.mCirclesColor = circlesColor;
        this.mCirclePaint.setColor(circlesColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setArrowsColor(@ColorInt int arrowsColor) {
        this.mArrowsColor = arrowsColor;
        this.mArrowPaint.setColor(arrowsColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setHourLabelsColor(@ColorInt int hourLabelsColor) {
        this.mHourLabelsColor = hourLabelsColor;
        this.mShadowPaint.setColor(hourLabelsColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setShadowColor(@ColorInt int shadowColor) {
        this.mShadowColor = shadowColor;
        this.mShadowPaint.setShadowLayer(dp2px(SHADOW_RADIUS), 0, 0, shadowColor);
        invalidate();
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(mBgColor);
        mShadowPaint.setShadowLayer(dp2px(SHADOW_RADIUS), 0, 0, mShadowColor);
        mShadowPaint.setStyle(Paint.Style.FILL);
        setLayerType(LAYER_TYPE_SOFTWARE, mShadowPaint);

        mArrowPaint.setAntiAlias(true);
        mArrowPaint.setColor(mArrowsColor);
        mArrowPaint.setStyle(Paint.Style.STROKE);

        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setColor(mHourLabelsColor);
        mLabelPaint.setStyle(Paint.Style.STROKE);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCirclesColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
    }

    private void drawSecondArrow(Canvas canvas) {

    }

    private void drawMinuteArrow(Canvas canvas) {

    }

    private void drawHourArrow(Canvas canvas) {

    }

    private void drawHourLabels(Canvas canvas) {

    }

    private void drawInnerCircles(Canvas canvas) {

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
            canvas.drawRoundRect(mBorderRect.left, mBorderRect.top, mBorderRect.right, mBorderRect.bottom, dp2px(5), dp2px(5), mShadowPaint);
        }
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
        int clockMargin = (int) (((float) width * 0.2f) / 2f);
        Log.d(TAG, "processCalculations: clock margin -> " + clockMargin);
        mClockRect = new Rect(clockMargin, clockMargin, width - clockMargin, width - clockMargin);
        int middleCircleMargin = (int) (((float) mClockRect.width() * 0.33f) / 2f);
        int smallCircleMargin = (int) (((float) mClockRect.width() * 0.66f) / 2f);
        mInnerCirclesRects[0] = new Rect(mClockRect.left + middleCircleMargin, mClockRect.top + middleCircleMargin,
                mClockRect.right - middleCircleMargin, mClockRect.bottom - middleCircleMargin);
        mInnerCirclesRects[1] = new Rect(mClockRect.left + smallCircleMargin, mClockRect.top + smallCircleMargin,
                mClockRect.right - smallCircleMargin, mClockRect.bottom - smallCircleMargin);
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
}
