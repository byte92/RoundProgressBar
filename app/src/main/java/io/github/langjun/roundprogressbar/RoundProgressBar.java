package io.github.langjun.roundprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆环ProgressBar 可以通过控制进度来调整圆圆弧的长度 <b>圆心在View对角线交点的位置,直径为View宽高的最小值减去圆环宽度<b/>
 *
 * @author langjun
 * @date 16/6/12 15:31
 */
public class RoundProgressBar extends View {

	private static final String TAG = "RoundProgressBar";
	private static final int DEFAULT_MAX_VALUE = 100;
	private static final int DEFAULT_PROGRESS_VALUE = 0;
	private static final int DEFAULT_CIRCLE_COLOR = Color.BLACK;
	private static final int DEFAULT_CIRCLE_WIDTH = 20;
	private static final int DEFAULT_ZERO_DEGREE = -90; // 椭圆默认0°角为三点钟方向开始,默认-90°将调整为12点钟方向

	private Paint mPaint;
	private int mCircleColor;
	private int mCircleWidth;

	private int mProgress;
	private int mMax;

	public RoundProgressBar(Context context) {
		super(context);
		initProgressBar();
		initPaint();
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initProgressBar();
		initPaint();
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initProgressBar();
		initPaint();
	}

	/**
	 * 初始化ProgressBar需要的参数
	 */
	private void initProgressBar() {
		mMax = DEFAULT_MAX_VALUE;
		mProgress = DEFAULT_PROGRESS_VALUE;
		mCircleColor = DEFAULT_CIRCLE_COLOR;
		mCircleWidth = DEFAULT_CIRCLE_WIDTH;
	}

	/**
	 * 初始化Paint
	 */
	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 抗锯齿
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setStyle(Paint.Style.STROKE); // 设置画笔样式
		mPaint.setStrokeCap(Paint.Cap.ROUND); // 设置线条为圆形
		mPaint.setColor(mCircleColor);
	}

	/**
	 * 刷新Paint
	 */
	private void refreshPaint() {
		mPaint.setStrokeWidth(mCircleWidth);
		mPaint.setColor(mCircleColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawTrack(canvas);
	}

	/**
	 * 绘制
	 *
	 * @param canvas
	 *            Canvas
	 */
	private void drawTrack(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();

		int radius = (Math.min(width, height) - mCircleWidth) / 2;

		int top = height / 2 - radius;
		int bottom = height / 2 + radius;
		int left = width / 2 - radius;
		int right = width / 2 + radius;

		RectF mOval = new RectF(left, top, right, bottom);

		canvas.drawArc(mOval, DEFAULT_ZERO_DEGREE, calcSweepAngle(), false, mPaint);
	}

	/**
	 * 计算应该旋转的角度
	 *
	 * @return 角度
	 */
	private float calcSweepAngle() {
		if (mMax <= 0) {
			return 0;
		}
		return (float) mProgress / mMax * 360;
	}

	/**
	 * 设置当前进度
	 *
	 * @param progress
	 *            进度
	 */
	public synchronized void setProgress(int progress) {
		if (progress > mMax) {
			progress = mMax;
		}

		if (progress < 0) {
			progress = 0;
		}

		if (mProgress != progress) {
			mProgress = progress;
			postInvalidate();
		}

	}

	/**
	 * 设置最大值
	 *
	 * @param max
	 *            最大值
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			max = 0;
		}

		if (mMax != max) {
			mMax = max;

			if (mMax < mProgress) {
				mProgress = mMax;
			}

			postInvalidate();
		}
	}

	/**
	 * 设置圆环的宽度
	 *
	 * @param circleWidth
	 *            宽度
	 */
	public void setCircleWidth(int circleWidth) {
		if (mCircleWidth != circleWidth) {
			mCircleWidth = circleWidth;
			refreshPaint();
			invalidate();
		}
	}

	/**
	 * 用于设置圆环颜色
	 *
	 * @param circleColor
	 *            颜色
	 */
	public void setCircleColor(int circleColor) {
		if (mCircleColor != circleColor) {
			mCircleColor = circleColor;
			refreshPaint();
			invalidate();
		}
	}
}
