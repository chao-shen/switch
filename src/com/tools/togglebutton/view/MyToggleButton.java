package com.tools.togglebutton.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyToggleButton extends View {

	private OnStateChangedListener listener;
	private Bitmap slideBackground;
	private Bitmap slideIcon;
	/** 滑块左边的位置 */
	private float slideIconLeft;
	/** 滑块往右移动时，滑块left的最大值 */
	private float slideIconMaxLeft;
	private boolean isOpen;

	/**
	 * 在xml代码中使用，由系统调用
	 * @param context
	 * @param attrs
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		/**  命名空间 */
		String namespace = "http://schemas.android.com/apk/res/com.tools.togglebutton";
		// 读取背景图片属性
		int slideBackgroundId = attrs.getAttributeResourceValue(namespace , "slideBackground", -1);
		int slideIconId = attrs.getAttributeResourceValue(namespace , "slideIcon", -1);
		if (slideBackgroundId != -1 && slideIconId != -1) {
			setToggleBackground(slideBackgroundId, slideIconId);
		}
		
		//  读取开关状态属性
		boolean isOpen = attrs.getAttributeBooleanValue(namespace, "state", false);
		setState(isOpen);
	}

	/**
	 * 这个方法在Java代码中使用
	 * @param context
	 */
	public MyToggleButton(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 设置MyToggleButton的宽和高
		setMeasuredDimension(slideBackground.getWidth(), slideBackground.getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// 画背景
		float left = 0f;	// 指定把内容画在x方向的哪里
		float top = 0f;		// 指定把内容画在y方向的哪里
		canvas.drawBitmap(slideBackground, left, top, null);
		
		// 画滑块
		drawSlideIcon(canvas);
	}

	/** 画滑块 */
	private void drawSlideIcon(Canvas canvas) {
		// 预防超出范围
		if (slideIconLeft < 0) {
			slideIconLeft = 0;
		} else if (slideIconLeft > slideIconMaxLeft) {
			// 滑块往右移动时，滑块left的最大值 = 背景宽 – 滑块宽
			slideIconLeft = slideIconMaxLeft;
		}
		
		// 判断状态是否有发生改变
		boolean isOpenTemp = slideIconLeft > 0;	// 如果大于0，则是打开状态
		if (isHandUp) {
			isHandUp = false;
			if (isOpen != isOpenTemp && listener != null) {
				// 如果状态发生改变，则通知监听器
				listener.onStateChanged(isOpenTemp);
			}
			isOpen = isOpenTemp;
		}
		canvas.drawBitmap(slideIcon, slideIconLeft, 0, null);
	}
	
	/** 判断手指是否是抬起，如果为true则是抬起的 */
	boolean isHandUp;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 滑动的时候计算滑块left = 触摸位置event.getX() – 滑块宽 / 2
			slideIconLeft = event.getX() - slideIcon.getWidth() / 2;
			break;
		case MotionEvent.ACTION_MOVE:
			slideIconLeft = event.getX() - slideIcon.getWidth() / 2;
			break;
		case MotionEvent.ACTION_UP:
			isHandUp = true;
			// 手指松开时，计算滑块应该滑到最左边，还是滑到最右边：
			if (event.getX() < slideBackground.getWidth() / 2) {
				// 如果手指抬起的位置 < 背景宽 / 2，把滑块滑到最左边
				slideIconLeft = 0;
			} else {
				// 否则滑到最右边
				slideIconLeft = slideIconMaxLeft;
			}

			break;
		}
		
		invalidate();	// 通知系统去更新UI，它里面会调用onDraw方法
		return true;
	}

	/**
	 * 设置开关按钮的背景
	 * @param slideBackgroundId
	 * @param slideIconId
	 */
	public void setToggleBackground(int slideBackgroundId, int slideIconId) {
		slideBackground = BitmapFactory.decodeResource(getResources(), slideBackgroundId);
		slideIcon = BitmapFactory.decodeResource(getResources(), slideIconId);
		
		slideIconMaxLeft = slideBackground.getWidth() - slideIcon.getWidth();
	}

	/**
	 * 设置开关状态
	 * @param isOpen
	 */
	public void setState(boolean isOpen) {
		isHandUp = true;
		if (isOpen) {
			// 如果是打开状态，则把滑块画到最右边
			slideIconLeft = slideIconMaxLeft;
		} else {
			// 如果是不打开状态，则把滑块画到最左边
			slideIconLeft = 0;
		}
		invalidate();	// 通知系统去更新UI，它里面会调用onDraw方法
	}

	/** 设置状态改变的监听器 */
	public void setOnStateChangedListener(OnStateChangedListener listener) {
		this.listener = listener;
	}
	
	/**
	 * 状态改变的监听器
	 * @author dzl
	 *
	 */
	public interface OnStateChangedListener {
		/**
		 * 当状态发生改变的时候会执行这个方法
		 * @param isOpen 开关是否是打开的
		 */
		void onStateChanged(boolean isOpen);
	}
	
}
