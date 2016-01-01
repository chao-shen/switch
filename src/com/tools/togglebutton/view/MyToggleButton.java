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
	/** ������ߵ�λ�� */
	private float slideIconLeft;
	/** ���������ƶ�ʱ������left�����ֵ */
	private float slideIconMaxLeft;
	private boolean isOpen;

	/**
	 * ��xml������ʹ�ã���ϵͳ����
	 * @param context
	 * @param attrs
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		/**  �����ռ� */
		String namespace = "http://schemas.android.com/apk/res/com.tools.togglebutton";
		// ��ȡ����ͼƬ����
		int slideBackgroundId = attrs.getAttributeResourceValue(namespace , "slideBackground", -1);
		int slideIconId = attrs.getAttributeResourceValue(namespace , "slideIcon", -1);
		if (slideBackgroundId != -1 && slideIconId != -1) {
			setToggleBackground(slideBackgroundId, slideIconId);
		}
		
		//  ��ȡ����״̬����
		boolean isOpen = attrs.getAttributeBooleanValue(namespace, "state", false);
		setState(isOpen);
	}

	/**
	 * ���������Java������ʹ��
	 * @param context
	 */
	public MyToggleButton(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ����MyToggleButton�Ŀ�͸�
		setMeasuredDimension(slideBackground.getWidth(), slideBackground.getHeight());
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		// ������
		float left = 0f;	// ָ�������ݻ���x���������
		float top = 0f;		// ָ�������ݻ���y���������
		canvas.drawBitmap(slideBackground, left, top, null);
		
		// ������
		drawSlideIcon(canvas);
	}

	/** ������ */
	private void drawSlideIcon(Canvas canvas) {
		// Ԥ��������Χ
		if (slideIconLeft < 0) {
			slideIconLeft = 0;
		} else if (slideIconLeft > slideIconMaxLeft) {
			// ���������ƶ�ʱ������left�����ֵ = ������ �C �����
			slideIconLeft = slideIconMaxLeft;
		}
		
		// �ж�״̬�Ƿ��з����ı�
		boolean isOpenTemp = slideIconLeft > 0;	// �������0�����Ǵ�״̬
		if (isHandUp) {
			isHandUp = false;
			if (isOpen != isOpenTemp && listener != null) {
				// ���״̬�����ı䣬��֪ͨ������
				listener.onStateChanged(isOpenTemp);
			}
			isOpen = isOpenTemp;
		}
		canvas.drawBitmap(slideIcon, slideIconLeft, 0, null);
	}
	
	/** �ж���ָ�Ƿ���̧�����Ϊtrue����̧��� */
	boolean isHandUp;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ������ʱ����㻬��left = ����λ��event.getX() �C ����� / 2
			slideIconLeft = event.getX() - slideIcon.getWidth() / 2;
			break;
		case MotionEvent.ACTION_MOVE:
			slideIconLeft = event.getX() - slideIcon.getWidth() / 2;
			break;
		case MotionEvent.ACTION_UP:
			isHandUp = true;
			// ��ָ�ɿ�ʱ�����㻬��Ӧ�û�������ߣ����ǻ������ұߣ�
			if (event.getX() < slideBackground.getWidth() / 2) {
				// �����ָ̧���λ�� < ������ / 2���ѻ��黬�������
				slideIconLeft = 0;
			} else {
				// ���򻬵����ұ�
				slideIconLeft = slideIconMaxLeft;
			}

			break;
		}
		
		invalidate();	// ֪ͨϵͳȥ����UI������������onDraw����
		return true;
	}

	/**
	 * ���ÿ��ذ�ť�ı���
	 * @param slideBackgroundId
	 * @param slideIconId
	 */
	public void setToggleBackground(int slideBackgroundId, int slideIconId) {
		slideBackground = BitmapFactory.decodeResource(getResources(), slideBackgroundId);
		slideIcon = BitmapFactory.decodeResource(getResources(), slideIconId);
		
		slideIconMaxLeft = slideBackground.getWidth() - slideIcon.getWidth();
	}

	/**
	 * ���ÿ���״̬
	 * @param isOpen
	 */
	public void setState(boolean isOpen) {
		isHandUp = true;
		if (isOpen) {
			// ����Ǵ�״̬����ѻ��黭�����ұ�
			slideIconLeft = slideIconMaxLeft;
		} else {
			// ����ǲ���״̬����ѻ��黭�������
			slideIconLeft = 0;
		}
		invalidate();	// ֪ͨϵͳȥ����UI������������onDraw����
	}

	/** ����״̬�ı�ļ����� */
	public void setOnStateChangedListener(OnStateChangedListener listener) {
		this.listener = listener;
	}
	
	/**
	 * ״̬�ı�ļ�����
	 * @author dzl
	 *
	 */
	public interface OnStateChangedListener {
		/**
		 * ��״̬�����ı��ʱ���ִ���������
		 * @param isOpen �����Ƿ��Ǵ򿪵�
		 */
		void onStateChanged(boolean isOpen);
	}
	
}
